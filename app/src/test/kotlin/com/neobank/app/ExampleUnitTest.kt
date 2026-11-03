package com.neobank.app

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testPlatform() {
        val platform = getPlatform()
        assert(platform.name.startsWith("Android"))
    }
}
