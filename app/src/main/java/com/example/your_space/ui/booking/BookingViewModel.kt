package com.example.your_space.ui.booking

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.database.BookingDB
import com.example.your_space.database.SpaceRoomDB
import com.example.your_space.network.networkdatamodel.BookingPropertyPost
import com.example.your_space.repository.AppRepository
import com.example.your_space.ui.authentication.AuthenticationActivity
import kotlinx.coroutines.launch
import java.util.*

enum class RecyclerType {
    CURRENT,
    HISTORY
}

class BookingViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    private val _bookedList = repository.bookingsRepo
    val bookedList: LiveData<List<BookingDB>>
        get() = _bookedList

    private var posted = MutableLiveData(true)

    private val _userId = MutableLiveData("-1")
    val userId: LiveData<String>
        get() = _userId

    var minute = 0
    var hour = 0
    var day = 0
    var month = 0
    var year = 0

    var savedMinute = 0
    var savedHour = 0
    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedCorrectMonth = 0

    private var _bookedHistoryList = repository.historyBookingsRepo

    //    MutableLiveData(
//        listOf(
//            BookingDB(
//                bookingId = "8",
//                date = "2023-4-28",
//                startTime = "02:30",
//                endTime = "04:30"
//            ),
//            BookingDB(
//                bookingId = "9",
//                date = "2023-4-28",
//                startTime = "02:30",
//                endTime = "04:30"
//            ),
//            BookingDB(
//                bookingId = "10",
//                date = "2023-4-28",
//                startTime = "02:30",
//                endTime = "04:30"
//            ),
//            BookingDB(
//                bookingId = "11",
//                date = "2023-4-28",
//                startTime = "02:30",
//                endTime = "04:30"
//            ),
//            BookingDB(
//                bookingId = "12",
//                date = "2023-4-28",
//                startTime = "02:30",
//                endTime = "04:30"
//            )
//        )
//    )
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
//        fillHistoryList()



        viewModelScope.launch {
            //repository.deleteBooking()
            val sp = app.getSharedPreferences(AuthenticationActivity.LOGIN_STATE, MODE_PRIVATE)
            _userId.value = sp.getString(AuthenticationActivity.USER_ID, null)

            repository.refreshBookings(userId.value!!)
        }
    }

    fun setUserId(userId: String) {
        _userId.value = userId
    }

    fun getUserId() = _userId.value

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
//        _bookedHistoryList.postValue(_bookedHistoryList.value?.filter { it.bookingId != bookItem.bookingId })
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
            repository.refreshBookings(_userId.value!!)
            _isSwipeRefreshing.value = false
        }
    }

    fun isEmptyBook(bookItem: BookingDB): Boolean {
        if (bookItem.startTime.isEmpty() ||
            bookItem.date.isEmpty() ||
            bookItem.endTime.isEmpty()
        ) {
            return true
        }
        return false
    }


    fun addNewBookWithPoints(
        bookItem: BookingDB,
        roomItem: SpaceRoomDB,
        duration: String
    ): Boolean {
        var flag = false
        viewModelScope.launch {
            val user = _userId.value?.let { repository.getUserWithId(it) }
            if ((roomItem.price * duration.toInt()) < user!!.points) {
                val newBooking = getUserId()?.let {
                    BookingPropertyPost(
                        startTime = bookItem.startTime,
                        endTime = bookItem.endTime,
                        date = bookItem.date,
                        roomId = bookItem.roomId,
                        userId = it,
                        spaceName = repository.getSpaceWithSpaceId(bookItem.spaceId).name,
                        paymentMethod = "Points"
                    )
                }
                posted.value = newBooking?.let { repository.addNewBooking(it) }

                if (posted.value == true) {
                    Toast.makeText(
                        getApplication(),
                        "Booking Added Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    flag = true
                } else {
                    Toast.makeText(getApplication(), "Please Put Valid Data", Toast.LENGTH_SHORT)
                        .show()
                    flag = false
                }


            } else {
                Toast.makeText(getApplication(), "You Don't Have Enough Points", Toast.LENGTH_SHORT)
                    .show()
                flag = false
            }
        }
        return flag
    }


    fun addNewBook(bookItem: BookingDB) {
        viewModelScope.launch {

            val newBooking = getUserId()?.let {
                BookingPropertyPost(
                    startTime = bookItem.startTime,
                    endTime = bookItem.endTime,
                    date = bookItem.date,
                    roomId = bookItem.roomId,
                    userId = it,
                    spaceName = repository.getSpaceWithSpaceId(bookItem.spaceId).name,
                    paymentMethod = "Cash"
                )
            }
            posted.value = newBooking?.let { repository.addNewBooking(it) }
            if (posted.value == true) {
                Toast.makeText(getApplication(), "Booking Added Successfully", Toast.LENGTH_SHORT)
                    .show()
            } else Toast.makeText(getApplication(), "Please Put Valid Data", Toast.LENGTH_SHORT)
                .show()
        }
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

    fun getDateTimeCalender() {
        val cal = Calendar.getInstance()
        minute = cal.get(Calendar.MINUTE)
        hour = cal.get(Calendar.HOUR)
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

}
