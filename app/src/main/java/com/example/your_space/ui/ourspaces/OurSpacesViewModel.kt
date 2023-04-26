package com.example.your_space.ui.ourspaces


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

class OurSpacesViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _spacesList = repository.workingSpacesRepo
    val spacesList: LiveData<List<WorkingSpaceDB>>
        get() = _spacesList

    private var _spacesPageNumber = MutableLiveData(0)
    private val spacesPageNumber: LiveData<Int>
        get() = _spacesPageNumber

    private var _isWorkingSpacesPageLoading = MutableLiveData(false)
    val isWorkingSpacesPageLoading: LiveData<Boolean>
        get() = _isWorkingSpacesPageLoading

    private val _selectedSpaceItem = MutableLiveData<WorkingSpaceDB?>()
    val selectedSpaceItem: LiveData<WorkingSpaceDB?>
        get() = _selectedSpaceItem

    init {
        viewModelScope.launch {

            repository.loadWorkingSpacesOfPage(0)
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
            _isWorkingSpacesPageLoading.value = true
            spacesPageNumber.value?.let { repository.loadWorkingSpacesOfPage(it) }
            _spacesList.value?.let { _spacesPageNumber.value = it.size /3 }
            _isWorkingSpacesPageLoading.value = false
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