package com.example.finalproject_cthru.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.finalproject_cthru.R
import com.example.finalproject_cthru.data.remote.response.UserPrediction
import com.example.finalproject_cthru.view.history.HistoryViewModel

class DetectionAdapter(private val listUsers: MutableList<UserPrediction>,
                       private val viewModel: HistoryViewModel
) :
    RecyclerView.Adapter<DetectionAdapter.ViewHolder>()  {


    override fun getItemCount(): Int = listUsers.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.history_item, viewGroup, false)
    )

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val user = listUsers[position]
        viewHolder.result_prediction.text = user.resultPrediction
        viewHolder.score_prediction.text = user.confidenceScore
        viewHolder.date_prediction.text = user.datePrediction
        viewHolder.time_prediction.text = user.timePrediction
        Glide.with(viewHolder.image_prediction.context)
            .load(user.imagePrediction)
            .into(viewHolder.image_prediction)

        viewHolder.delete_log_button.setOnClickListener {
            viewModel.deleteUser(user)
            // Use position to remove the item from the list
            listUsers.removeAt(position)
            // Notify the adapter that the item is removed
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, listUsers.size)
            Toast.makeText(viewHolder.itemView.context, "Item deleted at position: $position", Toast.LENGTH_SHORT).show()
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val result_prediction: TextView = view.findViewById(R.id.log_title_text)
        val score_prediction: TextView = view.findViewById(R.id.score_text)
        val image_prediction: ImageView = view.findViewById(R.id.log_image)
        val date_prediction: TextView = view.findViewById(R.id.date_text)
        val time_prediction: TextView = view.findViewById(R.id.time_text)
        val delete_log_button: Button = view.findViewById(R.id.delete_log_button)
        }
    }