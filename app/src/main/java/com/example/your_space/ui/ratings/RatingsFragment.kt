package com.example.your_space.ui.ratings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.your_space.R
import com.example.your_space.databinding.FragmentRatingsBinding
import com.example.your_space.databinding.FragmentRoomsBinding
import com.example.your_space.ui.booking.BookingViewModel


class RatingsFragment : Fragment() {

    private var _binding: FragmentRatingsBinding? = null
    private val binding
        get() = _binding!!

    val ratingViewModel by activityViewModels<RatingViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatingsBinding.inflate(inflater, container, false)
        val adapter = RatingsAdapter()

        binding.viewModel = ratingViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.ratingItemsRecyclerView.adapter = adapter
        // Inflate the layout for this fragment
        return binding.root
    }


}