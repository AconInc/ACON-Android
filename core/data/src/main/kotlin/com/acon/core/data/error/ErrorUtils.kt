package com.acon.core.data.error

import com.acon.acon.domain.error.RootError
import timber.log.Timber
import kotlin.coroutines.cancellation.CancellationException

internal inline fun <T: RootError, R> runCatchingWith(
    errorType: T,
    block: () -> R
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: RemoteError) {
        Timber.e(e)
        errorType.createErrorInstances().find { definedError ->
            e.errorCode == definedError.code
        }?.let { Result.failure(it) } ?: Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Timber.e(e)
        Result.failure(e)
    }
}

internal inline fun <R> runCatchingWith(
    block: () -> R
): Result<R> {
    return try {
        Result.success(block())
    } catch (e: RemoteError) {
        Timber.e(e)
        Result.failure(e)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Timber.e(e)
        Result.failure(e)
    }
}