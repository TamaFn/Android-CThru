package com.example.finalproject_cthru.view.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.finalproject_cthru.R
import com.example.finalproject_cthru.data.local.auth.Users
import com.example.finalproject_cthru.databinding.ActivityProfileEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileEditBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var currentImageUri: Uri? = null

    private val launcherGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                currentImageUri = uri
                showImage()
            } else {
                Toast.makeText(this, getString(R.string.no_image_selected), Toast.LENGTH_SHORT).show()
            }
        }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            Glide.with(this).load(it).into(binding.imgAvatar)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setting up Firebase
        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        storageReference = FirebaseStorage.getInstance().getReference("Users/$uid/profile.jpg")

        setupView()

        if (uid != null) {
            loadUserProfile(uid)
        }

        binding.backButton.setOnClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }

        binding.saveButton.setOnClickListener {
            Log.d("Save Button", "Save button clicked")
            val fullName = binding.nameEditText.text.toString()
            val phoneNumber = binding.phoneEditTextProfile.text.toString()
            val age = binding.ageEditTextProfile.text.toString()

            val user = Users(fullName, phoneNumber, age, currentImageUri.toString())
            if (uid != null) {
                databaseReference.child(uid).setValue(user).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (currentImageUri != null) {
                            uploadFile(uid)
                        } else {
                            Toast.makeText(this@ProfileEditActivity, "Profile updated", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    } else {
                        Toast.makeText(this@ProfileEditActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.addImgButton.setOnClickListener {
            startGallery()
        }
    }

    private fun loadUserProfile(uid: String) {
        databaseReference.child(uid).get().addOnSuccessListener { dataSnapshot ->
            val user = dataSnapshot.getValue(Users::class.java)
            user?.let {
                binding.nameEditText.setText(it.fullNameUser)
                binding.phoneEditTextProfile.setText(it.phoneNumber)
                binding.ageEditTextProfile.setText(it.age)
                if (!it.imageUrl.isNullOrEmpty()) {
                    currentImageUri = Uri.parse(it.imageUrl)
                    showImage()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadFile(uid: String) {
        Log.d("Upload File", "Uploading file...")

        currentImageUri?.let { uri ->
            storageReference.putFile(uri).addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to update profile image", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Log.d("Upload File", "No image selected")
            Toast.makeText(this@ProfileEditActivity, "No image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private fun setupView() {
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
}