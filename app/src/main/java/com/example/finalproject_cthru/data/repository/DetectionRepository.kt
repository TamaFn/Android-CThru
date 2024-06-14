package com.example.finalproject_cthru.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.finalproject_cthru.data.local.database.DetectionDao
import com.example.finalproject_cthru.data.local.database.DetectionDatabase
import com.example.finalproject_cthru.data.remote.response.UserPrediction
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DetectionRepository(application: Application) {
    private val detectionDao: DetectionDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    //  Dalam deklarasi init, harus primary constructor dan init harus saling terhubung
    init {
        val db = DetectionDatabase.getDatabase(application)
        detectionDao = db.detectionDao()
    }

    fun insertUser(user: UserPrediction) {
        executorService.execute { detectionDao.insert(user) }
    }
    fun deleteUser(user: UserPrediction) {
        executorService.execute { detectionDao.delete(user) }
    }
    fun getAllData(): LiveData<List<UserPrediction>> = detectionDao.getAllDataUsers()

    fun removeAllData(): Unit = detectionDao.removeAllDataUsers()


}