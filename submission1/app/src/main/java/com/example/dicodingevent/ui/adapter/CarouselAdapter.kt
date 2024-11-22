package com.example.dicodingevent.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.entity.EventEntity
import com.example.dicodingevent.databinding.ItemCarouselBinding

class CarouselAdapter(private val onClick: (EventEntity) -> Unit)
    : ListAdapter<EventEntity, CarouselAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        Log.d("EventAdapter", "Binding event at position $position: ${event.name}")
        holder.bind(event)
        holder.itemView.setOnClickListener{onClick(event)}
    }

    class MyViewHolder(private val binding: ItemCarouselBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: EventEntity) {
            binding.eventTitle.text = event.name
            Glide.with(binding.root.context)
                .load(event.mediaCover)
                .into(binding.imgItem)

            Log.d("EventAdapter", "Binding event: ${event.id} - ${event.name}")
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(
                oldItem: EventEntity,
                newItem: EventEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}