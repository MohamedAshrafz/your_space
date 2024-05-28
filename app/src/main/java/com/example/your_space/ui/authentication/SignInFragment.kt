package com.example.your_space.ui.authentication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.your_space.R
import com.example.your_space.databinding.FragmentSignInBinding
import com.google.android.material.snackbar.Snackbar

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
            signInViewModel.setLoginButtonPressed()
        }

        signInViewModel.loginButtonPressed.observe(viewLifecycleOwner) { isPressed ->
            if (isPressed) {
                if (signInViewModel.checkNotEmpty()) {
                    signInViewModel.runSignInFlow()
                } else {
                    Snackbar.make(
                        binding.root,
                        getString(R.string.Please_ill_both_username_and_password),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                signInViewModel.clearLoginButtonPressed()
            }
        }

        signInViewModel.showSignedUpToast.observe(viewLifecycleOwner) { toastText ->
            if (!toastText.isNullOrEmpty()) {
                if (toastText == getString(R.string.you_have_successfully_logged_in)) {
//                    Toast.makeText(requireContext(), toastText, Toast.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, toastText, Snackbar.LENGTH_SHORT).show()
                }
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