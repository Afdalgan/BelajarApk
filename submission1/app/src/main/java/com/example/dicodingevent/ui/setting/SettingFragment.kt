package com.example.dicodingevent.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.dicodingevent.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding
    private lateinit var themePreferences: SettingPreference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding!!.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themePreferences = SettingPreference(requireContext())
        val isDarkMode = themePreferences.isDarkMode()

        binding?.switchTheme?.isChecked = themePreferences.isDarkMode()
        updateDescriptionText(isDarkMode)

        binding?.switchTheme?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                themePreferences.setDarkMode(true)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                themePreferences.setDarkMode(false)
            }
        }

//        val switchTheme = binding?.switchTheme
//        val pref = SettingPreference.getInstance(requireActivity().application.dataStore)
//        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]
//
//        settingViewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive: Boolean ->
//            if (isDarkModeActive) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//                switchTheme?.isChecked = true
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//                switchTheme?.isChecked = false
//            }
//        }
//
//        switchTheme?.setOnCheckedChangeListener { _, isChecked: Boolean ->
//            settingViewModel.saveThemeSetting(isChecked)
//        }
    }

    private fun updateDescriptionText(isDarkMode: Boolean) {
        if (isDarkMode) {
            binding?.themeDescription2?.visibility = View.VISIBLE
            binding?.themeDescription?.visibility = View.GONE
        } else {
            binding?.themeDescription?.visibility = View.VISIBLE
            binding?.themeDescription2?.visibility = View.GONE
        }
    }
}