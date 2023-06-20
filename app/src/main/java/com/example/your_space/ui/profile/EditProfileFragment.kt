package com.example.your_space.ui.profile

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.example.your_space.databinding.FragmentEditProfileBinding
import com.example.your_space.ui.authentication.AuthenticationActivity


class EditProfileFragment : Fragment() {
    private lateinit var _binding: FragmentEditProfileBinding
    val binding
        get() = _binding

    private lateinit var editProfileViewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)

        val sp = requireActivity().getSharedPreferences(
            AuthenticationActivity.LOGIN_STATE,
            AppCompatActivity.MODE_PRIVATE
        )

        val userId = sp.getString(AuthenticationActivity.USER_ID, null)

        if (userId != null) {
            editProfileViewModel = ViewModelProvider(
                this,
                EditProfileViewModelFactory(requireActivity().application, userId)
            )[EditProfileViewModel::class.java]
        }

        binding.viewModel = editProfileViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        editProfileViewModel.user.observe(viewLifecycleOwner){ user->
            if (user != null){
                editProfileViewModel.apply {
                    _firstName.value = user.firstName
                    _lastName.value = user.lastName
                    _mobileNo.value = user.mobileNo
                    _address.value = user.address
                    _birthDate.value = user.birthDate
                    _bio.value = user.bio
                }
            }
        }

        binding.saveButton.setOnClickListener {

        }

        return binding.root
    }

}