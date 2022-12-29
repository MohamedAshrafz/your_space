package com.example.your_space.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.your_space.R
import com.example.your_space.databinding.FragmentSpaceDetailsBinding
import com.example.your_space.ui.SpaceDetailsFragmentArgs.Companion.fromBundle
import com.google.android.material.bottomnavigation.BottomNavigationView


class SpaceDetailsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.let {
//            it.visibility = View.GONE
//        }
        val binding = FragmentSpaceDetailsBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val spaceItem = SpaceDetailsFragmentArgs.fromBundle(requireArguments()).selectedSpaceItem

        binding.spaceItem = spaceItem

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}