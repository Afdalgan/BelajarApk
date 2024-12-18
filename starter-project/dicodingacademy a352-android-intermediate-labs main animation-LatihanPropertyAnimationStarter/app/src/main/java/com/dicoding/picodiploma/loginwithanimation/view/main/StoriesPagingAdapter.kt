package com.dicoding.picodiploma.loginwithanimation.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryItem
import com.dicoding.picodiploma.loginwithanimation.view.DetailActivity

class StoriesPagingAdapter : PagingDataAdapter<StoryItem, StoriesPagingAdapter.StoryViewHolder>(
    DIFF_CALLBACK
) {

    class StoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivItemPhoto: ImageView = view.findViewById(R.id.iv_item_photo)
        val tvItemName: TextView = view.findViewById(R.id.tv_item_name)
        val tvDetailDescription: TextView = view.findViewById(R.id.tv_description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position)
        story?.let { storyItem ->
            holder.tvItemName.text = storyItem.name
            holder.tvDetailDescription.text = storyItem.description

            Glide.with(holder.itemView.context)
                .load(storyItem.photoUrl)
                .into(holder.ivItemPhoto)

            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                    putExtra("story_item", storyItem)
                }

                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.ivItemPhoto, "profile"),
                    Pair(holder.tvItemName, "name"),
                    Pair(holder.tvDetailDescription, "description")
                )

                holder.itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}