package com.example.your_space.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.your_space.R
import com.example.your_space.databinding.FragmentProfileBinding
import com.example.your_space.ui.authentication.AuthenticationActivity
import com.firebase.ui.auth.AuthUI


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.btnLogoutProfile.setOnClickListener {
            AuthUI.getInstance().signOut(this.requireContext()).addOnSuccessListener {
                // finishing the current activity
                requireActivity().finish()

                // launch the sign-in activity
                val authenticationActivityIntent =
                    Intent(
                        this.requireContext(),
                        AuthenticationActivity::class.java
                    )
                startActivity(authenticationActivityIntent)
            }

            // Inflate the layout for this fragment

        }
        return binding.root
    }
}