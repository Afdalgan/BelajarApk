package com.example.dicodingevent.ui.favorite

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.DetailActivity
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.ViewModelFactory
import com.example.dicodingevent.data.entity.EventEntity
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.ui.adapter.FavoriteAdapter

class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        FavoriteAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            }
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())

        viewModel.getFavoriteEvent()

        viewModel.eventData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    adapter.submitList(result.data)
                    hideError()
                }
                is Result.Error -> {
                    showLoading(false)
                    showError(result.error)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getFavoriteEvent()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    private fun setEventData(finishedEvents: List<EventEntity>) {
//        adapter.submitList(finishedEvents)
//        binding.rvFavoriteEvent.adapter = adapter
//        binding.rvFavoriteEvent.visibility = View.VISIBLE
//    }

    private fun showError(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.rvFavoriteEvent.visibility = View.INVISIBLE
    }

    private fun hideError() {
        binding.tvErrorMessage.visibility = View.GONE
        binding.rvFavoriteEvent.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.rvFavoriteEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }
}