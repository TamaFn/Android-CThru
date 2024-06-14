package com.example.finalproject_cthru.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.finalproject_cthru.data.remote.response.UserPrediction

@Dao
interface DetectionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserPrediction)
    @Delete
    fun delete(user: UserPrediction)
    @Query("SELECT * from UserPrediction ORDER BY result_prediction DESC")
    fun getAllDataUsers(): LiveData<List<UserPrediction>>

    @Query("DELETE FROM UserPrediction")
    fun removeAllDataUsers()

}