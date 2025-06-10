package com.hm.picplz

import android.app.Application
import android.util.Log
import com.hm.picplz.data.provider.TokenManager
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate() {
        super.onCreate()
        try {
            KakaoMapSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
            tokenManager.setDevelopmentTokens()
        } catch (e: Exception) {
            Log.e("KakaoMapSdk", "카카오맵 SDK init 실패", e)
        }
    }
}