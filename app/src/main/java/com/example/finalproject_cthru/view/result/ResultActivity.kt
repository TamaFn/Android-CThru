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
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject_cthru.R
import com.example.finalproject_cthru.data.remote.response.UserPrediction
import com.example.finalproject_cthru.databinding.ActivityResultBinding
import com.example.finalproject_cthru.view.history.HistoryViewModelFactory
import com.example.finalproject_cthru.view.login.LoginActivity
import com.example.finalproject_cthru.view.maps.MapsActivity
import com.example.finalproject_cthru.view.upload.UploadActivity

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private var isFavorite: Boolean = false
    private lateinit var resultViewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        ReceiveSetup()

        resultViewModel = obtainViewModel(this)
        val user = intent.getParcelableExtra<UserPrediction>(EXTRA_CONFIDENCE_CATARACT) as UserPrediction

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

        binding.buttonSave.setOnClickListener(){
            if (isFavorite) {
                resultViewModel.deleteDataUser(user)
                Toast.makeText(this, "Delete is Successful", Toast.LENGTH_SHORT).show()
            } else {
                resultViewModel.insertDataUser(user)
                Toast.makeText(this, "Add is Successful", Toast.LENGTH_SHORT).show()
            }
            isFavorite = !isFavorite
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