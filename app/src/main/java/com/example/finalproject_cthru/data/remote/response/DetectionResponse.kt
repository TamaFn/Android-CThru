package com.example.finalproject_cthru.data.remote.response

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserPrediction(

    @PrimaryKey
    @field:SerializedName("id")
    @ColumnInfo("id")
    val id: String,

    @field:SerializedName("result_prediction")
    @ColumnInfo("result_prediction")
    val resultPrediction: String,

    @ColumnInfo
    @field:SerializedName("confidence_score")
    val confidenceScore: String,

    @ColumnInfo("image_prediction")
    @field:SerializedName("image_prediction")
    val imagePrediction: String? = null,

    @ColumnInfo
    @field:SerializedName("date_prediction")
    val datePrediction: String? = null,

    @ColumnInfo
    @field:SerializedName("time_prediction")
    val timePrediction: String? = null
) : Parcelable