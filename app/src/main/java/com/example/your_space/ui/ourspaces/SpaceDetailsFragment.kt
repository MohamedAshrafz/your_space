package com.example.your_space.ui.ourspaces


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentSpaceDetailsBinding
import com.example.your_space.repository.AppRepository


class SpaceDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentSpaceDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        val spaceItem = SpaceDetailsFragmentArgs.fromBundle(requireArguments()).selectedSpaceItem
        val spaceId = spaceItem.spaceId

        binding.roomBtn.setOnClickListener {
            findNavController().navigate(
                SpaceDetailsFragmentDirections.actionSpaceDetailsFragmentToRoomsFragment(spaceId)
            )
        }

        binding.mapBtn.setOnClickListener {
            findNavController().navigate(
                SpaceDetailsFragmentDirections.actionSpaceDetailsFragmentToMapsFragment2(spaceId)
            )
        }


        binding.ratingsBtn.setOnClickListener {
            findNavController().navigate(
                SpaceDetailsFragmentDirections.actionSpaceDetailsFragmentToRatingsFragment(spaceId)
            )
        }

        binding.spaceItem = spaceItem

        return binding.root
    }


}