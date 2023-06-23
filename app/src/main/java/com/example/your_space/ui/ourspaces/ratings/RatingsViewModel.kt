package com.example.your_space.ui.ourspaces.ratings

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.RatingsDB
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

class RatingsViewModelFactory(
    private val application: Application,
    private val spaceId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RatingsViewModel::class.java)) {
            return RatingsViewModel(application, spaceId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class RatingsViewModel(app: Application, private val spaceIdString: String) : AndroidViewModel(app) {
    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _ratingsList =
        liveData { emitSource(repository.getRatingsBySpaceIdFromDB(spaceIdString)) }

    val ratingsList: LiveData<List<RatingsDB>>
        get() = _ratingsList

    init {
        viewModelScope.launch {
            repository.getRatingsBySpaceIdFromNetwork(spaceIdString)
        }
    }
}