package com.example.your_space.ui.ourspaces


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentSpaceDetailsBinding


class SpaceDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSpaceDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        val spaceItem = SpaceDetailsFragmentArgs.fromBundle(requireArguments()).selectedSpaceItem

        binding.bookButton.setOnClickListener {
            findNavController().navigate(
                SpaceDetailsFragmentDirections.actionSpaceDetailsFragmentToAddNewBookingFromWS2(
                    spaceItem
                )
            )
        }

        binding.spaceItem = spaceItem

        return binding.root
    }


}