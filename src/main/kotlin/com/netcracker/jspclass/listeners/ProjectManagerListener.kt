package com.netcracker.jspclass.listeners

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManagerListener
import com.intellij.openapi.vfs.VirtualFileManager
import com.netcracker.jspclass.Constants
import com.netcracker.jspclass.vfs.JspVirtualFile
import com.netcracker.jspclass.vfs.JspVirtualFileSystem

/**
 * SEBY0408
 */
class ProjectManagerListener: ProjectManagerListener {
    override fun projectOpened(project: Project) {
        val fileSystem = VirtualFileManager.getInstance().getFileSystem(Constants.PROTOCOL) as JspVirtualFileSystem
        var projectFile = fileSystem.findFileByPath(project.name) as JspVirtualFile?
        if (projectFile == null) {
            projectFile = JspVirtualFile(project.name)
            fileSystem.addFile(projectFile)
        }
    }
}