package com.pritom.common

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*

/**
 * Testing Result class
 */
class ResultTest {

    @Test
    fun result_catches_errors() = runTest {
        flow {
            emit(1)
            throw Exception("Test Done")
        }
            .asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())
//                val result = awaitItem()
//                assertTrue(result is Result.Error)
//                assertEquals("Test Done", (result as Result.Error).throwable.message)

                when (val errorResult = awaitItem()) {
                    is Result.Error -> assertEquals(
                        "Test Done",
                        errorResult.throwable.message,
                    )

                    Result.Loading,
                    is Result.Success,
                        -> throw IllegalStateException(
                        "The flow should have emitted an Error Result",
                    )
                }

                awaitComplete()
            }
    }

    @Test
    fun `emits loading state first`() = runTest {
        flowOf(1)
            .asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
    }

    @Test
    fun `emits success with data when flow completes`() = runTest {
        flowOf(1, 2, 3)
            .asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())
                assertEquals(Result.Success(2), awaitItem())
                assertEquals(Result.Success(3), awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun `emits error and stops after exception`() = runTest {
        flow {
            emit(1)
            throw RuntimeException("Failed")
            emit(2) // This should never be reached
        }
            .asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())
                assertTrue(awaitItem() is Result.Error)
                awaitComplete()
            }
    }
}