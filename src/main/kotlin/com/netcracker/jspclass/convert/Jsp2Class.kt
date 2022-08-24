package com.netcracker.jspclass.convert

import com.netcracker.jspclass.Constants

/**
 * SEBY0408
 */
class Jsp2Class {
    companion object {
        private val initClass = Replacement(
            "(?s)<%!(.*?)%>", "$1"
        )

        private val initPage = Replacement(
            "(?s)<%([^@!].*?)%>", "/**init** $1*/"
        )

        private val noImportPage = Replacement(
            "(?m)<%@\\s*page\\s*([^\\s]*)\\s*=\\s*\"\\s*([^\\s]+)\\s*\"\\s*(?!%>)(.*)", "<%@ page $1=\"$2\"%>" + Constants.NEW_LINE + "<%@ page $3"
        )

        private val multiplePageImport = Replacement(
            "(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*([^\\s]+)\\s*,\\s*(.*)", "import $1;" + Constants.NEW_LINE + "<%@ page import=\"$2"
        )

        private val multipleImportInPage = Replacement(
            "(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*([^\\s]+)\\s*\"(?!%>)(.*)", "import $1;" + Constants.NEW_LINE + "<%@ page $2"
        )

        private val singlePageImport = Replacement(
            "(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*([^\\s]+)\\s*\"\\s*%>", "import $1;"
        )

        private val emptyPageImport = Replacement(
            "(?m)<%@\\s*page\\s*import\\s*=\\s*\"\\s*\"\\s*%>", ""
        )

        private val emptyPage = Replacement(
            "(?m)<%@\\s*page\\s*%>", ""
        )
    }

    fun transform(string: String): String {
        var result = string
        val maxIterate = 1000
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
        return result
    }
}