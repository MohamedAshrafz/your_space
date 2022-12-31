package com.example.your_space.ui.booking.addnewbook

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.your_space.R
import com.example.your_space.databinding.FragmentAddNewBookBinding
import com.example.your_space.databinding.FragmentBookingBinding
import com.example.your_space.ui.AddNewSpaceFragmentDirections
import com.example.your_space.ui.ViewModel
import com.example.your_space.ui.booking.BookItem

class AddNewBookFragment : Fragment() {

    lateinit var _binding: FragmentAddNewBookBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewBookBinding.inflate(inflater, container, false)

        val addNewBookViewModel by activityViewModels<ViewModel>()

        _binding.btnAddNewBook.setOnClickListener {
            val newBook = BookItem(
                _binding.bookNameEditText.text.toString(),
                _binding.bookDateEditText.text.toString(),
                _binding.bookTimeEditText.text.toString()
            )

            if (!addNewBookViewModel.isEmptyBook(newBook)){
                addNewBookViewModel.addNewBook(newBook)
                findNavController().navigate(AddNewBookFragmentDirections.actionAddNewBookFragmentToBookingFragment())
            } else{
                Toast.makeText(context,"Plaese Fill All Data", Toast.LENGTH_SHORT).show()
            }
        }

        return _binding.root
    }
}