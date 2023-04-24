package com.example.your_space.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.your_space.databinding.FragmentCurrentBookingPageBinding
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
                "Cancel"
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
                findNavController().navigate(BookingFragmentDirections.actionBookingFragmentToAddNewBookFragment())
            }
        } else {
            rvAdaptor = BookingRecyclerViewAdaptor(
                { bookItem -> bookingAppViewModel.onDeleteBookedItem(bookItem) },
                "Delete"
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

        return binding.root
    }
}