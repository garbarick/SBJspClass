package com.netcracker.jspclass.convert

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import java.io.File

/**
 * SEBY0408
 */
class Class2JspTest : BasePlatformTestCase() {
    private val source = "src/test/resources/${javaClass.name.replace(".", "/")}"

    private fun test(fileName: String) {
        val jspContent = File(source, "${fileName}.txt").readText()
        val expected = File(source, "${fileName}.jsp").readText()
        val actual = Class2Jsp().transform(jspContent)
        if (expected != actual) {
            File(source, "${fileName}Actual.jsp").writeText(actual)
        }
        assertEquals(expected, actual)
    }

    fun testTransform1() {
        test("Test1")
    }

    fun testTransform2() {
        test("Test2")
    }

    fun testTransform3() {
        test("Test3")
    }
}