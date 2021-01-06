/*
 * Copyright (C) 2020 Hamidreza Etebarian & Ali Modares.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package meow.core.data

import android.app.Application
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import meow.ktx.avoidException
import meow.ktx.fromJson
import meow.ktx.toJson

/**
 * Shared Preferences class with obscured key and data availability.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

@Suppress("unused", "MemberVisibilityCanBePrivate")
class MeowSharedPreferences(
    application: Application,
    settingName: String? = null,
    key: MasterKey = MasterKey.Builder(application)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build(),
    val sp: SharedPreferences = EncryptedSharedPreferences.create(
        application,
        settingName ?: "encrypted_shared_preferences",
        key,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )
) {

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> get(key: String, def: T): T {
        return avoidException {
            when (def) {
                is Boolean -> sp.getBoolean(key, def) as T
                is Int -> sp.getInt(key, def) as T
                is Long -> sp.getLong(key, def) as T
                is Float -> sp.getFloat(key, def) as T
                is Double -> sp.getFloat(key, def.toFloat()).toDouble() as T
                is String -> sp.getString(key, def) as T
                else -> sp.getString(key, "").fromJson() ?: def
            }
        } ?: def
    }

    fun put(key: String, value: Any?) {
        avoidException {
            when (value) {
                is Boolean -> sp.edit().putBoolean(key, value).apply()
                is Int -> sp.edit().putInt(key, value).apply()
                is Long -> sp.edit().putLong(key, value).apply()
                is Float -> sp.edit().putFloat(key, value).apply()
                is Double -> sp.edit().putFloat(key, value.toFloat()).apply()
                is String -> sp.edit().putString(key, value).apply()
                else -> sp.edit().putString(key, value.toJson()).apply()
            }
        }
    }

}
