package com.example.your_space.ui.booking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.databinding.FragmentBookingBinding
import com.example.your_space.ui.AppViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Dispatchers


class BookingFragment : Fragment() {
    private lateinit var _binding: FragmentBookingBinding

    private var inHistoryTab = false

    companion object {
        const val bookingTabPosition = 0
        const val historyTabPosition = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBookingBinding.inflate(inflater, container, false)

        val bookingAppViewModel by activityViewModels<AppViewModel>()

        val adaptorBooking = BookingRecyclerViewAdaptor(
            { bookItem -> bookingAppViewModel.onCancelBookedItem(bookItem) },
            "Cancel"
        )

        val adaptorHistory = BookingRecyclerViewAdaptor(
            { bookItem -> bookingAppViewModel.onDeleteBookedItem(bookItem) },
            "Delete"
        )

        _binding.viewModel = bookingAppViewModel
        _binding.lifecycleOwner = this

        adaptorBooking.submitList(bookingAppViewModel.bookedList.value)
        adaptorHistory.submitList(bookingAppViewModel.bookedHistoryList.value)

        _binding.bookingRecyclerView.adapter = adaptorBooking

        _binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {

                if (tab != null) {
                    when (tab.position) {
                        bookingTabPosition -> {
                            _binding.bookingRecyclerView.adapter = adaptorBooking
                            _binding.addBookFab.show()
                            inHistoryTab = false
                        }
                        historyTabPosition -> {
                            _binding.bookingRecyclerView.adapter = adaptorHistory
                            _binding.addBookFab.hide()
                            inHistoryTab = true
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        bookingAppViewModel.showDelete.observe(viewLifecycleOwner, Observer { value ->
            if (value == true) {
                _binding.bookingRecyclerView.adapter?.notifyDataSetChanged()
                bookingAppViewModel.clearDeleteBookedItem()
                Snackbar.make(
                    requireView(),
                    "The item was successfully deleted",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        bookingAppViewModel.showCancel.observe(viewLifecycleOwner, Observer { value ->
            if (value == true) {
                _binding.bookingRecyclerView.adapter?.notifyDataSetChanged()
                bookingAppViewModel.clearCancelBookedItem()
                Snackbar.make(
                    requireView(),
                    "The book was successfully cancelled",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        })

        _binding.addBookFab.setOnClickListener {
            findNavController().navigate(BookingFragmentDirections.actionBookingFragmentToAddNewBookFragment())
        }

        _binding.bookingRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!inHistoryTab){
                    if (dy > 0)
                        _binding.addBookFab.hide()
                    else if (dy < 0)
                        _binding.addBookFab.show()
                }
            }
        })

        return _binding.root
    }
}