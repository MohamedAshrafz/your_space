package com.example.your_space.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.your_space.ui.homepage.HomeItem
import com.example.your_space.ui.ourspaces.SpaceItem

class ViewModel : ViewModel() {
    private var _homeList = MutableLiveData(mutableListOf<HomeItem>())
    val homeList: LiveData<MutableList<HomeItem>>
        get() = _homeList

    private var _spacesList = MutableLiveData(mutableListOf<SpaceItem>())
    val spacesList: LiveData<MutableList<SpaceItem>>
        get() = _spacesList

    val textTV = MutableLiveData<String>()

    val img = MutableLiveData<String>()

    private val _selectedSpaceItem = MutableLiveData<SpaceItem?>()
    val selectedSpaceItem: LiveData<SpaceItem?>
        get() = _selectedSpaceItem

    fun onSelectSpaceItem(spaceItem: SpaceItem) {
        _selectedSpaceItem.value = spaceItem
    }


    init {
        fillList()
    }

    private fun fillList() {
        _homeList.value?.apply {
            add(HomeItem("go to our spaces","@drawable/coworking"))
            add(HomeItem("go to our spaces","@drawable/coworking"))
            add(HomeItem("go to our spaces","@drawable/coworking"))
        }
        _spacesList.value?.apply {
            add(SpaceItem("Co-working space 1","this is co-working space 1","@drawable/coworking"))
            add(SpaceItem("Co-working space 2","this is co-working space 2","@drawable/coworking"))
            add(SpaceItem("Co-working space 3","this is co-working space 3","@drawable/coworking"))
            add(SpaceItem("Co-working space 4","this is co-working space 4","@drawable/coworking"))
            add(SpaceItem("Co-working space 5","this is co-working space 5","@drawable/coworking"))
        }
    }
}