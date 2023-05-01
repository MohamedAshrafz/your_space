package com.example.your_space.ui.rooms

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.database.SpaceRoomDB
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

class RoomsViewModel(app: Application) : AndroidViewModel(app) {
    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _roomsList = repository.roomsRepo
    val roomsList: LiveData<List<SpaceRoomDB>>
        get() = _roomsList

    private val _selectedRoomItem = MutableLiveData<SpaceRoomDB?>()
    val selectedRoomItem: LiveData<SpaceRoomDB?>
        get() = _selectedRoomItem

    private val _imageString = MutableLiveData("http://192.168.1.12:8080/api/images/1")
    val imageString: LiveData<String>
        get() = _imageString



    init {
        viewModelScope.launch {

            repository.refreshRooms()
        }
    }

    fun onSelectRoomItem(roomItem: SpaceRoomDB) {
        _selectedRoomItem.value = roomItem
        //_navigateOnSelectedItem.value = true
    }

    fun clearSelectedItem() {
        // _navigateOnSelectedItem.value = false
        _selectedRoomItem.value = null
    }


}