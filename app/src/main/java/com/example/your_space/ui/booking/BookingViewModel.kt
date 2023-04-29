package com.example.your_space.ui.booking

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.BookingDB
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

enum class RecyclerType {
    CURRENT,
    HISTORY
}

class BookingViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    private val _bookedList = repository.bookingsRepo
    val bookedList: LiveData<List<BookingDB>>
        get() = _bookedList

    private var _bookedHistoryList = MutableLiveData(
        listOf(
            BookingDB(
                bookingId = "8",
                date = "2023-4-28",
                startTime = "02:30",
                endTime = "04:30"
            ),
            BookingDB(
                bookingId = "9",
                date = "2023-4-28",
                startTime = "02:30",
                endTime = "04:30"
            ),
            BookingDB(
                bookingId = "10",
                date = "2023-4-28",
                startTime = "02:30",
                endTime = "04:30"
            ),
            BookingDB(
                bookingId = "11",
                date = "2023-4-28",
                startTime = "02:30",
                endTime = "04:30"
            ),
            BookingDB(
                bookingId = "12",
                date = "2023-4-28",
                startTime = "02:30",
                endTime = "04:30"
            )
        )
    )
    val bookedHistoryList: LiveData<List<BookingDB>>
        get() = _bookedHistoryList

    private val _showCancel = MutableLiveData(false)
    val showCancel: LiveData<Boolean>
        get() = _showCancel

    private val _showDelete = MutableLiveData(false)
    val showDelete: LiveData<Boolean>
        get() = _showDelete

    private var _isSwipeRefreshing = MutableLiveData(false)
    val isSwipeRefreshing: LiveData<Boolean>
        get() = _isSwipeRefreshing

    init {
        fillHistoryList()

        viewModelScope.launch {
            //repository.deleteBooking()

            repository.refreshBookings()
            repository.postBooking()
        }
    }

    fun onCancelBookedItem(bookItem: BookingDB) {
        viewModelScope.launch {
            repository.deleteBookingWithId(bookItem)
            _showCancel.value = true
        }
    }

    fun clearCancelBookedItem() {
        _showCancel.value = false
    }

    fun onDeleteBookedItem(bookItem: BookingDB) {
        _bookedHistoryList.postValue(_bookedHistoryList.value?.filter { it.bookingId != bookItem.bookingId })
        _showDelete.value = true
    }

    fun clearDeleteBookedItem() {
        _showDelete.value = false
    }

    fun getAppropriateRecyclerView(recyclerType: String): LiveData<List<BookingDB>> {
        return when (recyclerType) {
            RecyclerType.CURRENT.name -> bookedList
            else -> bookedHistoryList
        }
    }

    fun refreshOnSwipe() {
        viewModelScope.launch {
            _isSwipeRefreshing.value = true
            repository.refreshBookings()
            _isSwipeRefreshing.value = false
        }
    }

//    fun isEmptyBook(bookItem: BookingDB): Boolean {
//        if (bookItem.bookName.isEmpty() ||
//            bookItem.date.isEmpty() ||
//            bookItem.time.isEmpty()
//        ) {
//            return true
//        }
//        return false
//    }


    fun addNewBook(bookItem: BookingDB) {
        _bookedList.value?.toMutableList()?.add(
            bookItem
        )
    }

    private fun fillHistoryList() {
//        _bookedHistoryList.value?.apply {
//            add(
//                BookItem(
//                    bookName = "History booked 1",
//                    date = "11/11/1111",
//                    time = "11:11"
//                )
//            )
//            add(
//                BookItem(
//                    bookName = "History booked 2",
//                    date = "22/22/2211",
//                    time = "22:22"
//                )
//            )
//            add(
//                BookItem(
//                    bookName = "History booked 3",
//                    date = "33/33/3311",
//                    time = "33:33"
//                )
//            )
//            add(
//                BookItem(
//                    bookName = "History booked 4",
//                    date = "44/44/4411",
//                    time = "44:44"
//                )
//            )
//            add(
//                BookItem(
//                    bookName = "History booked 5",
//                    date = "55/55/5511",
//                    time = "55:55"
//                )
//            )
//        }
    }
}