package com.example.your_space.ui.ourspaces


import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.network.PAGE_SIZE
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

class OurSpacesViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    var _switchKey = MutableLiveData(0)
    val switchKey: LiveData<Int>
        get() = _switchKey

    var _searchText = MutableLiveData("")
    val searchText: LiveData<String>
        get() = _searchText

    private var _spacesList = switchKey.switchMap { switchKey ->
        var returnedRV = repository.workingSpacesRepo
        when (switchKey) {
            0 -> {
                returnedRV = repository.workingSpacesRepo
            }
            1 -> {
                returnedRV = repository.workingSpacesRepoOrderByRatings
            }
            2 -> {
                returnedRV = repository.workingSpacesRepoSearchBy(searchText.value!!)
            }
        }
        returnedRV
    }
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

    private val _imageString = MutableLiveData("http://192.168.1.12:8080/api/images/1")
    val imageString: LiveData<String>
        get() = _imageString

    init {
        viewModelScope.launch {

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