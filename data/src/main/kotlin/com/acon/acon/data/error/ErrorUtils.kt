package com.acon.acon.data.error

import com.acon.acon.domain.error.RootError
import kotlin.coroutines.cancellation.CancellationException

internal inline fun <R> runCatchingWith(
    vararg definedErrors: RootError,
    block: () -> R
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: RemoteError) {
        definedErrors.find { definedError ->
            e.errorCode == definedError.code
        }?.let { Result.failure(it) } ?: Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}