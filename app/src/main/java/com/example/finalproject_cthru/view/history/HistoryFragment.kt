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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val dashboardViewModel =
            ViewModelProvider(this).get(HistoryViewModel::class.java)

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHistory
//        dashboardViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.analyzeButton.setOnClickListener {
            val intent = Intent(requireActivity(), UploadActivity::class.java)
            requireActivity().startActivity(intent)
        }

        binding.logAdapter.layoutManager = LinearLayoutManager(requireContext())
        binding.logAdapter.setHasFixedSize(true)

        val historyUserViewModel = obtainViewModel(requireActivity())

        historyUserViewModel.getAllUsers().observe(viewLifecycleOwner) { list ->
            if (list.isNotEmpty()) {
                val adapter = DetectionAdapter(list)
                binding.logAdapter.adapter = adapter
            }
        }

        return root
    }

    private fun obtainViewModel(activity: FragmentActivity): HistoryViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[HistoryViewModel::class.java]
    }
}