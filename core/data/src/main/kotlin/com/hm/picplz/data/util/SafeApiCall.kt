package com.hm.picplz.data.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.model.ApiErrorResponseDto
import retrofit2.Response

private val errorResponseGson = Gson()
private const val TAG = "SafeApiCall"

/**
 * Retrofit Response를 안전하게 처리하여 AppResult<T>로 반환.
 *
 * 사용 예시:
 * ```
 * safeApiCall { api.getPhotographers(lat, lng, dist) }
 * ```
 */
suspend fun <T> safeApiCall(call: suspend () -> Response<T>): AppResult<T> =
    runCatchingAppError {
        val response = call()
        if (response.isSuccessful) {
            response.body() ?: throw AppError.Network.EmptyBody
        } else {
            throw response.toHttpAppError()
        }
    }

/**
 * 응답 body를 변환하는 오버로드.
 * toDomain() 매핑이나 body 후처리가 필요한 경우 사용.
 *
 * 사용 예시:
 * ```
 * safeApiCall({ api.login(request) }) { it.toDomain() }
 * ```
 */
suspend fun <T, R> safeApiCall(
    call: suspend () -> Response<T>,
    transform: (T) -> R,
): AppResult<R> =
    runCatchingAppError {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body() ?: throw AppError.Network.EmptyBody
            transform(body)
        } else {
            throw response.toHttpAppError()
        }
    }

/**
 * 응답 body가 필요 없는 API 호출용 (create, update, delete).
 *
 * 사용 예시:
 * ```
 * safeApiCallUnit { api.createPhotographer(request) }
 * ```
 */
suspend fun safeApiCallUnit(call: suspend () -> Response<*>): AppResult<Unit> =
    runCatchingAppError {
        val response = call()
        if (!response.isSuccessful) {
            throw response.toHttpAppError()
        }
    }

fun Response<*>.toHttpAppError(): AppError.Network.Http {
    val rawBody = errorBody()?.string()
    val serverError = rawBody?.toApiErrorResponseDtoOrNull()
    return AppError.Network.Http(
        code = code(),
        body = rawBody,
        statusCode = serverError?.statusCode,
        serverMessage = serverError?.message,
        timestamp = serverError?.timestamp,
        data = serverError?.data?.toString(),
    )
}

private fun String.toApiErrorResponseDtoOrNull(): ApiErrorResponseDto? =
    try {
        errorResponseGson.fromJson(this, ApiErrorResponseDto::class.java)
    } catch (error: JsonSyntaxException) {
        Log.d(TAG, "Failed to parse API error response", error)
        null
    } catch (error: IllegalStateException) {
        Log.d(TAG, "Failed to parse API error response", error)
        null
    }
