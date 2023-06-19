package com.example.your_space.ui.authentication

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.your_space.databinding.FragmentSignUpBinding
import java.util.*

class SignUpFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private lateinit var _binding: FragmentSignUpBinding
    val binding
        get() = _binding

    private val signUpViewModel by activityViewModels<SignUpViewModel>()

    @RequiresApi(Build.VERSION_CODES.N)
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
            }
        }

//        // you have to observe the object to exist
//        // if you removes this line signUpViewModel.validPassword.value will return null everywhere
//        signUpViewModel.validPassword.observeForever { }
//        signUpViewModel.validEmail.observeForever { }

        binding.passwordIte.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.passwordTe.error = null
            }
        }

        binding.emailIte.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.emailTe.error = null
            }
        }

        binding.repasswordIte.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.repasswordTe.error = null
            }
        }

        binding.birthDateIte.setOnClickListener {
            val dateAttrs = getCurrentDate()
            DatePickerDialog(
                requireContext(),
                this,
                dateAttrs.first,
                dateAttrs.second - 1,
                dateAttrs.third
            ).show()
        }

        return binding.root
    }

    private fun getCurrentDate(): Triple<Int, Int, Int> {
        val calendar = Calendar.getInstance()
        return if (signUpViewModel.savedDay.value == 0) {
            Triple(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        } else {
            Triple(
                signUpViewModel.savedYear.value ?: 0,
                signUpViewModel.savedMonth.value ?: 0,
                signUpViewModel.savedDay.value ?: 0
            )
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        signUpViewModel.setBirthDate(dayOfMonth, month + 1, year)
    }
}