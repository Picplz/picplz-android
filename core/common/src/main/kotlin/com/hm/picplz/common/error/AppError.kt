package com.hm.picplz.common.error

import java.io.IOException
import java.util.concurrent.TimeoutException

sealed class AppError(
    message: String? = null,
    cause: Throwable? = null,
) : Exception(message, cause) {
    sealed class Network(
        message: String? = null,
        cause: Throwable? = null,
    ) : AppError(message, cause) {
        data object NoConnection : Network("No network connection")

        data object Timeout : Network("Network timeout")

        data class Http(
            val code: Int,
            val body: String?,
            val statusCode: Int? = null,
            val serverMessage: String? = null,
            val timestamp: String? = null,
            val data: String? = null,
        ) : Network(serverMessage ?: "HTTP $code $body")

        data object EmptyBody : Network("Response body is null")

        data class Unknown(
            val original: Throwable?,
        ) : Network(original?.message, original)
    }

    sealed class Auth(
        message: String? = null,
        cause: Throwable? = null,
    ) : AppError(message, cause) {
        data object KakaoTokenMissing : Auth("Kakao token is null")

        data object SocialInfoMissing : Auth("Social login info is missing")

        data class KakaoSdk(
            val original: Throwable?,
        ) : Auth(original?.message, original)
    }

    sealed class Storage(
        message: String? = null,
        cause: Throwable? = null,
    ) : AppError(message, cause) {
        data class UploadFailed(
            val original: Throwable?,
        ) : Storage(original?.message, original)
    }

    sealed class Location(
        message: String? = null,
        cause: Throwable? = null,
    ) : AppError(message, cause) {
        data object PermissionDenied : Location("Location permission denied")

        data class LookupFailed(
            val original: Throwable?,
        ) : Location(original?.message, original)
    }

    data class Unknown(
        val original: Throwable?,
    ) : AppError(original?.message, original)

    companion object {
        fun fromThrowable(throwable: Throwable): AppError =
            when (throwable) {
                is AppError -> throwable
                is java.net.SocketTimeoutException,
                is TimeoutException,
                -> Network.Timeout
                is IOException -> Network.NoConnection
                else -> Unknown(throwable)
            }
    }
}
