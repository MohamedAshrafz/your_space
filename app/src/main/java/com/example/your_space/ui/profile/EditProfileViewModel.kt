package com.example.your_space.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.UserDB
import com.example.your_space.repository.AppRepository

class EditProfileViewModelFactory(
    private val application: Application,
    private val userId: String
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class EditProfileViewModel(app: Application, val userId: String) : AndroidViewModel(app) {
    private val repository = AppRepository.getInstance(app.applicationContext)

    private val _user: LiveData<UserDB> =
        liveData { emit(repository.getUserWithId(userId)) }
    val user: LiveData<UserDB>
        get() = _user

    val _firstName = MutableLiveData("")
    val firstName: LiveData<String>
        get() = _firstName

    val _lastName = MutableLiveData("")
    val lastName: LiveData<String>
        get() = _lastName

    val _mobileNo = MutableLiveData("")
    val mobileNo: LiveData<String>
        get() = _mobileNo

    val _address = MutableLiveData("")
    val address: LiveData<String>
        get() = _address

    val _birthDate = MutableLiveData("")
    val birthDate: LiveData<String>
        get() = _birthDate

    val _bio = MutableLiveData("")
    val bio: LiveData<String>
        get() = _bio

}