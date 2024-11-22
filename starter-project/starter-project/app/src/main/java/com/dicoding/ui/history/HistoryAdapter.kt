package com.dicoding.ui.history

import android.annotation.SuppressLint
import android.net.Uri
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.R
import com.dicoding.asclepius.entity.HistoryEntity

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.HistoryViewHolder>(DiffCallback) {

    private var onDelete: (HistoryEntity) -> Unit = {}

    fun setOnDeleteListener(listener: (HistoryEntity) -> Unit) {
        onDelete = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val prediction = getItem(position)
        holder.bind(prediction, onDelete)
    }

    class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val resultText: TextView = itemView.findViewById(R.id.result_text)
        private val timestampText: TextView = itemView.findViewById(R.id.timestamp_text)
        private val imageView: ImageView = itemView.findViewById(R.id.result_image)
        private val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        @SuppressLint("SetTextI18n")
        fun bind(prediction: HistoryEntity, onDelete: (HistoryEntity) -> Unit) {
            resultText.text = prediction.result
            timestampText.text = DateFormat.format("dd/MM/yyyy hh:mm:ss", prediction.timestamp)
            imageView.setImageURI(Uri.parse(prediction.imageUri))

            deleteButton.setOnClickListener {
                onDelete(prediction)
            }
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<HistoryEntity>() {
            override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}
