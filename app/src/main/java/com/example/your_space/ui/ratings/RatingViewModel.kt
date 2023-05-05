package com.example.your_space.ui.ratings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope

import com.example.your_space.database.RatingDB

import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

class RatingViewModel (app: Application) : AndroidViewModel(app){
    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _ratingsList = repository.ratingsRepo
    val ratingsList: LiveData<List<RatingDB>>
        get() = _ratingsList

    init {
        viewModelScope.launch {
            repository.getRatingsBySpaceIdFromNetwork()
        }
    }

}