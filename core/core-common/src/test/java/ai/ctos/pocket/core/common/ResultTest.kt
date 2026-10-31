package ai.ctos.pocket.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class ResultTest {
    @Test
    fun `success result returns value`() {
        val result = resultOf { 3 }
        assertEquals(DomainResult.Success(3), result)
    }

    @Test
    fun `failure result captures domain error`() {
        val result = resultOf { throw IllegalStateException("boom") }
        assertEquals("unexpected_error", (result as DomainResult.Failure).error.code)
    }
}
