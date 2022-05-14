package com.netcracker.jspclass.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.vfs.VirtualFileManager
import com.netcracker.jspclass.Constants
import com.netcracker.jspclass.vfs.JspVirtualFile

/**
 * SEBY0408
 */
class ToggleJspClass : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val file = event.getData(LangDataKeys.VIRTUAL_FILE) ?: return
        val project = event.project ?: return
        val manager = FileEditorManager.getInstance(project)
        if (file is JspVirtualFile) {
            val source = file.source ?: return
            file.toggle = true
            manager.closeFile(file)
            manager.openFile(source, true)
        } else if (Constants.JSP == file.extension) {
            val fileSystem = VirtualFileManager.getInstance().getFileSystem(Constants.PROTOCOL)
            val jspFile = (fileSystem.findFileByPath(file.path) ?: return) as JspVirtualFile
            jspFile.toggle = false
            manager.closeFile(file)
            manager.openFile(jspFile, true)
        }
    }

    override fun update(event: AnActionEvent) {
        val file = event.getData(LangDataKeys.VIRTUAL_FILE) ?: return
        event.presentation.isVisible = file.extension == Constants.JAVA || file.extension == Constants.JSP
    }
}