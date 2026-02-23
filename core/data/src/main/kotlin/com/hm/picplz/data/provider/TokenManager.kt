package com.hm.picplz.data.provider

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val configProvider: ConfigProvider,
    ) {
        private val prefs: SharedPreferences =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE,
            )

        companion object {
            private const val PREF_NAME = "picplz_auth"
            private const val KEY_ACCESS_TOKEN = "access_token"
            private const val KEY_REFRESH_TOKEN = "refresh_token"
            private const val KEY_USER_TYPE = "user_type"
            private const val KEY_SOCIAL_CODE = "social_code"
            private const val KEY_SOCIAL_EMAIL = "social_email"
            private const val KEY_SOCIAL_PROVIDER = "social_provider"
        }

        enum class AuthRole(val value: String) {
            GUEST("ROLE_GUEST"),
            USER("ROLE_USER"),
        }

        private fun saveToken(
            token: String,
            authRole: AuthRole,
        ) {
            prefs.edit()
                .putString(KEY_ACCESS_TOKEN, token)
                .putString(KEY_USER_TYPE, authRole.value)
                .apply()
        }

        fun getToken(): String? {
            return prefs.getString(KEY_ACCESS_TOKEN, null)
        }

        fun getAuthRole(): AuthRole? {
            val value = prefs.getString(KEY_USER_TYPE, null)
            return AuthRole.entries.find { it.value == value }
        }

        fun saveLoginToken(
            accessToken: String,
            refreshToken: String? = null,
        ) {
            prefs.edit()
                .putString(KEY_ACCESS_TOKEN, accessToken)
                .putString(KEY_USER_TYPE, AuthRole.USER.value)
                .apply {
                    refreshToken?.let { putString(KEY_REFRESH_TOKEN, it) }
                }
                .apply()
        }

        fun saveSocialInfo(
            socialCode: String?,
            socialEmail: String?,
            socialProvider: String?,
        ) {
            prefs.edit()
                .putString(KEY_SOCIAL_CODE, socialCode)
                .putString(KEY_SOCIAL_EMAIL, socialEmail)
                .putString(KEY_SOCIAL_PROVIDER, socialProvider)
                .apply()
        }

        fun getSocialCode(): String? = prefs.getString(KEY_SOCIAL_CODE, null)

        fun getSocialEmail(): String? = prefs.getString(KEY_SOCIAL_EMAIL, null)

        fun getSocialProvider(): String? = prefs.getString(KEY_SOCIAL_PROVIDER, null)

        fun clearSocialInfo() {
            prefs.edit()
                .remove(KEY_SOCIAL_CODE)
                .remove(KEY_SOCIAL_EMAIL)
                .remove(KEY_SOCIAL_PROVIDER)
                .apply()
        }

        fun clearToken() {
            prefs.edit()
                .remove(KEY_ACCESS_TOKEN)
                .remove(KEY_REFRESH_TOKEN)
                .remove(KEY_USER_TYPE)
                .apply()
        }

        fun isLoggedIn(): Boolean {
            return getToken() != null
        }

        fun setDevelopmentTokens() {
            saveToken(configProvider.devUserToken, AuthRole.USER)
        }

        fun switchToGuestToken() {
            saveToken(configProvider.devGuestToken, AuthRole.GUEST)
        }
    }
