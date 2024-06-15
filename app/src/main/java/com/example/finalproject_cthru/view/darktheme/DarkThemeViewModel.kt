package com.example.finalproject_cthru.view.darktheme

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.finalproject_cthru.data.local.pref.SettingPreferences
import kotlinx.coroutines.launch

class DarkThemeViewModel(private val pref: SettingPreferences) : ViewModel() {

    //  asLiveData digunakan untuk mengubah flow menjadi LiveData
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    //  Digunakan apabila tidak bisa membuat ViewModel dengan constructor secara langsung
    class DarkThemeViewModelFactory(private val pref: SettingPreferences) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DarkThemeViewModel::class.java)) {
                return DarkThemeViewModel(pref) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

}