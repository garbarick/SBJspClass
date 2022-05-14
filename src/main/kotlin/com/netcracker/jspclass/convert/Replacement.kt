package com.netcracker.jspclass.convert

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * SEBY0408
 */
class Replacement(private val pattern: Pattern, private val replace: String) {
    fun replaceFirst(string: String): String {
        return pattern.matcher(string).replaceFirst(replace)
    }

    fun replaceAll(string: String): String {
        return pattern.matcher(string).replaceAll(replace)
    }

    fun find(string: String): Boolean {
        return pattern.matcher(string).find()
    }

    fun group(string: String, i: Int): String? {
        val matcher: Matcher = pattern.matcher(string)
        return if (matcher.find()) {
            matcher.group(i)
        } else null
    }

    override fun toString(): String {
        return "Replacement{pattern=$pattern, replace='$replace}"
    }
}