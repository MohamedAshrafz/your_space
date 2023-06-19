package com.example.your_space.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentMainAuthBinding

class MainAuthFragment : Fragment() {
    private lateinit var _binding: FragmentMainAuthBinding
    val binding
        get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainAuthBinding.inflate(inflater, container, false)

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(MainAuthFragmentDirections.actionMainAuthFragmentToSignUpFragment())
        }

        binding.loginButton.setOnClickListener {
            findNavController().navigate(MainAuthFragmentDirections.actionMainAuthFragmentToSignInFragment())
        }

        return binding.root
    }

}