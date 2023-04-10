package com.example.your_space.repository

import com.example.your_space.database.AppDao
import com.example.your_space.database.WorkingSpaceDB

class AppRepository(private val database: AppDao) {

    fun getWorkingSpaces() {

    }

    fun refreshWorkingSpaces(vararg list: List<WorkingSpaceDB>) {
        AppDao.insertAll(*(list.toList().toTypedArray()))
    }
}