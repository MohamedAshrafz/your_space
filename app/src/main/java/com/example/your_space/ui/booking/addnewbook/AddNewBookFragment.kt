package com.example.your_space.ui.booking.addnewbook

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.your_space.database.BookingDB
import com.example.your_space.databinding.FragmentAddNewBookBinding
//import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.booking.BookingViewModel
import java.util.*

class AddNewBookFragment : Fragment() ,DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    lateinit var _binding: FragmentAddNewBookBinding
    var minute = 0
    var hour = 0
    var day =0
    var month = 0
    var year = 0

    var savedMinute = 0
    var savedHour = 0
    var savedDay =0
    var savedMonth = 0
    var savedYear = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewBookBinding.inflate(inflater, container, false)

        val addNewBookAppViewModel by activityViewModels<BookingViewModel>()


        _binding.imgCalendar.setOnClickListener {
            getDateTimeCalender()
            DatePickerDialog(requireContext(),this,year,month,day).show()

        }

        _binding.imgStartTime.setOnClickListener {
            getDateTimeCalender()
            TimePickerDialog(requireContext(),this,hour,minute,true).show()

        }

        _binding.btnAddNewBook.setOnClickListener {
            val newBook = BookingDB(
                bookingId = "30",
                startTime = "${savedHour}:${savedMinute}:00",
                endTime = "10:00:00",
                date = "${if (savedDay < 10) "0${savedDay}" else "${savedDay}"}-${if (savedMonth < 10) "0${savedMonth}" else "${savedMonth}"}-${savedYear}",
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
    private fun getDateTimeCalender(){
        val cal = Calendar.getInstance()
        minute = cal.get(Calendar.MINUTE)
        hour = cal.get(Calendar.HOUR)
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalender()
        _binding.bookDateEditText.setText("${savedDay}-${savedMonth}-${savedYear}")
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedMinute = minute
        savedHour = hourOfDay
        getDateTimeCalender()
        _binding.bookTimeEditText.setText("${savedHour}:${savedMinute}")
    }
}