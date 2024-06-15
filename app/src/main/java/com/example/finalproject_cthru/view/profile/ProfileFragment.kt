package com.example.finalproject_cthru.view.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject_cthru.R
import com.example.finalproject_cthru.data.local.pref.SettingPreferences
import com.example.finalproject_cthru.data.local.pref.dataStore // Import your dataStore extension property here
import com.example.finalproject_cthru.databinding.FragmentProfileBinding
import com.example.finalproject_cthru.view.darktheme.DarkThemeViewModel
import com.example.finalproject_cthru.view.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var darkthemeViewModel: DarkThemeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        firebaseAuth = FirebaseAuth.getInstance()
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        displayUserInfo()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Handle the result from ProfileEditActivity
            // Update UI or refresh data if needed
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
