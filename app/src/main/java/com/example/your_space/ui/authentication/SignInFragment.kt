package com.example.your_space.ui.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.your_space.R
import com.example.your_space.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var _binding: FragmentSignInBinding
    val binding
        get() = _binding

    private val signInViewModel by activityViewModels<SignInViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        binding.viewModel = signInViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.signInButton.setOnClickListener {
            if (signInViewModel.checkNotEmpty()) {
                signInViewModel.runSignInFlow()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please fill both username and password",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        signInViewModel.showSignedUpToast.observe(viewLifecycleOwner) { toastText ->
            if (!toastText.isNullOrEmpty()) {
                Toast.makeText(requireContext(), toastText, Toast.LENGTH_LONG).show()
            }
        }

        binding.userNameIte.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.userNameIte.hint = ""
            } else {
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.userNameIte, InputMethodManager.SHOW_IMPLICIT)

                binding.userNameIte.hint = getString(R.string.please_type_your_username)
            }
        }

        binding.passwordIte.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                binding.passwordIte.hint = ""
            } else {
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(binding.passwordIte, InputMethodManager.SHOW_IMPLICIT)

                binding.passwordIte.hint = getString(R.string.please_type_your_password)
            }
        }

        return binding.root
    }
}