package com.example.submission

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission.Response.ListEventsItem


class ReviewAdapter(private val events: List<ListEventsItem?>?) :
    RecyclerView.Adapter<ReviewAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events?.get(position)
        holder.bind(event)
    }

    override fun getItemCount(): Int {
        return events?.size ?: 0
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val imageCover: ImageView = itemView.findViewById(R.id.imageCover)

        fun bind(event: ListEventsItem?) {
            title.text = event?.name
            Glide.with(itemView.context)
                .load(event?.imageLogo)
                .into(imageCover)
        }
    }
}

