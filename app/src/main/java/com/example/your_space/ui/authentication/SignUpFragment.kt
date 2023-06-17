package com.example.your_space.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.your_space.databinding.FragmentSignUpBinding
import com.example.your_space.network.networkdatamodel.UserPropertyPost

class SignUpFragment : Fragment() {
    private lateinit var _binding: FragmentSignUpBinding
    val binding
        get() = _binding

    private val signUpViewModel by activityViewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)


        binding.viewModel = signUpViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.signUpButton.setOnClickListener {
            if (signUpViewModel.allFieldsNotEmpty()) {

                val newUser = UserPropertyPost(
                    email = signUpViewModel.email.value ?: "",
                    firstName = signUpViewModel.firstName.value ?: "",
                    lastName = signUpViewModel.lastName.value ?: "",
                    password = signUpViewModel.password.value ?: "",
                    mobileNo = signUpViewModel.mobileNo.value ?: "",
                    address = signUpViewModel.address.value ?: "",
                    birthDate = signUpViewModel.birthDate.value ?: "",
                    username = signUpViewModel.username.value ?: ""
                )

                signUpViewModel.postNewUser(newUser)
//                Toast.makeText(requireContext(), newUser.toString(), Toast.LENGTH_LONG)
//                    .show()
            } else {
                Toast.makeText(requireContext(), "Please fill all fields first", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        signUpViewModel.showSignedUpToast.observe(viewLifecycleOwner){ toastText ->
            if(!toastText.isNullOrEmpty()){
                Toast.makeText(requireContext(), toastText, Toast.LENGTH_LONG).show()
                signUpViewModel.clearSignedUpToast()
            }
        }

        return binding.root
    }

}