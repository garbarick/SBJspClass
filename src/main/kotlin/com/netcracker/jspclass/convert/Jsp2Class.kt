package com.netcracker.jspclass.convert

import java.util.regex.Pattern

/**
 * SEBY0408
 */
class Jsp2Class {
    companion object {
        private val comment = Replacement(
            Pattern.compile("(?s)<%--(.*?)--%>"), "/**comment**$1*/"
        )

        private val initClass = Replacement(
            Pattern.compile("(?s)<%!(.*?)%>"), "$1"
        )

        private val initPage = Replacement(
            Pattern.compile("(?s)<%([^@!].*?)%>"), "/**init** $1*/"
        )

        private val noImportPage = Replacement(
            Pattern.compile("(?m)<%@\\s*page\\s*([^\\s]*)\\s*=\\s*\"\\s*([^\\s]+)\\s*\"\\s*(?!%>)(.*)"), "<%@ page $1=\"$2\"%>\n<%@ page $3"
        )

        private val multiplePageImport = Replacement(
            Pattern.compile("(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*([^\\s]+)\\s*,\\s*(.*)"), "import $1;\n<%@ page import=\"$2"
        )

        private val multipleImportInPage = Replacement(
            Pattern.compile("(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*([^\\s]+)\\s*\"(?!%>)(.*)"), "import $1;\n<%@ page $2"
        )

        private val singlePageImport = Replacement(
            Pattern.compile("(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*([^\\s]+)\\s*\"\\s*%>"), "import $1;"
        )

        private val emptyPageImport = Replacement(
            Pattern.compile("(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*\"\\s*%>"), ""
        )

        private val emptyPage = Replacement(
            Pattern.compile("(?m)<%@\\s*page\\s*%>"), ""
        )

        private val other = Replacement(
            Pattern.compile("(?m)(<%.*%>)"), "/**other**$1*/"
        )
    }

    fun transform(string: String): String {
        var result = string
        val maxIterate = 1000
        result = comment.replaceAll(result)
        result = initClass.replaceAll(result)
        result = initPage.replaceAll(result)
        result = singlePageImport.replaceAll(result)
        var iter = 0
        while (noImportPage.find(result) && iter < maxIterate) {
            result = noImportPage.replaceAll(result)
            iter++
        }
        result = singlePageImport.replaceAll(result)
        iter = 0
        while (multiplePageImport.find(result) && iter < maxIterate) {
            result = multiplePageImport.replaceAll(result)
            iter++
        }
        result = singlePageImport.replaceAll(result)
        iter = 0
        while (multipleImportInPage.find(result) && iter < maxIterate) {
            result = multipleImportInPage.replaceAll(result)
            iter++
        }
        result = singlePageImport.replaceAll(result)
        result = emptyPageImport.replaceAll(result)
        result = emptyPage.replaceAll(result)
        result = other.replaceAll(result)
        return result
    }
}