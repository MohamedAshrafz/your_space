package com.example.your_space.ui.booking.addnewbook

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.graphics.Insets.add
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.core.graphics.Insets.add
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.your_space.database.BookingDB
import com.example.your_space.databinding.FragmentAddNewBookBinding
//import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.booking.BookingViewModel
import java.util.*

class AddNewBookFragment : Fragment() , DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    lateinit var _binding: FragmentAddNewBookBinding
    val addNewBookAppViewModel by activityViewModels<BookingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewBookBinding.inflate(inflater, container, false)




        _binding.bookDateEditText.setOnClickListener {
            if(addNewBookAppViewModel.savedDay == 0){
                addNewBookAppViewModel.getDateTimeCalender()
                DatePickerDialog(requireContext(),this,addNewBookAppViewModel.year,addNewBookAppViewModel.month,addNewBookAppViewModel.day).show()
            }
            else{
                DatePickerDialog(requireContext(),this,addNewBookAppViewModel.savedYear,addNewBookAppViewModel.savedCorrectMonth,addNewBookAppViewModel.savedDay).show()
            }
        }

        _binding.bookTimeEditText.setOnClickListener {
            if(addNewBookAppViewModel.savedHour == 0){
                addNewBookAppViewModel.getDateTimeCalender()
                TimePickerDialog(requireContext(), this, addNewBookAppViewModel.hour, addNewBookAppViewModel.minute, false).show()
            }
            else{
                TimePickerDialog(requireContext(), this, addNewBookAppViewModel.savedHour, addNewBookAppViewModel.savedMinute, false).show()
            }
        }

        _binding.btnAddNewBook.setOnClickListener {
            if( _binding.bookDateEditText.text.toString() == "" ||
                    _binding.bookTimeEditText.text.toString() == "" ||
                    _binding.durationEditText.text.toString() == ""){
                Toast.makeText(context,"Plaese Fill All Data", Toast.LENGTH_SHORT).show()
            }else{

                val newBook = BookingDB(
                    bookingId = "30",
                    startTime = "${addNewBookAppViewModel.savedHour}:${addNewBookAppViewModel.savedMinute}:00",
                    endTime = "${getEndTime().first}:${getEndTime().second}:00",
                    date = "${if (addNewBookAppViewModel.savedDay < 10) "0${addNewBookAppViewModel.savedDay}" else "${addNewBookAppViewModel.savedDay}"}-${if (addNewBookAppViewModel.savedMonth < 10) "0${addNewBookAppViewModel.savedMonth}" else "${addNewBookAppViewModel.savedMonth}"}-${addNewBookAppViewModel.savedYear}",
                    roomId = "1"
                )
                addNewBookAppViewModel.addNewBook(newBook)
                findNavController().navigate(AddNewBookFragmentDirections.actionAddNewBookFragmentToBookingFragment())

            }
//            if (!addNewBookAppViewModel.isEmptyBook(newBook)){

//            } else{
//                Toast.makeText(context,"Plaese Fill All Data", Toast.LENGTH_SHORT).show()
//            }
//        }

    }
        return _binding.root
    }


    fun getEndTime() : Pair<Int,Int> {
        val duration = _binding.durationEditText.text.toString().toInt()
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR,addNewBookAppViewModel.savedHour)
        cal.set(Calendar.MINUTE,addNewBookAppViewModel.savedMinute)
        cal.add(Calendar.HOUR,duration)
        val endHour = cal.get(Calendar.HOUR)
        val endMinute = cal.get(Calendar.MINUTE)
        return Pair(endHour,endMinute)
    }
    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        addNewBookAppViewModel.savedDay = dayOfMonth
        addNewBookAppViewModel.savedMonth = month+1
        addNewBookAppViewModel.savedCorrectMonth = month
        addNewBookAppViewModel.savedYear = year

        addNewBookAppViewModel.getDateTimeCalender()
        _binding.bookDateEditText.setText("${addNewBookAppViewModel.savedDay}-${addNewBookAppViewModel.savedMonth}-${addNewBookAppViewModel.savedYear}")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        addNewBookAppViewModel.savedMinute = minute
        addNewBookAppViewModel.savedHour = hourOfDay
        addNewBookAppViewModel.getDateTimeCalender()
        _binding.bookTimeEditText.setText("${addNewBookAppViewModel.savedHour}:${addNewBookAppViewModel.savedMinute}")
    }

}
