package com.example.your_space.ui.booking.addnewbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.your_space.database.BookingDB
import com.example.your_space.databinding.FragmentAddNewBookBinding
//import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.booking.BookingViewModel

class AddNewBookFragment : Fragment() {

    lateinit var _binding: FragmentAddNewBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewBookBinding.inflate(inflater, container, false)

        val addNewBookAppViewModel by activityViewModels<BookingViewModel>()

        val x = _binding.bookTimeEditText.text.toString()
        _binding.btnAddNewBook.setOnClickListener {
            val newBook = BookingDB(
                bookingId = "30",
                startTime = _binding.bookTimeEditText.text.toString(),
                endTime = "10:00:00",
                date = _binding.bookDateEditText.text.toString(),
                roomId = "1"
                )


            addNewBookAppViewModel.addNewBook(newBook)
            findNavController().navigate(AddNewBookFragmentDirections.actionAddNewBookFragmentToBookingFragment())

//            if (!addNewBookAppViewModel.isEmptyBook(newBook)){
//                addNewBookAppViewModel.addNewBook(newBook)
//                findNavController().navigate(AddNewBookFragmentDirections.actionAddNewBookFragmentToBookingFragment())
//            } else{
//                Toast.makeText(context,"Plaese Fill All Data", Toast.LENGTH_SHORT).show()
//            }
        }

        return _binding.root
    }
}