package com.example.your_space.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.your_space.databinding.FragmentSignUpBinding

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
            signUpViewModel.postNewUser()
        }

        signUpViewModel.showSignedUpToast.observe(viewLifecycleOwner) { toastText ->
            if (!toastText.isNullOrEmpty()) {
                Toast.makeText(requireContext(), toastText, Toast.LENGTH_LONG).show()
                signUpViewModel.clearSignedUpToast()
                if (!signUpViewModel.isValidPassword()) {
                    binding.passwordTe.error =
                        "The password must contain 8 characters at lease, upper, lower letters and numbers"
                } else {
                    binding.passwordTe.error = null
                }
                if (!signUpViewModel.isValidEmail()) {
                    binding.emailTe.error =
                        "Please enter a valid mail"
                } else {
                    binding.emailTe.error = null
                }
                if (!signUpViewModel.checkPasswordMatching() && signUpViewModel.isValidPassword()) {
                    binding.repasswordTe.error =
                        "Please make sure you entered the right password"
                } else {
                    binding.repasswordTe.error = null
                }

                if (toastText == "You have successfully signed up"){
                    (requireActivity() as AuthenticationActivity).signed.postValue(true)
                }
            }
        }

//        // you have to observe the object to exist
//        // if you removes this line signUpViewModel.validPassword.value will return null everywhere
//        signUpViewModel.validPassword.observeForever { }
//        signUpViewModel.validEmail.observeForever { }

        binding.passwordIte.setOnClickListener {
            binding.passwordTe.error = null
        }

        binding.emailIte.setOnClickListener {
            binding.emailTe.error = null
        }

        binding.repasswordIte.setOnClickListener {
            binding.repasswordTe.error = null
        }

        return binding.root
    }

}