package com.example.dicodingevent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.ViewModelFactory
import com.example.dicodingevent.databinding.ActivityDetailBinding
import java.util.Locale

class DetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityDetailBinding
    private var isFavorite = false
    private lateinit var eventLink: String
    private val viewModel: DetailViewModel by lazy {
        val factory = ViewModelFactory.getInstance(this)
        ViewModelProvider(this, factory)[DetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.imgLike.setOnClickListener(this)

        binding.btnRegist.setOnClickListener(this)

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, 0)

        observeViewModel()
        viewModel.getEventDetailById(eventId.toString())

    }

    private fun observeViewModel() {
        viewModel.eventDetail.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    hideError()

                    result.data.let { eventData ->
                        binding.apply {
                            titleEvent.text = eventData.name
                            organizerTitle.text = eventData.ownerName
                            timeEvent.text = eventData.beginTime
                            quota.text = String.format(
                                Locale.getDefault(),
                                "%d",
                                eventData.quota - eventData.registrants
                            )

                            Glide.with(root.context)
                                .load(eventData.mediaCover)
                                .into(imgEvent)

                            description.text = HtmlCompat.fromHtml(
                                eventData.description,
                                HtmlCompat.FROM_HTML_MODE_LEGACY
                            )
                        }

                        updateFavoriteIcon(eventData.isFavorite)
                        isFavorite = eventData.isFavorite
                        eventLink = eventData.link
                    }
                }

                is Result.Error -> {
                    showError(result.error ?: "Unknown error")
                }
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showError(errorMessage)
            } else {
                hideError()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRegist -> {
                eventLink.let { link ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(link)
                    this.startActivity(intent)
                }
            }

            R.id.imgLike -> {
                val eventData = (viewModel.eventDetail.value as? Result.Success)?.data
                eventData?.let {
                    viewModel.toggleFavoriteStatus(it)
                }
            }

        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val favoriteDrawable = if (isFavorite) {
            R.drawable.baseline_favorite_24
        } else {
            R.drawable.ic_favorite_24
        }
        binding.imgLike.setImageDrawable(ContextCompat.getDrawable(this, favoriteDrawable))
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    private fun showError(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE

        binding.eventOrganizer.visibility = View.VISIBLE
        binding.timeEvent.visibility = View.VISIBLE
        binding.quota.visibility = View.VISIBLE
        binding.description.visibility = View.VISIBLE
        binding.btnRegist.visibility = View.VISIBLE
    }

    private fun hideError() {
        binding.tvErrorMessage.visibility = View.GONE

        binding.eventOrganizer.visibility = View.VISIBLE
        binding.timeEvent.visibility = View.VISIBLE
        binding.quota.visibility = View.VISIBLE
        binding.description.visibility = View.VISIBLE
        binding.btnRegist.visibility = View.VISIBLE
    }
}