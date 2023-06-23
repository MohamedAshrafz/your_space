package com.example.your_space.ui.booking

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.R
import com.example.your_space.databinding.FragmentCurrentBookingPageBinding
import com.example.your_space.ui.homepage.FragmentEnumForIndexing
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class CurrentBookingPageFragment : Fragment() {

    private lateinit var _binding: FragmentCurrentBookingPageBinding
    private val binding
        get() = _binding

    private val bookingAppViewModel by activityViewModels<BookingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCurrentBookingPageBinding.inflate(inflater, container, true)

        var recyclerViewType: String? = ""
        arguments?.takeIf { it.containsKey(CURRENT_OR_HISTORY_KEY) }?.apply {
            recyclerViewType = getString(CURRENT_OR_HISTORY_KEY)
        }

        val rvAdaptor: BookingRecyclerViewAdaptor

        if (recyclerViewType == RecyclerType.CURRENT.name) {
            rvAdaptor = BookingRecyclerViewAdaptor(
                { bookItem -> bookingAppViewModel.onCancelBookedItem(bookItem) },
                ButtonType.CANCEL.asString
            )
            bookingAppViewModel.showCancel.observe(viewLifecycleOwner) { value ->
                if (value == true) {
//                    binding.bookingRecyclerView.adapter?.notifyItemRemoved(0)
                    bookingAppViewModel.clearCancelBookedItem()
                    Snackbar.make(
                        requireView(),
                        "The book was successfully cancelled",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }

            binding.addBookFab.visibility = View.VISIBLE
            binding.bookingRecyclerView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0)
                        binding.addBookFab.hide()
                    else if (dy < 0)
                        binding.addBookFab.show()
                }
            })
            binding.addBookFab.setOnClickListener {
                val menuItem =
                    requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)?.menu?.get(
                        FragmentEnumForIndexing.OUR_SPACES.index
                    )
                if (menuItem != null) {
                    NavigationUI.onNavDestinationSelected(menuItem, findNavController())
                }

                try {
                    Snackbar.make(
                        requireActivity().findViewById(R.id.nav_host_fragment_content_main),
                        "Choose the preferred Space Now",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Log.e(
                        "Error showing Snackbar",
                        e.printStackTrace().toString()
                    )
                }
            }
        } else {
            rvAdaptor = BookingRecyclerViewAdaptor(
                { bookItem -> bookingAppViewModel.onCancelBookedItem(bookItem) },
                ButtonType.DELETE.asString
            )
            bookingAppViewModel.showDelete.observe(viewLifecycleOwner) { value ->
                if (value == true) {
//                    binding.bookingRecyclerView.adapter?.notifyDataSetChanged()
                    bookingAppViewModel.clearDeleteBookedItem()
                    Snackbar.make(
                        requireView(),
                        "The item was successfully deleted",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
            binding.addBookFab.visibility = View.INVISIBLE
        }



        binding.rvType = recyclerViewType
        binding.viewModel = bookingAppViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.bookingRecyclerView.adapter = rvAdaptor

        binding.swipeRefreshLayout.setOnRefreshListener {
            bookingAppViewModel.refreshOnSwipe()
        }

        bookingAppViewModel.isSwipeRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            binding.swipeRefreshLayout.isRefreshing = isRefreshing
            if (!isRefreshing) {
                rvAdaptor.notifyDataSetChanged()
                binding.bookingRecyclerView.scrollToPosition(0)
            }
        }

        return binding.root
    }
}