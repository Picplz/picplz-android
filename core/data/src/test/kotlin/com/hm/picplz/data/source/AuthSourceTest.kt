package com.hm.picplz.data.source

import android.util.Log
import com.hm.picplz.common.error.AppError
import com.hm.picplz.data.api.AuthApi
import com.hm.picplz.data.model.KaKaoLoginRequest
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.MockedStatic
import org.mockito.Mockito
import retrofit2.Response

class AuthSourceTest {
    private lateinit var mockedLog: MockedStatic<Log>

    @Before
    fun setUp() {
        mockedLog = Mockito.mockStatic(Log::class.java)
        mockedLog.`when`<Int> { Log.d(anyString(), anyString()) }.thenReturn(0)
        mockedLog
            .`when`<Int> { Log.d(anyString(), anyString(), any(Throwable::class.java)) }
            .thenReturn(0)
    }

    @After
    fun tearDown() {
        mockedLog.close()
    }

    @Test
    fun `login parses direct kakao login response body`() =
        runTest {
            val authSource =
                AuthSourceImpl(
                    FakeAuthApi(
                        Response.success(loginResponseBody(directLoginResponseJson())),
                    ),
                )

            val response = authSource.loginWithKaKao("KAKAO_ACCESS_TOKEN").getOrThrow()

            assertEquals("SOCIAL_CODE", response.socialCode)
            assertEquals("user@example.com", response.socialEmail)
            assertEquals("KAKAO", response.socialProvider)
            assertEquals("ACCESS_TOKEN", response.accessToken)
            assertEquals("REFRESH_TOKEN", response.refreshToken)
            assertEquals(true, response.registered)
        }

    @Test
    fun `login unwraps data wrapper response body`() =
        runTest {
            val authSource =
                AuthSourceImpl(
                    FakeAuthApi(
                        Response.success(
                            loginResponseBody(
                                """
                                {
                                  "statusCode": 200,
                                  "message": "success",
                                  "data": ${directLoginResponseJson()}
                                }
                                """.trimIndent(),
                            ),
                        ),
                    ),
                )

            val response = authSource.loginWithKaKao("KAKAO_ACCESS_TOKEN").getOrThrow()

            assertEquals("SOCIAL_CODE", response.socialCode)
            assertEquals("ACCESS_TOKEN", response.accessToken)
            assertEquals(true, response.registered)
        }

    @Test
    fun `login unwraps result wrapper response body`() =
        runTest {
            val authSource =
                AuthSourceImpl(
                    FakeAuthApi(
                        Response.success(
                            loginResponseBody(
                                """
                                {
                                  "result": ${directLoginResponseJson()}
                                }
                                """.trimIndent(),
                            ),
                        ),
                    ),
                )

            val response = authSource.loginWithKaKao("KAKAO_ACCESS_TOKEN").getOrThrow()

            assertEquals("SOCIAL_CODE", response.socialCode)
            assertEquals("ACCESS_TOKEN", response.accessToken)
        }

    @Test
    fun `login unwraps response wrapper response body`() =
        runTest {
            val authSource =
                AuthSourceImpl(
                    FakeAuthApi(
                        Response.success(
                            loginResponseBody(
                                """
                                {
                                  "response": ${directLoginResponseJson()}
                                }
                                """.trimIndent(),
                            ),
                        ),
                    ),
                )

            val response = authSource.loginWithKaKao("KAKAO_ACCESS_TOKEN").getOrThrow()

            assertEquals("SOCIAL_CODE", response.socialCode)
            assertEquals("ACCESS_TOKEN", response.accessToken)
        }

    @Test
    fun `login returns http app error on failed response`() =
        runTest {
            val authSource =
                AuthSourceImpl(
                    FakeAuthApi(
                        Response.error(
                            401,
                            """
                            {
                              "statusCode": 401,
                              "message": "invalid token",
                              "timestamp": "2026-06-06T00:00:00"
                            }
                            """.trimIndent().toResponseBody("application/json".toMediaType()),
                        ),
                    ),
                )

            val error = authSource.loginWithKaKao("BAD_TOKEN").exceptionOrNull()

            assertTrue(error is AppError.Network.Http)
            assertEquals(401, (error as AppError.Network.Http).code)
            assertEquals("invalid token", error.serverMessage)
        }

    private class FakeAuthApi(
        private val response: Response<ResponseBody>,
    ) : AuthApi {
        var lastRequest: KaKaoLoginRequest? = null

        override suspend fun loginWithKaKao(request: KaKaoLoginRequest): Response<ResponseBody> {
            lastRequest = request
            return response
        }
    }

    private fun loginResponseBody(json: String): ResponseBody = json.toResponseBody("application/json".toMediaType())

    private fun directLoginResponseJson(): String =
        """
        {
          "socialCode": "SOCIAL_CODE",
          "socialEmail": "user@example.com",
          "socialProvider": "KAKAO",
          "token": {
            "grantType": "Bearer",
            "accessToken": "ACCESS_TOKEN",
            "refreshToken": "REFRESH_TOKEN",
            "accessTokenExpires": 3600,
            "accessTokenExpiresDate": "2026-06-06T00:00:00"
          },
          "registered": true
        }
        """.trimIndent()
}
