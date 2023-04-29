package com.example.your_space.ui.ourspaces


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.network.networkdatamodel.BookingProperty
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OurSpacesViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _spacesList = repository.workingSpacesRepo
    val spacesList: LiveData<List<WorkingSpaceDB>>
        get() = _spacesList

    private var _spacesPageNumber = MutableLiveData(0)
    private val spacesPageNumber: LiveData<Int>
        get() = _spacesPageNumber

    private var _isPageLoading = MutableLiveData(false)
    val isPageLoading: LiveData<Boolean>
        get() = _isPageLoading

    private var _isSwipeRefreshing = MutableLiveData(false)
    val isSwipeRefreshing: LiveData<Boolean>
        get() = _isSwipeRefreshing

    private val _selectedSpaceItem = MutableLiveData<WorkingSpaceDB?>()
    val selectedSpaceItem: LiveData<WorkingSpaceDB?>
        get() = _selectedSpaceItem

    private var _testingString = MutableLiveData<String>()
    val testingString: LiveData<String>
        get() = _testingString


    init {
        viewModelScope.launch {

            repository.refreshBookings()
            repository.refreshAllWorkingSpacesString()
            var stringVal: List<BookingProperty>
            stringVal = repository.refreshAllBookingString()

            _testingString.value = stringVal.toString()

            repository.loadWorkingSpacesOfPage(0, true)
        }
    }

    fun onSelectSpaceItem(spaceItem: WorkingSpaceDB) {
        _selectedSpaceItem.value = spaceItem
        //_navigateOnSelectedItem.value = true
    }

    fun clearSelectedItem() {
        // _navigateOnSelectedItem.value = false
        _selectedSpaceItem.value = null
    }

    fun loadMoreWorkingSpaces() {
        viewModelScope.launch {
            _isPageLoading.value = true
            spacesPageNumber.value?.let { repository.loadWorkingSpacesOfPage(it, false) }
            _spacesList.value?.let { _spacesPageNumber.value = it.size / PAGE_SIZE }
            _isPageLoading.value = false
        }
    }

    fun refreshOnSwipe() {
        viewModelScope.launch {
            _isSwipeRefreshing.value = true
            _spacesPageNumber.value = 0
            spacesPageNumber.value?.let { repository.loadWorkingSpacesOfPage(it, true) }
            _isSwipeRefreshing.value = false
        }
    }

    fun isEmptySpace(spaceItem: WorkingSpaceDB): Boolean {
        if (spaceItem.name.isEmpty() ||
            spaceItem.contactNumber.isEmpty() ||
            spaceItem.description.isEmpty() ||
            spaceItem.address.isEmpty()
        ) {
            return true
        }
        return false
    }
}