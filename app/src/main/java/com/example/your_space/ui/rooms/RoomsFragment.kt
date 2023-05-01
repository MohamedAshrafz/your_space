package com.example.your_space.ui.rooms

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentRoomsBinding
import com.example.your_space.ui.ourspaces.SecondFragmentDirections
import com.example.your_space.ui.ourspaces.SpaceDetailsFragmentDirections


class RoomsFragment : Fragment() {


    private var _binding: FragmentRoomsBinding? = null
    private val binding
        get() = _binding!!

    private val roomViewModel by activityViewModels<RoomsViewModel>()
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)

        binding.viewModel = roomViewModel

        val adaptor =
            RoomsAdapter { roomItem ->
                roomViewModel.onSelectRoomItem(roomItem)
            }

        binding.lifecycleOwner = viewLifecycleOwner



        binding.lifecycleOwner = viewLifecycleOwner
        binding.roomItemsRecyclerView.adapter = adaptor


        roomViewModel.selectedRoomItem.observe(viewLifecycleOwner) { selectedRoomItem ->
            if (selectedRoomItem != null) {
                requireView().findNavController()
                    .navigate(
                        RoomsFragmentDirections.actionRoomsFragmentToAddNewBookingFromWS(selectedRoomItem)
                    )
                roomViewModel.clearSelectedItem()
            }
        }

        roomViewModel.selectedRoomItem

        binding.swipeRefreshLayout.setOnRefreshListener {
            roomViewModel.refreshOnSwipe()
        }

        roomViewModel.isSwipeRefreshing.observe(viewLifecycleOwner){ isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (!isRefreshing){
                adaptor.notifyDataSetChanged()
                binding.roomItemsRecyclerView.scrollToPosition(0)
            }
        }

        return binding.root
    }

}