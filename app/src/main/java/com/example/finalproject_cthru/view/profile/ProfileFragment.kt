package com.example.finalproject_cthru.view.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.finalproject_cthru.BuildConfig
import com.example.finalproject_cthru.R
import com.example.finalproject_cthru.data.local.auth.Users
import com.example.finalproject_cthru.data.local.pref.SettingPreferences
import com.example.finalproject_cthru.data.local.pref.dataStore
import com.example.finalproject_cthru.databinding.FragmentProfileBinding
import com.example.finalproject_cthru.view.darktheme.DarkThemeViewModel
import com.example.finalproject_cthru.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var darkthemeViewModel: DarkThemeViewModel
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1
    private var currentImageUrl: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Setting Passing Data From Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        val uid = firebaseAuth.currentUser?.uid
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        storageReference = FirebaseStorage.getInstance().getReference("Users/$uid/profile.jpg")

        if (uid != null) {
            loadUserProfile(uid)
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        displayUserInfo()

        binding.versionInfo.text = BuildConfig.VERSION_NAME

        binding.logoutButton.setOnClickListener {
            signOut()
        }

        binding.editButton.setOnClickListener {
            val intent = Intent(activity, ProfileEditActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
        }

        // Dark Theme Setting
        val pref = SettingPreferences.getInstance(requireContext().dataStore)

        darkthemeViewModel = ViewModelProvider(
            this,
            DarkThemeViewModel.DarkThemeViewModelFactory(pref)
        )[DarkThemeViewModel::class.java]

        darkthemeViewModel.getThemeSettings().observe(viewLifecycleOwner) { isActive ->
            if (isActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switch1.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switch1.isChecked = false
            }
        }

        binding.switch1.setOnCheckedChangeListener { _: CompoundButton?, isChecked ->
            darkthemeViewModel.saveThemeSetting(isChecked)
        }

        return root
    }

    private fun loadUserProfile(uid: String) {
        databaseReference.child(uid).get().addOnSuccessListener { dataSnapshot ->
            val user = dataSnapshot.getValue(Users::class.java)
            user?.let {
                binding.name.text = it.fullNameUser
                binding.phoneNumberView.text = it.phoneNumber
                binding.ageView.text = it.age
                if (!it.imageUrl.isNullOrEmpty()) {
                    currentImageUrl = it.imageUrl
                    showImage()
                }
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show()
        }
    }


    private fun showImage() {
        currentImageUrl?.let { imageUrl ->
            Log.d("Image URL", "showImage: $imageUrl")
            Glide.with(this)
                .load(imageUrl)
                .into(binding.imgAvatar)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Handle result if needed
        }
    }

    private fun displayUserInfo() {
        val user = firebaseAuth.currentUser
        user?.let {
            val name = it.displayName ?: "No Name"
            val email = it.email ?: "No Email"
            binding.name.text = name
            binding.email.text = email
        }
    }

    private fun signOut() {
        firebaseAuth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val EDIT_PROFILE_REQUEST_CODE = 1
    }
}
