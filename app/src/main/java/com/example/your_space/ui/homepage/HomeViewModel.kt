package com.example.your_space.ui.homepage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    private var _list = MutableLiveData(mutableListOf<HomeItem>())
    val list: LiveData<MutableList<HomeItem>>
        get() = _list

    val textTV = MutableLiveData<String>()

    val img = MutableLiveData<String>()


    init {
        fillList()
    }

    private fun fillList() {
        _list.value?.apply {
            add(HomeItem("go to our spaces","@drawable/coworking"))
            add(HomeItem("go to our spaces","@drawable/coworking"))
            add(HomeItem("go to our spaces","@drawable/coworking"))
        }
    }
}