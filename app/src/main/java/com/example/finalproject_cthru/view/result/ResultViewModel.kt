package com.example.finalproject_cthru.view.result

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.finalproject_cthru.data.remote.response.UserPrediction
import com.example.finalproject_cthru.data.repository.DetectionRepository

class ResultViewModel(mAppk: Application): ViewModel() {


    private val DetectionData: DetectionRepository = DetectionRepository(mAppk)

    fun insertDataUser(user: UserPrediction) {
        DetectionData.insertUser(user)
    }

    fun deleteDataUser(user: UserPrediction) {
        DetectionData.deleteUser(user)
    }

    companion object {
        private const val TAG = "ResultViewModel"
    }
}