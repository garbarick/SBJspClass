package com.netcracker.jspclass.convert

import com.netcracker.jspclass.Constants

/**
 * SEBY0408
 */
class Class2Jsp {
    companion object {
        private val classStart = Replacement(
            "(((private|public|static|final)\\s*?)*class\\s*.+)", "<%!" + Constants.NEW_LINE + "$1"
        )

        private val initPage = Replacement(
            "/\\*\\*init\\*\\*\\s*", "<%"
        )

        private val importPage = Replacement(
            "import\\s*(.+?);", "<%@ page import=\"$1\" %>"
        )

        private val describePackage = Replacement(
            "package\\s*(.+?);", ""
        )

        private val commentOpen = Replacement(
            "/\\*\\*", ""
        )

        private val initPage2 = Replacement(
            "\\s*\\*\\s*init\\*\\*", "<%"
        )

        private val commentProgress = Replacement(
            "\\s*\\*\\s+(.*)", "$1"
        )

        private val commentClose = Replacement(
            "\\s*\\*/", ""
        )
    }

    private val result = StringBuffer()
    private var openClass = false
    private var openComment = false

    fun transform(string: String): String {
        string.lines().forEach { row ->
            if (importPage.matches(row)) {
                addNewLine()
                result.append(importPage.replaceAll(row))
            } else if (describePackage.matches(row)) {
                return@forEach
            } else if (classStart.matches(row)) {
                addNewLine()
                closeClassTag(true)
                result.append(classStart.replaceAll(row))
                openClass = true
            } else if (initPage.matches(row)) {
                addNewLine()
                closeClassTag(true)
                result.append(initPage.replaceAll(row))
                openClass = true
            } else if (commentOpen.matches(row)) {
                openComment = true
                return@forEach
            } else if (initPage2.matches(row) && openComment) {
                addNewLine()
                closeClassTag(true)
                result.append(initPage2.replaceAll(row))
                openClass = true
            } else if (commentProgress.matches(row) && openComment) {
                addNewLine()
                result.append(commentProgress.replaceAll(row))
            } else if (commentClose.matches(row)) {
                openComment = false
                return@forEach
            } else if (openClass || openComment) {
                addNewLine()
                result.append(row)
            }
        }
        closeClassTag(false)
        return result.toString()
    }

    private fun closeClassTag(addNewLine: Boolean) {
        if (openClass) {
            while (result.endsWith('\n') || result.endsWith('\r')) {
                result.setLength(result.length - 1)
            }
            addNewLine()
            result.append("%>")
            if (addNewLine) {
                addNewLine()
            }
        }
    }

    private fun addNewLine() {
        if (result.isNotEmpty()) {
            result.append(Constants.NEW_LINE)
        }
    }
}