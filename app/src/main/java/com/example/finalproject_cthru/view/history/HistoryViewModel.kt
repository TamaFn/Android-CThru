package com.example.finalproject_cthru.view.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject_cthru.data.remote.response.UserPrediction
import com.example.finalproject_cthru.data.repository.DetectionRepository
import com.example.finalproject_cthru.view.result.ResultViewModel

class HistoryViewModel(application: Application) : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is history Fragment"
    }
    val text: LiveData<String> = _text

    private val mUserRepo: DetectionRepository = DetectionRepository(application)

    fun getAllUsers(): LiveData<List<UserPrediction>> = mUserRepo.getAllData()

    fun removeAllUsers() = mUserRepo.removeAllData()
}

class HistoryViewModelFactory(private val mApp: Application) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(mApp) as T
        } else if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
            return ResultViewModel(mApp) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var INSTANCE: HistoryViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application): HistoryViewModelFactory {
            if (INSTANCE == null) {
                synchronized(HistoryViewModelFactory::class.java) {
                    INSTANCE = HistoryViewModelFactory(application)
                }
            }
            return INSTANCE as HistoryViewModelFactory
        }
    }
}