package com.example.finalproject_cthru.view.result

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject_cthru.R
import com.example.finalproject_cthru.data.remote.response.UserPrediction
import com.example.finalproject_cthru.databinding.ActivityResultBinding
import com.example.finalproject_cthru.view.history.HistoryViewModelFactory
import com.example.finalproject_cthru.view.login.LoginActivity
import com.example.finalproject_cthru.view.maps.MapsActivity
import com.example.finalproject_cthru.view.upload.UploadActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var isFavorite: Boolean = false
    private lateinit var resultViewModel: ResultViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        ReceiveSetup()

        resultViewModel = obtainViewModel(this)

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            // Add any result data if needed
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        binding.mapsButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.buttonSave.setOnClickListener() {
            val imageUri = Uri.parse(intent.getStringExtra(EXTRA_IMAGE_URI)!!)
            val eyePrediction = intent.getStringExtra(EXTRA_PREDICT_CATARACT)
            val cataractConfidence = intent.getDoubleExtra(EXTRA_CONFIDENCE_CATARACT, 0.0)

            val currentDateTime = LocalDateTime.now()
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
            val currentDate = currentDateTime.format(dateFormatter)
            val currentTime = currentDateTime.format(timeFormatter)

            val userPrediction = UserPrediction(
                resultPrediction = eyePrediction!!,
                confidenceScore = String.format("%.2f%%", cataractConfidence * 100),
                imagePrediction = imageUri.toString(),
                datePrediction = currentDate,
                timePrediction = currentTime
            )

            resultViewModel.insertDataUser(userPrediction)
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()

        }
    }


    private fun ReceiveSetup() {
        val image = intent.getStringExtra(EXTRA_IMAGE_URI)!!

        val bundle = intent.extras

        val imageUri = Uri.parse(image)
        imageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.resultImage.setImageURI(it)
        }

//        val eyeConfidence = bundle?.getDouble(EXTRA_CONFIDENCE_EYE)
//        val formattedEyeConfidence = String.format("%.2f%%", eyeConfidence?.times(100) ?: 0.0)
//        Log.d("Confidence Score", "Eye Confidence: $formattedEyeConfidence")
//        binding.eyeConfidenceResult.text = formattedEyeConfidence
//
//        val cataractConfidence = bundle?.getDouble(EXTRA_CONFIDENCE_CATARACT)
//        val formattedCataractConfidence =
//            String.format("%.2f%%", cataractConfidence?.times(100) ?: 0.0)
//        Log.d("Confidence Score", "Cataract Confidence: $formattedCataractConfidence")
//        binding.cataractConfidenceResult.text = formattedCataractConfidence

        val eyePrediction = bundle?.getString(EXTRA_PREDICT_CATARACT)
        binding.eyeConfidenceResult.text = eyePrediction

        val cataractConfidence = bundle?.getDouble(EXTRA_CONFIDENCE_CATARACT)
        val formattedCataractConfidence = String.format("%.2f%%", cataractConfidence?.times(100) ?: 0.0)
        Log.d("Confidence Score", "Cataract Confidence: $formattedCataractConfidence")
        binding.cataractConfidenceResult.text = formattedCataractConfidence


        if (eyePrediction == "Cataract") {
            binding.eyeConfidenceResult.setTextColor(resources.getColor(R.color.red))
        } else {
            binding.eyeConfidenceResult.setTextColor(resources.getColor(R.color.blue))
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun obtainViewModel(activity: AppCompatActivity): ResultViewModel {
        val factory = HistoryViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[ResultViewModel::class.java]
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_CONFIDENCE_CATARACT = "extra_confidence_cataract"
        const val EXTRA_CONFIDENCE_EYE = "extra_confidence_eye"
        const val EXTRA_PREDICT_CATARACT = "extra_predict_cataract"
    }
}