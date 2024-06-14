package com.example.finalproject_cthru.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.finalproject_cthru.data.remote.response.UserPrediction

@Database(entities = [UserPrediction::class], version = 4)
abstract class DetectionDatabase: RoomDatabase() {
    abstract fun detectionDao(): DetectionDao
    companion object {
        @Volatile
        private var INSTANCE: DetectionDatabase? = null
        @JvmStatic
        fun getDatabase(context: Context): DetectionDatabase {
            if (INSTANCE == null) {
                synchronized(DetectionDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DetectionDatabase::class.java, "database").fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as DetectionDatabase
        }
    }
}