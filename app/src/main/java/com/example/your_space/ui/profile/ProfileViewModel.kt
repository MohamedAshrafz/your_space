package com.example.your_space.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.UserDB
import com.example.your_space.repository.AppRepository

class ProfileViewModelFactory(
    private val application: Application,
    private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class ProfileViewModel(app: Application, val userId: String) : AndroidViewModel(app) {
    private val repository = AppRepository.getInstance(app.applicationContext)

    private val _user: LiveData<UserDB> =
        liveData { emit(repository.getUserWithId(userId)) }

    val email: LiveData<String> = _user.map { it.email }

//    val firstName = _user.map { it.firstName }
//
//    val lastName = _user.map { it.lastName }

    val mobileNo = _user.map { it.mobileNo }

    val address = _user.map { it.address }

    val birthDate = _user.map { it.birthDate }

    val username = _user.map { it.userName }

    val bio = _user.map { it.bio }

    val points = _user.map { it.points }

    fun namingFunction(): LiveData<String> = _user.map { it.firstName + " " + it.lastName }
}