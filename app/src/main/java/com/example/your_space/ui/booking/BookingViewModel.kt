package com.example.your_space.ui.booking

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

enum class RecyclerType {
    CURRENT,
    HISTORY
}

class BookingViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    private val _bookedList = repository.BookingsRepo
    val bookedList: LiveData<List<BookItem>>
        get() = _bookedList

    private var _bookedHistoryList = MutableLiveData(mutableListOf<BookItem>())
    val bookedHistoryList: LiveData<MutableList<BookItem>>
        get() = _bookedHistoryList

    private val _showCancel = MutableLiveData(false)
    val showCancel: LiveData<Boolean>
        get() = _showCancel

    private val _showDelete = MutableLiveData(false)
    val showDelete: LiveData<Boolean>
        get() = _showDelete

    init {
        fillHistoryList()

        viewModelScope.launch {
            //repository.deleteBooking()

            repository.refreshBookings()
        }
    }

    fun onCancelBookedItem(bookItem: BookItem) {
        viewModelScope.launch {
            repository.deleteBookingWithId(bookItem)
        }
    }

    fun clearCancelBookedItem() {
        _showCancel.value = false
    }

    fun onDeleteBookedItem(bookItem: BookItem) {
        _bookedHistoryList.value!!.remove(bookItem)
        _showDelete.value = true
    }

    fun clearDeleteBookedItem() {
        _showDelete.value = false
    }

    fun getAppropriateRecyclerView(recyclerType: String): LiveData<MutableList<BookItem>> {
        return when (recyclerType) {
            RecyclerType.CURRENT.name -> bookedList.map { it.toMutableList() }
            else -> bookedHistoryList
        }
    }

    fun isEmptyBook(bookItem: BookItem): Boolean {
        if (bookItem.bookName.isEmpty() ||
            bookItem.date.isEmpty() ||
            bookItem.time.isEmpty()
        ) {
            return true
        }
        return false
    }


    fun addNewBook(bookItem: BookItem) {
        _bookedList.value?.toMutableList()?.add(
            bookItem
        )
    }

    private fun fillHistoryList() {
        _bookedHistoryList.value?.apply {
            add(
                BookItem(
                    bookName = "History booked 1",
                    date = "11/11/1111",
                    time = "11:11"
                )
            )
            add(
                BookItem(
                    bookName = "History booked 2",
                    date = "22/22/2211",
                    time = "22:22"
                )
            )
            add(
                BookItem(
                    bookName = "History booked 3",
                    date = "33/33/3311",
                    time = "33:33"
                )
            )
            add(
                BookItem(
                    bookName = "History booked 4",
                    date = "44/44/4411",
                    time = "44:44"
                )
            )
            add(
                BookItem(
                    bookName = "History booked 5",
                    date = "55/55/5511",
                    time = "55:55"
                )
            )
        }
    }
}