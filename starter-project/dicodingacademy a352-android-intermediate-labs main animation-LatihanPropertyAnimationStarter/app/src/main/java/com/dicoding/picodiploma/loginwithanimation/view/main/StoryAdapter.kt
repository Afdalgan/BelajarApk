package com.dicoding.picodiploma.loginwithanimation.adapter

import android.app.Activity
import androidx.core.util.Pair
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import com.dicoding.picodiploma.loginwithanimation.view.DetailActivity

class StoryAdapter(private val stories: List<StoryItem>) : RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val iv_item_photo: ImageView = view.findViewById(R.id.iv_item_photo)
        val tv_item_name: TextView = view.findViewById(R.id.tv_item_name)
        val tv_detail_description: TextView = view.findViewById(R.id.tv_detail_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = stories[position]
        holder.tv_item_name.text = story.name
        holder.tv_detail_description.text = story.description

        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.iv_item_photo)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra("story_item", story)
            }

            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                holder.itemView.context as Activity,
                Pair(holder.iv_item_photo, "profile"),
                Pair(holder.tv_item_name, "name"),
                Pair(holder.tv_detail_description, "description")
            )

            holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
        }
    }

    override fun getItemCount(): Int = stories.size
}
