package com.example.your_space.ui.ourspaces

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.R
import com.example.your_space.databinding.FragmentSecondBinding
import com.example.your_space.ui.ViewModel
import com.example.your_space.ui.booking.BookingFragmentDirections

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {


    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        val spaceViewModel by activityViewModels<ViewModel>()
        binding.viewModel = spaceViewModel
        val adaptor =
            OurSpacesAdabter{ spaceItem ->
                spaceViewModel.onSelectSpaceItem(spaceItem)
            }

        binding.lifecycleOwner = this
        binding.spaceItemsRecyclerView.adapter = adaptor

        spaceViewModel.selectedSpaceItem.observe(viewLifecycleOwner, Observer { selectedSpaceItem ->
            if (selectedSpaceItem != null) {
                requireView().findNavController()
                    .navigate(SecondFragmentDirections.actionSecondFragmentToSpaceDetailsFragment(selectedSpaceItem))
                spaceViewModel.clearSelectedItem()
            }
        })

        binding.addSpaceFab.setOnClickListener {
            findNavController().navigate(SecondFragmentDirections.actionSecondFragmentToAddNewSpaceFragment())
        }

        binding.spaceItemsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0)
                    binding.addSpaceFab.hide()
                else if (dy < 0)
                    binding.addSpaceFab.show()
            }
        })

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}