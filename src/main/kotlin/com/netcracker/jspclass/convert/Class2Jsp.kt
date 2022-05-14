package com.netcracker.jspclass.convert

import java.util.regex.Pattern

/**
 * SEBY0408
 */
class Class2Jsp {
    companion object {
        private val classStart = Replacement(
            Pattern.compile("(?m)\\s*(((private|public|static|final)\\s*?)*class\\s*.+\\s*\\{)"), "\n<%!\n$1"
        )

        private val initPage = Replacement(
            Pattern.compile("(?s)\r*\n* */\\*\\*init\\*\\* *(.*?) *\\*/"), "%>\n<%$1%>"
        )

        private val importPage = Replacement(
            Pattern.compile("import (.+?);"), "<%@ page import=\"$1\" %>"
        )

        private val removePackage = Replacement(
            Pattern.compile("^\\s*package (.+?);"), ""
        )

        private val comment = Replacement(
            Pattern.compile("(?s)\r*\n* */\\*\\*comment\\*\\*(.*?) *\\*/"), "<%--$1--%>"
        )

        private val other = Replacement(
            Pattern.compile("(?m)/\\*\\*other\\*\\*(.*)\\*/"), "$1"
        )
    }

    fun transform(string: String): String {
        var result = string
        result = classStart.replaceFirst(result)
        result = initPage.replaceAll(result)
        if (!result.contains("%>")) {
            result = "$result%>"
        }
        result = importPage.replaceAll(result)
        result = removePackage.replaceAll(result)
        result = other.replaceAll(result)
        result = comment.replaceAll(result)
        return result
    }
}