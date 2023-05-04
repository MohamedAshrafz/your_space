package com.example.your_space.ui.rooms

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.SpaceRoomDB
import com.example.your_space.database.WorkingSpaceDB
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

class RoomsViewModelFactory(
    private val application: Application,
    private val spaceId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomsViewModel::class.java)) {
            return RoomsViewModel(application, spaceId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RoomsViewModel(app: Application, private val spaceIdString: String) : AndroidViewModel(app) {
    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _roomsList = liveData { emitSource(repository.getRoomsBySpaceIdFromDB(spaceIdString)) }

    val roomsList: LiveData<List<SpaceRoomDB>>
        get() = _roomsList

    private val _selectedRoomItem = MutableLiveData<SpaceRoomDB?>()
    val selectedRoomItem: LiveData<SpaceRoomDB?>
        get() = _selectedRoomItem

    private val _imageString = MutableLiveData("http://192.168.1.12:8080/api/images/1")
    val imageString: LiveData<String>
        get() = _imageString


    private var _isSwipeRefreshing = MutableLiveData(false)
    val isSwipeRefreshing: LiveData<Boolean>
        get() = _isSwipeRefreshing


    fun onSelectRoomItem(roomItem: SpaceRoomDB) {
        _selectedRoomItem.value = roomItem
        //_navigateOnSelectedItem.value = true
    }

    fun clearSelectedItem() {
        // _navigateOnSelectedItem.value = false
        _selectedRoomItem.value = null
    }

    fun refreshOnSwipe() {
        viewModelScope.launch {
            _isSwipeRefreshing.value = true
            repository.getRoomsBySpaceIdFromNetwork(spaceIdString)
            _isSwipeRefreshing.value = false
        }
    }

}