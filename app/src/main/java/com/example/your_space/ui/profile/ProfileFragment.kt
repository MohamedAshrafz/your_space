package com.example.your_space.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentProfileBinding
import com.example.your_space.ui.authentication.AuthenticationActivity


class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        val sp = requireActivity().getSharedPreferences(
            AuthenticationActivity.LOGIN_STATE,
            AppCompatActivity.MODE_PRIVATE
        )

        val userId = sp.getString(AuthenticationActivity.USER_ID, null)

        if (userId != null) {
            profileViewModel = ViewModelProvider(
                this,
                ProfileViewModelFactory(requireActivity().application, userId)
            )[ProfileViewModel::class.java]
        }

        binding.viewModel = profileViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editProfileFab.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }

        return binding.root
    }
}