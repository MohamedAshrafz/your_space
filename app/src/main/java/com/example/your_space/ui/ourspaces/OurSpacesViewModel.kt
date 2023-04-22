package com.example.your_space.ui.ourspaces

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

class OurSpacesViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _spacesList = repository.workingSpacesRepo
    val spacesList: LiveData<List<SpaceItem>>
        get() = _spacesList

    private var _spacesPageNumber = MutableLiveData(0)
    private val spacesPageNumber: LiveData<Int>
        get() = _spacesPageNumber

    private var _isWorkingSpacesPageLoading = MutableLiveData(false)
    val isWorkingSpacesPageLoading: LiveData<Boolean>
        get() = _isWorkingSpacesPageLoading

    private val _selectedSpaceItem = MutableLiveData<SpaceItem?>()
    val selectedSpaceItem: LiveData<SpaceItem?>
        get() = _selectedSpaceItem

    init {
        viewModelScope.launch {

            repository.loadWorkingSpacesOfPage(0)
        }
    }

    fun onSelectSpaceItem(spaceItem: SpaceItem) {
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
}