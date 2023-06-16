package com.example.your_space.ui.authentication

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.your_space.R
import com.example.your_space.databinding.FragmentBookingBinding
import com.example.your_space.databinding.FragmentMainAuthBinding
import com.example.your_space.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private lateinit var _binding: FragmentSignUpBinding
    val binding
        get() = _binding

    companion object {
        fun newInstance() = SignUpFragment()
    }

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)

        return binding.root
    }

}