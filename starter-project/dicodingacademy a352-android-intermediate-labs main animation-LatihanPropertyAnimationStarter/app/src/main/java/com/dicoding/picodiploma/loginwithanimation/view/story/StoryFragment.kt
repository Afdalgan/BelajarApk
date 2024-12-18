package com.dicoding.picodiploma.loginwithanimation.view.story

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.databinding.FragmentStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.add.AddActivity
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import com.dicoding.picodiploma.loginwithanimation.view.main.StoriesPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class StoryFragment : Fragment() {
    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var storiesPagingAdapter: StoriesPagingAdapter
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observePagingData()
        observeLoadState()
        setupAddStoryButton()

        if (viewModel.storiesPaging.value == null) {
            viewModel.fetchStoriesPaging()
        }
    }

    private fun setupRecyclerView() {
        storiesPagingAdapter = StoriesPagingAdapter()

        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = storiesPagingAdapter.withLoadStateFooter(
                footer = LoadStateAdapter { storiesPagingAdapter.retry() }
            )
        }
    }

    private fun observePagingData() {
        viewModel.storiesPaging.observe(viewLifecycleOwner) { pagingData ->
            storiesPagingAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun observeLoadState() {
        viewLifecycleOwner.lifecycleScope.launch {
            storiesPagingAdapter.loadStateFlow.collectLatest { loadState ->
                binding.progressBar.isVisible = loadState.refresh is androidx.paging.LoadState.Loading
            }
        }
    }

    private fun setupAddStoryButton() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(requireContext(), AddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
