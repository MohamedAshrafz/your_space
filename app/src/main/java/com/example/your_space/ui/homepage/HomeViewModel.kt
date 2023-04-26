package com.example.your_space.ui.homepage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.your_space.repository.AppRepository

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    private var _homeList = MutableLiveData(mutableListOf<HomeItem>())
    val homeList: LiveData<MutableList<HomeItem>>
        get() = _homeList

    init {
        fillHomeList()
    }

    private fun fillHomeList() {
        _homeList.value?.apply {
            add(HomeItem("go to our spaces", "@drawable/coworking"))
            add(HomeItem("Check your bookings", "@drawable/coworking"))
            add(HomeItem("Found the nearest Co-working spaces now", "@drawable/coworking"))
        }
    }
}