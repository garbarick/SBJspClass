package com.netcracker.jspclass.util

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.module.ModuleUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.*
import com.intellij.openapi.vfs.VirtualFile
import com.netcracker.jspclass.vfs.JspVirtualFile


/**
 * SEBY0408
 */
class Helper(private val project: Project) {

    private fun addToSource(index: ProjectFileIndex, file: JspVirtualFile) {
        val isInContent = index.isInContent(file.parent!!)
        if (!isInContent && file.source != null) {
            val module = ModuleUtil.findModuleForFile(file.source!!, project)
            if (module != null) {
                val model = ModuleRootManager.getInstance(module).modifiableModel
                val parentFile: VirtualFile? = file.parent
                var entry = findContentEntry(model, parentFile)
                if (entry == null) {
                    entry = model.addContentEntry(parentFile!!)
                }
                for (sourceFolder in entry.sourceFolders) {
                    if (sourceFolder.file == parentFile) {
                        entry.removeSourceFolder(sourceFolder)
                    }
                }
                entry.addSourceFolder(parentFile!!, false)
                model.commit()
            }
        }
    }

    fun addToSource(file: JspVirtualFile) {
        val index = getContent(file)
        if (index != null) {
            ApplicationManager.getApplication().runWriteAction {
                if (inOtherContent(index, file)) {
                    removeFromContent(file.parent)
                }
                addToSource(index, file)
            }
        }
    }

    private fun removeFromContent(file: JspVirtualFile?) {
        if (file != null) {
            val module = ModuleUtil.findModuleForFile(file, project)
            if (module != null) {
                val model = ModuleRootManager.getInstance(module).modifiableModel
                val entry = findContentEntry(model, file)
                if (entry != null) {
                    model.removeContentEntry(entry)
                    model.commit()
                }
            }
        }
    }

    private fun inOtherContent(index: ProjectFileIndex?, file: JspVirtualFile): Boolean {
        if (index != null && index.isInContent(file.parent!!) && file.source != null) {
            val moduleForSource = ModuleUtil.findModuleForFile(file.source!!, project)
            val moduleForParent = ModuleUtil.findModuleForFile(file.parent!!, project)
            return moduleForSource != moduleForParent
        }
        return false
    }

    private fun getContent(file: JspVirtualFile): ProjectFileIndex? {
        val parent = file.parent
        val source = file.source
        return if (parent != null && source != null) {
            ProjectRootManager.getInstance(project).fileIndex
        } else null
    }

    private fun findContentEntry(model: ModuleRootModel, file: VirtualFile?): ContentEntry? {
        for (entry in model.contentEntries) {
            if (file == entry.file) {
                return entry
            }
        }
        return null
    }
}