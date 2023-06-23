package com.example.your_space.ui.ourspaces.ratings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.your_space.databinding.FragmentRatingsBinding
import com.example.your_space.ui.Ratingss.RatingsAdapter

class RatingsFragment : Fragment() {
    private var _binding: FragmentRatingsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var viewModel: RatingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRatingsBinding.inflate(inflater, container, false)
        val spaceId = RatingsFragmentArgs.fromBundle(requireArguments()).spaceIdRatings

        viewModel = ViewModelProvider(
            this,
            RatingsViewModelFactory(requireActivity().application, spaceId)
        )[RatingsViewModel::class.java]

        binding.viewModel = viewModel

        val adaptor = RatingsAdapter()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.ratingsItemsRecyclerView.adapter = adaptor

        return binding.root
    }

}