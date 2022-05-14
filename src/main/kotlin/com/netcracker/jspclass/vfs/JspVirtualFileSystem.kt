package com.netcracker.jspclass.vfs

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileListener
import com.intellij.openapi.vfs.VirtualFileSystem
import com.netcracker.jspclass.Constants
import java.util.*
import java.util.concurrent.atomic.AtomicLong

/**
 * SEBY0408
 */
class JspVirtualFileSystem : VirtualFileSystem() {
    companion object {
        val IDS = AtomicLong()
    }

    private val files = HashMap<String, VirtualFile>()

    override fun getProtocol(): String {
        return Constants.PROTOCOL
    }

    override fun findFileByPath(path: String): VirtualFile? {
        var file = files[path]
        if (file == null) {
            val items = path.split("/")
            if (items.size == 1) {
                file = addFile(JspVirtualFile(items[0]))
            } else if (items.size == 2) {
                file = getModuleFile(items[0], items[1])
            }
        }
        return file
    }

    override fun refresh(asynchronous: Boolean) {
    }

    override fun refreshAndFindFileByPath(path: String): VirtualFile? {
        val file = findFileByPath(path) as JspVirtualFile?
        if (file != null && !file.isDirectory) {
            file.refreshContent()
        }
        return file
    }

    override fun addVirtualFileListener(listener: VirtualFileListener) {
    }

    override fun removeVirtualFileListener(listener: VirtualFileListener) {
    }

    public override fun deleteFile(requestor: Any, file: VirtualFile) {
        files.remove(file.path)
        val parent: VirtualFile = file.parent ?: return
        (parent as JspVirtualFile).deleteChild(file)
    }

    override fun moveFile(requestor: Any, vFile: VirtualFile, newParent: VirtualFile) {
    }

    override fun renameFile(requestor: Any, vFile: VirtualFile, newName: String) {
    }

    fun createFile(dir: VirtualFile, name: String): VirtualFile {
        val result = JspVirtualFile(name)
        result.setParent(dir)
        return result
    }

    override fun createChildFile(requestor: Any, dir: VirtualFile, name: String): VirtualFile {
        return createFile(dir, name)
    }

    override fun createChildDirectory(requestor: Any, dir: VirtualFile, dirName: String): VirtualFile {
        return createFile(dir, dirName)
    }

    override fun copyFile(requestor: Any, virtualFile: VirtualFile, newParent: VirtualFile, copyName: String): VirtualFile {
        return createFile(newParent, copyName)
    }

    override fun isReadOnly(): Boolean {
        return false
    }

    fun addFile(parent: JspVirtualFile, file: JspVirtualFile): JspVirtualFile {
        file.setParent(parent)
        parent.addChild(file)
        return addFile(file)
    }

    fun addFile(file: JspVirtualFile): JspVirtualFile {
        if (!files.containsKey(file.path)) {
            files[file.path] = file
        }
        return file
    }

    fun getModuleFile(projectName: String, moduleName: String): JspVirtualFile {
        var projectFile = findFileByPath(projectName) as JspVirtualFile?
        if (projectFile == null) {
            projectFile = JspVirtualFile(projectName)
            addFile(projectFile)
        }
        var moduleFile = findFileByPath(moduleName) as JspVirtualFile?
        if (moduleFile == null) {
            moduleFile = JspVirtualFile(moduleName)
        }
        addFile(projectFile, moduleFile)
        return moduleFile
    }
}