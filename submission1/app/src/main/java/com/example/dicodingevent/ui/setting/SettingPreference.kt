package com.example.dicodingevent.ui.setting

import android.content.Context

class SettingPreference(context: Context) {
    private val preferences = context.getSharedPreferences("theme_preferences", Context.MODE_PRIVATE)

    fun setDarkMode(isDarkMode: Boolean) {
        preferences.edit().putBoolean("dark_mode", isDarkMode).apply()
    }

    fun isDarkMode(): Boolean {
        return preferences.getBoolean("dark_mode",false)
    }
}

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//class SettingPreference private constructor(private val dataStore: DataStore<Preferences>) {
//
//    companion object {
//        @Volatile
//        private var INSTANCE: SettingPreference? = null
//
//        fun getInstance(dataStore: DataStore<Preferences>): SettingPreference {
//            return INSTANCE ?: synchronized(this) {
//                val instance = SettingPreference(dataStore)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//
//    private val themeKey = booleanPreferencesKey("theme_setting")
//
//    fun getThemeSetting(): Flow<Boolean> {
//        return dataStore.data.map { preferences ->
//            preferences[themeKey] ?: false
//        }
//    }
//
//    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[themeKey] = isDarkModeActive
//        }
//    }
//}