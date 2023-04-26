package com.example.your_space.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.AppDatabase
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.network.networkdatamodel.BookingProperty
import com.example.your_space.repository.AppRepository
import com.example.your_space.ui.booking.BookItem
import com.example.your_space.ui.homepage.HomeItem
import com.example.your_space.ui.ourspaces.SpaceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

enum class RecyclerType(s: String) {
    CURRENT("CURRENT"),
    HISTORY("HISTORY")
}

class AppViewModel(app: Application) : AndroidViewModel(app) {
    private val database = AppDatabase.getInstance(app).dao
    private val repository = AppRepository(database)

    private var _homeList = MutableLiveData(mutableListOf<HomeItem>())
    val homeList: LiveData<MutableList<HomeItem>>
        get() = _homeList

    private var _spacesList = repository.workingSpacesRepo
    val spacesList: LiveData<List<SpaceItem>>
        get() = _spacesList

    private var _bookedList = repository.BookingsRepo
    val bookedList: LiveData<List<BookItem>>
        get() = _bookedList

    private var _bookedHistoryList = MutableLiveData(mutableListOf<BookItem>())
    private var _testingString = MutableLiveData<String>()
    val testingString: LiveData<String>
        get() = _testingString

    private var _bookHistoryList = MutableLiveData(mutableListOf<BookItem>())
    val bookedHistoryList: LiveData<MutableList<BookItem>>
        get() = _bookedHistoryList

    val textTV = MutableLiveData<String>()

    val img = MutableLiveData<String>()

    private val _selectedSpaceItem = MutableLiveData<SpaceItem?>()
    val selectedSpaceItem: LiveData<SpaceItem?>
        get() = _selectedSpaceItem

    fun onSelectSpaceItem(spaceItem: SpaceItem) {
        _selectedSpaceItem.value = spaceItem
        //_navigateOnSelectedItem.value = true
    }

    fun clearSelectedItem() {
        // _navigateOnSelectedItem.value = false
        _selectedSpaceItem.value = null
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

    private val _showCancel = MutableLiveData(false)
    val showCancel: LiveData<Boolean>
        get() = _showCancel

    private val _showDelete = MutableLiveData(false)
    val showDelete: LiveData<Boolean>
        get() = _showDelete

    init {
        viewModelScope.launch {
            repository.deleteAllBookings()
            repository.deleteAllWorkingSpaces()
            repository.refreshWorkingSpaces()
            repository.refreshBookings()
            repository.refreshAllWorkingSpacesString()
            var stringVal: List<BookingProperty>
            withContext(Dispatchers.IO) {
                stringVal = repository.refreshAllBookingString()
            }
            _testingString.value = stringVal.toString()
        }

        val newList = mutableListOf<WorkingSpaceDB>()
        newList.apply {
            add(
                WorkingSpaceDB(
                    name = "Co-working space 1",
                    address = "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
                    contactNumber = "+01045423576",
                    rating = 4.8,
                    minPrice = 15.5,
                    description = "our co-working space is open most of the day and we have many offers to our new customers",
                    images = "@drawable/coworking"
                )
            )
            add(
                WorkingSpaceDB(
                    name = "Co-working space 2",
                    address = "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
                    contactNumber = "+01045423576",
                    rating = 4.8,
                    minPrice = 15.5,
                    description = "our co-working space is open most of the day and we have many offers to our new customers",
                    images = "@drawable/coworking"
                )
            )
            add(
                WorkingSpaceDB(
                    name = "Co-working space 3",
                    address = "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
                    contactNumber = "+01045423576",
                    rating = 4.8,
                    minPrice = 15.5,
                    description = "our co-working space is open most of the day and we have many offers to our new customers",
                    images = "@drawable/coworking"
                )
            )
            add(
                WorkingSpaceDB(
                    name = "Co-working space 4",
                    address = "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
                    contactNumber = "+01045423576",
                    rating = 4.8,
                    minPrice = 15.5,
                    description = "our co-working space is open most of the day and we have many offers to our new customers",
                    images = "@drawable/coworking"
                )
            )
            add(
                WorkingSpaceDB(
                    name = "Co-working space 5",
                    address = "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
                    contactNumber = "+01045423576",
                    rating = 4.8,
                    minPrice = 15.5,
                    description = "our co-working space is open most of the day and we have many offers to our new customers",
                    images = "@drawable/coworking"
                )
            )
        }
//        viewModelScope.launch {
//            repository.deleteAllWorkingSpaces()
//            repository.refreshWorkingSpaces()
//            repository.refreshBookings()
//
//        }
        fillList()


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

//        _bookedList.value ?.apply{
//
//            add(
//                BookItem(
//                   "11/11/1111",
//                    "11:11",
//                     "20:00"
//                )
//            )
//            add(
//                BookItem(
//                    "Booked 2",
//                    "22/22/2211",
//                    "22:22",
//                    "20:00"
//                )
//            )
//            add(
//                BookItem(
//                    "Booked 3",
//                    "33/33/3311",
//                    "33:33",
//                    "20:00"
//                )
//            )
//            add(
//                BookItem(
//                    "Booked 4",
//                    "44/44/4411",
//                    "44:44",
//                    "20:00"
//                )
//            )
//            add(
//                BookItem(
//                    "Booked 5",
//                    "55/55/5511",
//                    "55:55"
//                )
//            )
//
//        }


    }


    private fun fillList() {

        _homeList.value?.apply {
            add(HomeItem("go to our spaces", "@drawable/coworking"))
            add(HomeItem("Check your bookings", "@drawable/coworking"))
            add(HomeItem("Found the nearest Co-working spaces now", "@drawable/coworking"))
        }
    }
//        _spacesList.value?.apply {
//            add(
//                SpaceItem(
//                    "Co-working space 1",
//                    "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
//                    "+01045423576",
//                    "4.8",
//                    "15:25 per hour",
//                    "our co-working space is open most of the day and we have many offers to our new customers",
//                    "@drawable/coworking"
//                )
//            )
//            add(
//                SpaceItem(
//                    "Co-working space 2",
//                    "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
//                    "+01045423576",
//                    "4.8",
//                    "15:25 per hour",
//                    "our co-working space is open most of the day and we have many offers to our new customers",
//                    "@drawable/coworking"
//                )
//            )
//            add(
//                SpaceItem(
//                    "Co-working space 3",
//                    "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
//                    "+01045423576",
//                    "4.8",
//                    "15:25 per hour",
//                    "our co-working space is open most of the day and we have many offers to our new customers",
//                    "@drawable/coworking"
//                )
//            )
//            add(
//                SpaceItem(
//                    "Co-working space 4",
//                    "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
//                    "+01045423576",
//                    "4.8",
//                    "15:25 per hour",
//                    "our co-working space is open most of the day and we have many offers to our new customers",
//                    "@drawable/coworking"
//                )
//            )
//            add(
//                SpaceItem(
//                    "Co-working space 5",
//                    "1 El Sarayat St.، ABBASSEYA, El Weili, Cairo Governorate 11535",
//                    "+01045423576",
//                    "4.8",
//                    "15:25 per hour",
//                    "our co-working space is open most of the day and we have many offers to our new customers",
//                    "@drawable/coworking"
//                )
//            )
//        }


    fun isEmptySpace(spaceItem: SpaceItem): Boolean {
        if (spaceItem.spaceName.isEmpty() ||
            spaceItem.mobile.isEmpty() ||
            spaceItem.description.isEmpty() ||
            spaceItem.location.isEmpty()
        ) {
            return true
        }
        return false
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
}
