package com.netcracker.jspclass.vfs

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFileSystem
import com.intellij.openapi.vfs.VirtualFileWithId
import com.netcracker.jspclass.Constants
import com.netcracker.jspclass.convert.Jsp2Class
import com.netcracker.jspclass.convert.StringOutputStream
import java.io.*


/**
 * SEBY0408
 */
class JspVirtualFile(private val name: String) : VirtualFile(), VirtualFileWithId {
    private val id = JspVirtualFileSystem.IDS.getAndIncrement()
    private var path: String = name
    var source: VirtualFile? = null
    private var content: String? = null
    private var children = HashMap<String, VirtualFile>()
    private var parent: JspVirtualFile? = null
    var toggle = false
    private var modifStamp: Long = 0
    private var timeStamp: Long = 0

    constructor(source: VirtualFile) : this(source.nameWithoutExtension + "." + Constants.JAVA) {
        this.source = source
        path = source.path
        modifStamp = source.modificationStamp
        timeStamp = source.timeStamp
    }

    override fun getName(): String {
        return name
    }

    override fun getFileSystem(): VirtualFileSystem {
        return VirtualFileManager.getInstance().getFileSystem(Constants.PROTOCOL)
    }

    override fun getPath(): String {
        if (!isDirectory) {
            return path
        }
        if (parent == null) {
            return name
        }
        return parent?.getPath() + File.separator + name;
    }

    override fun getUrl(): String {
        return VirtualFileManager.constructUrl(fileSystem.protocol, getPath())
    }

    override fun isWritable(): Boolean {
        return source == null || source!!.isWritable
    }

    override fun isDirectory(): Boolean {
        return source == null
    }

    override fun isValid(): Boolean {
        return true
    }

    override fun getParent(): JspVirtualFile? {
        return parent
    }

    fun setParent(parent: VirtualFile?) {
        this.parent = parent as JspVirtualFile?
    }

    fun addChild(file: VirtualFile) {
        if (file is JspVirtualFile) {
            children[file.path] = file
        }
    }

    override fun getChildren(): Array<VirtualFile> {
        return children.values.toTypedArray()
    }

    fun deleteChild(file: VirtualFile) {
        if (file is JspVirtualFile) {
            children.remove(file.path)
        }
    }

    override fun getOutputStream(requestor: Any?, newModificationStamp: Long, newTimeStamp: Long): OutputStream {
        this.timeStamp = newTimeStamp
        this.modifStamp = newModificationStamp
        return StringOutputStream(source!!.getOutputStream(requestor, modifStamp, timeStamp))
    }

    override fun contentsToByteArray(): ByteArray {
        refreshContent()
        return if (content != null) content!!.toByteArray() else ByteArray(0)
    }

    fun refreshContent() {
        if (source == null) {
            return
        }
        content = try {
            Jsp2Class().transform(String(source!!.contentsToByteArray()))
        } catch (e: IOException) {
            thisLogger().error("refreshContent error:", e)
            throw RuntimeException(e)
        }
    }

    override fun getTimeStamp(): Long {
        return timeStamp
    }

    override fun getModificationStamp(): Long {
        return modifStamp
    }

    override fun getLength(): Long {
        return if (content != null) content!!.toByteArray().size.toLong() else 0
    }

    override fun refresh(asynchronous: Boolean, recursive: Boolean, postRunnable: Runnable?) {
        if (source == null) {
            return
        }
        source!!.refresh(asynchronous, recursive, postRunnable)
    }

    override fun getInputStream(): InputStream {
        return ByteArrayInputStream(contentsToByteArray())
    }

    override fun getId(): Int {
        return id.toInt()
    }

    override fun toString(): String {
        return "JspVirtualFile{name='$name\', id=$id}"
    }
}