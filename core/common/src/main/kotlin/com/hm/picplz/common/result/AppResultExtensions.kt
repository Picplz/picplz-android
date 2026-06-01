package com.hm.picplz.common.result

import com.hm.picplz.common.error.AppError
import kotlin.coroutines.cancellation.CancellationException

inline fun <T> runCatchingAppError(block: () -> T): AppResult<T> =
    try {
        Result.success(block())
    } catch (error: CancellationException) {
        throw error
    } catch (error: Throwable) {
        Result.failure(AppError.fromThrowable(error))
    }
