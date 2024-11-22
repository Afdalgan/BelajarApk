package com.dicoding.picodiploma.loginwithanimation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem

class StoryAdapter(private val stories: List<StoryItem>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgStory: ImageView = view.findViewById(R.id.imgStory)
        val txtStoryName: TextView = view.findViewById(R.id.txtStoryName)
        val txtStoryDescription: TextView = view.findViewById(R.id.txtStoryDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.txtStoryName.text = story.name
        holder.txtStoryDescription.text = story.description

        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.imgStory)
    }

    override fun getItemCount(): Int = stories.size
}
