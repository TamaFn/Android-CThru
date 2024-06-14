package com.example.finalproject_cthru.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_cthru.R
import com.example.finalproject_cthru.data.remote.response.UserPrediction

class DetectionAdapter(private val listUsers: List<UserPrediction>) :
    RecyclerView.Adapter<DetectionAdapter.ViewHolder>()  {


    override fun getItemCount(): Int = listUsers.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.history_item, viewGroup, false)
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        val user = listUsers[position]
        viewHolder.result_prediction.text = user.resultPrediction
        viewHolder.score_prediction.text = user.confidenceScore
        Glide.with(viewHolder.image_prediction.context)
            .load(user.imagePrediction)
            .into(viewHolder.image_prediction)

    }


    inner class ViewHolder(private var view: View) : RecyclerView.ViewHolder(view) {
        val result_prediction: TextView = view.findViewById(R.id.log_title_text)
        val score_prediction: TextView = view.findViewById(R.id.score_text)
        val image_prediction: ImageView = view.findViewById(R.id.log_image)
    }
}