package com.hm.picplz.data.util

import retrofit2.Response

/**
 * API 호출 실패 시 발생하는 구조화된 예외.
 * HTTP 상태 코드와 에러 바디를 보존하여 호출부에서 에러 유형별 분기 가능.
 */
class ApiException(
    val code: Int,
    val errorBody: String?,
) : Exception("API failed: $code $errorBody")

/**
 * Retrofit Response를 안전하게 처리하여 Result<T>로 반환.
 *
 * 사용 예시:
 * ```
 * safeApiCall { api.getPhotographers(lat, lng, dist) }
 * ```
 */
suspend fun <T> safeApiCall(call: suspend () -> Response<T>): Result<T> =
    runCatching {
        val response = call()
        if (response.isSuccessful) {
            response.body() ?: error("Response body is null")
        } else {
            throw ApiException(response.code(), response.errorBody()?.string())
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
): Result<R> =
    runCatching {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body() ?: error("Response body is null")
            transform(body)
        } else {
            throw ApiException(response.code(), response.errorBody()?.string())
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
suspend fun safeApiCallUnit(call: suspend () -> Response<*>): Result<Unit> =
    runCatching {
        val response = call()
        if (!response.isSuccessful) {
            throw ApiException(response.code(), response.errorBody()?.string())
        }
    }
