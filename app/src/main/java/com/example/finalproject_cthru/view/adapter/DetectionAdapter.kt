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
import com.example.finalproject_cthru.databinding.HistoryItemBinding
import com.example.finalproject_cthru.view.history.HistoryViewModel

class DetectionAdapter(
    private val listUsers: MutableList<UserPrediction>,
    private val viewModel: HistoryViewModel
) : RecyclerView.Adapter<DetectionAdapter.ViewHolder>() {

    override fun getItemCount(): Int = listUsers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = HistoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = listUsers[position]
        holder.bind(user)
    }

    inner class ViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserPrediction) {
            binding.apply {
                logTitleText.text = user.resultPrediction
                scoreText.text = user.confidenceScore
                dateText.text = user.datePrediction
                timeText.text = user.timePrediction
                Glide.with(logImage.context)
                    .load(user.imagePrediction)
                    .into(logImage)

                deleteLogButton.setOnClickListener {
                    val adapterPosition = bindingAdapterPosition
                    if (adapterPosition != RecyclerView.NO_POSITION) {
                        viewModel.deleteUser(user)
                        listUsers.removeAt(adapterPosition)
                        notifyItemRemoved(adapterPosition)
                        // notifyItemRangeChanged(adapterPosition, itemCount - adapterPosition) // Use this for optimized range change
                        Toast.makeText(itemView.context, "Item deleted at position: $adapterPosition", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
