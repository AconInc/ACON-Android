package com.acon.acon.data.error

import com.acon.acon.domain.error.RootError
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

internal inline fun <R> runCatchingWith(
    vararg definedErrors: RootError,
    block: () -> R
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: RemoteError) {
        Timber.d("RemoteError: $e")
        definedErrors.find { definedError ->
            e.errorCode == definedError.code
        }?.let { Result.failure(it) } ?: Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Timber.d("Throwable: $e")
        Result.failure(e)
    }
}