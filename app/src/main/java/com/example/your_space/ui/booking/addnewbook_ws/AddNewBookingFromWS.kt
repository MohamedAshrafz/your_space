package com.example.your_space.ui.booking.addnewbook_ws

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.your_space.databinding.FragmentAddNewBookingFromWSBinding
import com.example.your_space.ui.SpaceDetailsFragmentArgs
import com.example.your_space.ui.ViewModel
import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.booking.addnewbook.AddNewBookFragmentDirections

class AddNewBookingFromWS : Fragment() {
    lateinit var _binding: FragmentAddNewBookingFromWSBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewBookingFromWSBinding.inflate(inflater, container, false)

        // provide argument to the function
        val spaceItem = AddNewBookingFromWSArgs.fromBundle(requireArguments()).selectedSpace

        _binding.bookNameEditText.text = spaceItem.spaceName

        val addNewBookViewModel by activityViewModels<ViewModel>()

        _binding.btnAddNewBook.setOnClickListener {
            val newBook = BookItem(
                _binding.bookNameEditText.text.toString(),
                _binding.bookDateEditText.text.toString(),
                _binding.bookTimeEditText.text.toString()
            )

            if (!addNewBookViewModel.isEmptyBook(newBook)){
                addNewBookViewModel.addNewBook(newBook)
                findNavController().navigate(AddNewBookingFromWSDirections.actionAddNewBookingFromWSToBookingFragment())
            } else{
                Toast.makeText(context,"Please Fill All Data", Toast.LENGTH_SHORT).show()
            }
        }

        return _binding.root
    }
}