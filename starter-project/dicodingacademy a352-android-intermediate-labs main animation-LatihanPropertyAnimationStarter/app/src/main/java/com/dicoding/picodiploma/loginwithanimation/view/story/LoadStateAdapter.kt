package com.dicoding.picodiploma.loginwithanimation.view.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.picodiploma.loginwithanimation.R

class LoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.load_state_footer, parent, false)
        return LoadStateViewHolder(view, retry)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class LoadStateViewHolder(
    private val view: View,
    private val retry: () -> Unit
) : RecyclerView.ViewHolder(view) {
    private val progressBar: ProgressBar = view.findViewById(R.id.progress_bar)
    private val retryButton: Button = view.findViewById(R.id.retry_button)
    private val errorText: TextView = view.findViewById(R.id.error_text)

    fun bind(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        retryButton.isVisible = loadState is LoadState.Error
        errorText.isVisible = loadState is LoadState.Error

        if (loadState is LoadState.Error) {
            errorText.text = loadState.error.localizedMessage
            retryButton.setOnClickListener { retry() }
        }
    }
}