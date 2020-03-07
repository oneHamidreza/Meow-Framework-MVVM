package meow.core.data

import `in`.co.ophio.secure.core.KeyStoreKeyGenerator
import `in`.co.ophio.secure.core.ObscuredPreferencesBuilder
import android.app.Application
import android.content.SharedPreferences
import meow.utils.avoidException
import meow.utils.fetchByClass
import meow.utils.toJson

/**
 * The Shared Preferences class with obscured data availability.
 *
 * @author  Hamidreza Etebarian
 * @version 1.0.0
 * @since   2020-03-06
 */

@Suppress("unused", "MemberVisibilityCanBePrivate")
open class MeowSharedPreferences(application: Application, settingName: String? = null) {

    var key: String =
        KeyStoreKeyGenerator.get(application, application.packageName).loadOrGenerateKeys()
    var sp: SharedPreferences = ObscuredPreferencesBuilder()
        .setApplication(application)
        .obfuscateValue(true)
        .obfuscateKey(true)
        .setSharePrefFileName(settingName)
        .setSecret(key)
        .createSharedPrefs()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> get(key: String, def: T): T {
        return when (def) {
            is Boolean -> sp.getBoolean(key, def) as T
            is Int -> sp.getInt(key, def) as T
            is Long -> sp.getLong(key, def) as T
            is Float -> sp.getFloat(key, def) as T
            is Double -> sp.getFloat(key, def.toFloat()).toDouble() as T
            is String -> sp.getString(key, def) as T
            else -> sp.getString(key, "").fetchByClass() ?: def
        }
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
