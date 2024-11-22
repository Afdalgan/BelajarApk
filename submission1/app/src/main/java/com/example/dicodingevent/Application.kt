package com.example.dicodingevent

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.dicodingevent.ui.setting.SettingPreference

class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        val themePreference = SettingPreference(this)
        if (themePreference.isDarkMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

    }
}
