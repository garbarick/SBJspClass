package com.netcracker.jspclass.convert

import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * SEBY0408
 */
class Replacement(private val pattern: Pattern, private val replace: String) {
    constructor(pattern: String, replace: String) : this(Pattern.compile(pattern), replace)

    fun matcher(string: String): Matcher {
        return pattern.matcher(string)
    }

    fun replaceFirst(string: String): String {
        return matcher(string).replaceFirst(replace)
    }

    fun replaceAll(string: String): String {
        return matcher(string).replaceAll(replace)
    }

    fun find(string: String): Boolean {
        return matcher(string).find()
    }

    fun group(string: String, i: Int): String? {
        val matcher = matcher(string)
        return if (matcher.find()) {
            matcher.group(i)
        } else null
    }

    fun matches(string: String): Boolean {
        return matcher(string).matches()
    }

    override fun toString(): String {
        return "Replacement{pattern=$pattern, replace='$replace}"
    }
}