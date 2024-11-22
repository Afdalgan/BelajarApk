package com.dicoding.ui.article

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.asclepius.response.ArticlesItem
import com.dicoding.asclepius.databinding.ItemArticleBinding
import com.bumptech.glide.Glide
import com.dicoding.asclepius.R


class ArticleAdapter(
    private val articles: List<ArticlesItem>,
    private val onItemClick: (ArticlesItem) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articles[position]

        Glide.with(holder.itemView.context)
            .load(article.urlToImage ?: R.drawable.placeholder)
            .into(holder.binding.imageView)

        holder.binding.titleTextView.text = article.title
        holder.binding.descriptionTextView.text = article.description.takeIf { it.isNotEmpty() } ?: "Deskripsi tidak tersedia"

        holder.itemView.setOnClickListener {
            onItemClick(article)
        }
    }

    override fun getItemCount(): Int = articles.size

    inner class ArticleViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root)
}



