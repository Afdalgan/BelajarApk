package com.dicoding.ui.article

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentArticleBinding
import com.dicoding.asclepius.response.ApiConfig
import com.dicoding.asclepius.response.ApiService
import com.dicoding.asclepius.response.ArticleResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleFragment : Fragment() {

    private lateinit var articleAdapter: ArticleAdapter
    private lateinit var binding: FragmentArticleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(inflater, container, false)

        val recyclerView = binding.recyclerView
        val progressBar = binding.progressBar
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Show ProgressBar when starting to load data
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        ApiConfig.getApiService().getArticlesByQuery("health", "cancer", "en", ApiService.API_KEY)
            .enqueue(object : Callback<ArticleResponse> {
                override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                    progressBar.visibility = View.GONE // Hide ProgressBar when data is received
                    if (response.isSuccessful) {
                        response.body()?.articles?.let {
                            val validArticles = it.filter { article ->
                                article.title != "[Removed]" && article.description != "[Removed]"
                            }
                            if (validArticles.isNotEmpty()) {
                                articleAdapter = ArticleAdapter(validArticles) { article ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                                    startActivity(intent)
                                }
                                recyclerView.adapter = articleAdapter
                                recyclerView.visibility = View.VISIBLE // Show RecyclerView after data is loaded
                            } else {
                                showErrorDialog("No valid articles found.")
                            }
                        }
                    } else {
                        showErrorDialog("Failed to load articles.")
                    }
                }

                override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE // Hide ProgressBar in case of failure
                    showErrorDialog(t.localizedMessage)
                }
            })

        return binding.root
    }

    private fun showErrorDialog(errorMessage: String?) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage(errorMessage ?: "An error occurred. Please try again later.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }
}
