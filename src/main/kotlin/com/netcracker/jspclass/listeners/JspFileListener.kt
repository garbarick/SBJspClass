package com.netcracker.jspclass.listeners

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.netcracker.jspclass.Constants
import com.netcracker.jspclass.util.Helper
import com.netcracker.jspclass.vfs.JspVirtualFile
import com.netcracker.jspclass.vfs.JspVirtualFileSystem
import java.io.IOException

/**
 * SEBY0408
 */
class JspFileListener : FileEditorManagerListener {
    override fun fileOpened(manager: FileEditorManager, file: VirtualFile) {
        val project = manager.project
        if (Constants.JSP == file.extension) {
            val fileSystem = VirtualFileManager.getInstance().getFileSystem(Constants.PROTOCOL) as JspVirtualFileSystem
            var jspFile = fileSystem.findFileByPath(file.path) as JspVirtualFile?
            if (jspFile == null) {
                jspFile = JspVirtualFile(file)
                fileSystem.addFile(jspFile)
                val module = ModuleUtil.findModuleForFile(file, project)
                if (module != null) {
                    val moduleFile = fileSystem.getModuleFile(project.getName(), module.name)
                    fileSystem.addFile(moduleFile, jspFile)
                }
            }
            if (!jspFile.toggle) {
                manager.closeFile(file)
                try {
                    manager.openFile(jspFile, false)
                } catch (e: Exception) {
                    thisLogger().warn("fileOpened error:", e)
                }
                Helper(project).addToSource(jspFile)
            }
        }
    }

    override fun fileClosed(manager: FileEditorManager, file: VirtualFile) {
        if (file is JspVirtualFile && !file.toggle) {
            val fileSystem = VirtualFileManager.getInstance().getFileSystem(Constants.PROTOCOL) as JspVirtualFileSystem
            try {
                fileSystem.deleteFile(this, file)
            } catch (e: IOException) {
                thisLogger().warn("fileClosed error:", e)
            }
        }
    }

    override fun selectionChanged(fileEditorManagerEvent: FileEditorManagerEvent) {
    }
}