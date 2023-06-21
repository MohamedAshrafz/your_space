package com.example.your_space.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.your_space.databinding.FragmentMessagesBinding
import com.google.android.material.snackbar.Snackbar

class MessagesFragment : Fragment() {

    private lateinit var _binding: FragmentMessagesBinding
    val binding
        get() = _binding

    private val messagesViewModel by activityViewModels<MessagesViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessagesBinding.inflate(inflater, container, false)

        binding.viewModel = messagesViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.sendButton.setOnClickListener {
            messagesViewModel.sentMessage()
        }

        messagesViewModel.showSnackbarMessage.observe(viewLifecycleOwner){ snackbarText ->
            if (snackbarText.isNotEmpty()){
                Snackbar.make(binding.root, snackbarText, Snackbar.LENGTH_LONG).show()
                messagesViewModel.clearShowSnackbarMessage()
            }
        }

        return binding.root
    }

}