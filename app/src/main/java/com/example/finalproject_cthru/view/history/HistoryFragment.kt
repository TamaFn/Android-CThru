package com.example.finalproject_cthru.view.history

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.finalproject_cthru.databinding.FragmentHistoryBinding
import com.example.finalproject_cthru.view.adapter.DetectionAdapter
import com.example.finalproject_cthru.view.upload.UploadActivity


class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var dashboardViewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        dashboardViewModel = obtainViewModel(requireActivity())

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.analyzeButton.setOnClickListener {
            val intent = Intent(requireActivity(), UploadActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.logAdapter.layoutManager = LinearLayoutManager(requireContext())
        binding.logAdapter.setHasFixedSize(true)

//        binding.deleteAllButton.setOnClickListener {
//            dashboardViewModel.removeAllUsers()
//        }

        dashboardViewModel.getAllUsers().observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                val adapter = DetectionAdapter(list.toMutableList(), dashboardViewModel)
                binding.logAdapter.adapter = adapter
            } else {
                // Handle empty list
            }
        }


        return root
    }

    private fun obtainViewModel(activity: FragmentActivity): HistoryViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HistoryViewModel::class.java]
    }
}