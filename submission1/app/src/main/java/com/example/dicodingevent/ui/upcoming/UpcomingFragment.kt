package com.example.dicodingevent.ui.upcoming

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.DetailActivity
import com.example.dicodingevent.data.ViewModelFactory
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.adapter.EventAdapter
import com.example.dicodingevent.data.Result

class UpcomingFragment : Fragment() {
    private val viewModel: UpcomingViewModel by viewModels{
        ViewModelFactory.getInstance(requireActivity())
    }

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        EventAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_EVENT_ID, event.id.toInt())
            }
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.getHeadlineEvent()

        viewModel.getEventsByFinished(1)

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
                    showError("Terjadi kesalahan: ${result.error}")
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

//    private fun setEventData(upcomingEvents: List<EventEntity>) {
//        adapter.submitList(upcomingEvents)
//        binding.rvFinishedEvent.adapter = adapter
//        binding.rvFinishedEvent.visibility = View.VISIBLE
//    }

    private fun showError(message: String) {
        binding.tvErrorMessage.text = message
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.rvFinishedEvent.visibility = View.INVISIBLE
    }

    private fun hideError() {
        binding.tvErrorMessage.visibility = View.GONE
        binding.rvFinishedEvent.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        binding.rvFinishedEvent.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@UpcomingFragment.adapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    companion object {
//        const val ARG_TAB = "tab_name"
//        const val TAB_UPCOMING_EVENTS = "upcoming_events"
//        const val TAB_FAVORITE = "favorite"
    }
}