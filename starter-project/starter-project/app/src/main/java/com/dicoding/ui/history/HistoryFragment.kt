package com.dicoding.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.FragmentHistoryBinding
import com.dicoding.asclepius.db.AppDatabase
import com.dicoding.asclepius.entity.HistoryEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: AppDatabase
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        db = AppDatabase.getDatabase(requireContext())
        setupRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        loadPredictionHistory() // Memuat ulang data setiap kali fragment terlihat
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter()
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HistoryFragment.adapter
        }

        adapter.setOnDeleteListener { prediction ->
            deletePrediction(prediction)
        }
    }

    private fun deletePrediction(prediction: HistoryEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            db.predictionHistoryDao().deletePrediction(prediction)
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Prediction deleted", Toast.LENGTH_SHORT).show()
                loadPredictionHistory()
            }
        }
    }

    private fun loadPredictionHistory() {
        db.predictionHistoryDao().getAllPredictions().observe(viewLifecycleOwner) { historyList ->
            if (historyList.isNotEmpty()) {
                adapter.submitList(historyList)
            } else {
                Toast.makeText(requireContext(), "No prediction history available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

