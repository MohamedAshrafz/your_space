package com.example.your_space.ui.rooms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.your_space.databinding.FragmentRoomsBinding


class RoomsFragment : Fragment() {


    private var _binding: FragmentRoomsBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var roomViewModel: RoomsViewModel
    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRoomsBinding.inflate(inflater, container, false)
        val spaceId = RoomsFragmentArgs.fromBundle(requireArguments()).spaceId

        roomViewModel = ViewModelProvider(
            this,
            RoomsViewModelFactory(requireActivity().application, spaceId)
        )[RoomsViewModel::class.java]

        binding.viewModel = roomViewModel

        val adaptor =
            RoomsAdapter { roomItem ->
                roomViewModel.onSelectRoomItem(roomItem)
            }


        binding.lifecycleOwner = viewLifecycleOwner
        binding.roomItemsRecyclerView.adapter = adaptor


        roomViewModel.selectedRoomItem.observe(viewLifecycleOwner) { selectedRoomItem ->
            if (selectedRoomItem != null) {
                requireView().findNavController()
                    .navigate(
                        RoomsFragmentDirections.actionRoomsFragmentToAddNewBookingFromWS(
                            selectedRoomItem
                        )
                    )
                roomViewModel.clearSelectedItem()
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            roomViewModel.refreshOnSwipe()
        }

        roomViewModel.isSwipeRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (!isRefreshing) {
                adaptor.notifyDataSetChanged()
//                binding.roomItemsRecyclerView.scrollToPosition(0)
            }
        }

        return binding.root
    }

}