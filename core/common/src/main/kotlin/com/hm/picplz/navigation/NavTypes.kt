package com.hm.picplz.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.hm.picplz.common.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

val UserNavType =
    object : NavType<User>(isNullableAllowed = false) {
        override fun get(
            bundle: Bundle,
            key: String,
        ): User? {
            return bundle.getString(key)?.let { Json.decodeFromString(it) }
        }

        override fun parseValue(value: String): User {
            return Json.decodeFromString(Uri.decode(value))
        }

        override fun serializeAsValue(value: User): String {
            return Uri.encode(Json.encodeToString(value))
        }

        override fun put(
            bundle: Bundle,
            key: String,
            value: User,
        ) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }

val UserTypeMap = mapOf(typeOf<User>() to UserNavType)
