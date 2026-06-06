package com.hm.picplz.data.source

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.hm.picplz.common.error.AppError
import com.hm.picplz.common.result.AppResult
import com.hm.picplz.common.result.runCatchingAppError
import com.hm.picplz.data.api.AuthApi
import com.hm.picplz.data.model.KaKaoLoginRequest
import com.hm.picplz.data.model.KaKaoLoginResponseDto
import com.hm.picplz.data.util.toHttpAppError
import com.hm.picplz.domain.model.KaKaoLoginResponse
import javax.inject.Inject

interface AuthSource {
    suspend fun loginWithKaKao(accessToken: String): AppResult<KaKaoLoginResponse>
}

class AuthSourceImpl
    @Inject
    constructor(
        private val authApi: AuthApi,
    ) : AuthSource {
        private val gson = Gson()

        override suspend fun loginWithKaKao(accessToken: String): AppResult<KaKaoLoginResponse> {
            return runCatchingAppError {
                val response = authApi.loginWithKaKao(KaKaoLoginRequest(accessToken))
                if (response.isSuccessful) {
                    val rawBody =
                        response.body()?.string()
                            ?: throw AppError.Network.EmptyBody
                    val body = parseLoginResponse(rawBody)
                    Log.d(
                        TAG,
                        "kakao login response: registered=${body.registered}, " +
                            "hasSocialCode=${body.socialCode != null}, " +
                            "hasSocialEmail=${body.socialEmail != null}, " +
                            "hasSocialProvider=${body.socialProvider != null}, " +
                            "hasAccessToken=${body.token?.accessToken != null}",
                    )
                    body.toDomain()
                } else {
                    throw response.toHttpAppError()
                }
            }
        }

        private fun parseLoginResponse(rawBody: String): KaKaoLoginResponseDto {
            val root = JsonParser().parse(rawBody).asJsonObject
            val payload = root.loginResponsePayload()
            Log.d(
                TAG,
                "kakao login response shape: rootKeys=${root.keySet().sorted()}, " +
                    "payloadKeys=${payload.keySet().sorted()}, " +
                    "tokenKeys=${payload.getAsJsonObjectOrNull(LOGIN_RESPONSE_TOKEN)?.keySet()?.sorted().orEmpty()}",
            )
            return gson.fromJson(payload.toString(), KaKaoLoginResponseDto::class.java)
        }

        companion object {
            private const val TAG = "AuthSource"
        }
    }

private fun JsonObject.loginResponsePayload(): JsonObject {
    if (has(LOGIN_RESPONSE_REGISTERED) || has(LOGIN_RESPONSE_TOKEN) || has(LOGIN_RESPONSE_SOCIAL_CODE)) {
        return this
    }

    return listOf("data", "result", "response")
        .firstNotNullOfOrNull { key -> getAsJsonObjectOrNull(key) }
        ?: this
}

private fun JsonObject.getAsJsonObjectOrNull(key: String): JsonObject? =
    get(key)?.takeIf { it.isJsonObject }?.asJsonObject

private const val LOGIN_RESPONSE_REGISTERED = "registered"
private const val LOGIN_RESPONSE_TOKEN = "token"
private const val LOGIN_RESPONSE_SOCIAL_CODE = "socialCode"
