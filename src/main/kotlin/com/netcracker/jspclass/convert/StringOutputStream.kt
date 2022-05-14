package com.netcracker.jspclass.convert

import java.io.IOException
import java.io.OutputStream

/**
 * SEBY0408
 */
class StringOutputStream(private val out: OutputStream): OutputStream() {
    private val string = StringBuilder()
    
    override fun write(b: Int) {
        string.append(b.toChar())
    }

    fun getString(): String {
        return string.toString()
    }

    @Throws(IOException::class)
    override fun close() {
        super.close()
        out.write(Class2Jsp().transform(getString()).toByteArray())
        out.close()
    }
}