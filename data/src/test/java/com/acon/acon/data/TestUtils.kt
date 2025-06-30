package com.acon.acon.data

import com.acon.acon.data.error.RemoteError
import com.acon.acon.domain.error.RootError
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.params.provider.Arguments
import java.util.stream.Stream
import kotlin.reflect.KClass
import kotlin.test.assertTrue

internal fun createErrorStream(
    vararg errorPairs: Pair<Int, KClass<out RootError>> // errorCode to errorKClass
): Stream<Arguments> {
    return Stream.of(*errorPairs.map { Arguments.of(it.first, it.second) }.toTypedArray())
}

internal fun createFakeRemoteError(
    errorCode: Int,
) : RemoteError {
    return RemoteError(mockk(relaxed = true), errorCode, "")
}

internal fun assertValidErrorMapping(result: Result<Any>, expectedErrorClass: KClass<out RootError>) {
    assertTrue(result.isFailure)
    val exception = result.exceptionOrNull()
    assertInstanceOf(expectedErrorClass.java, exception, "에러 코드와 예외 클래스가 올바르게 매핑되지 않음")
}