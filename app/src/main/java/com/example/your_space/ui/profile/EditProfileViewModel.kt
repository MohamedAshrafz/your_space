package com.example.your_space.ui.profile

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.R
import com.example.your_space.database.UserDB
import com.example.your_space.network.networkdatamodel.UserPropertyUpdate
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

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

class EditProfileViewModel(val app: Application, val userId: String) : AndroidViewModel(app) {
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

    private val _snackbarText = MutableLiveData("")
    val snackbarText: LiveData<String>
        get() = _snackbarText

    private val _navigateBack = MutableLiveData(false)
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    fun clearNavigateBack() {
        _navigateBack.value = false
    }

    fun updateUserAndInformBackendAndDatabase() {
        val newUserData = UserPropertyUpdate(
            userId = user.value!!.userId,
            email = user.value!!.email,
            username = user.value!!.userName,

            firstName = firstName.value!!,
            lastName = lastName.value!!,
            mobileNo = mobileNo.value!!,
            address = address.value!!,
            birthDate = birthDate.value!!,
            bio = bio.value!!
        )
        var isSucceeded: Boolean
        viewModelScope.launch {
            isSucceeded = repository.updateUserData(newUserData, userId)
            if (isSucceeded) {
                _snackbarText.value = app.getString(R.string.Fields_updated_correctly)
                _navigateBack.value = true
            } else {
                _snackbarText.value = app.getString(R.string.sorry_we_could_not_update_your_data)
            }
        }
    }

    val savedDay = MutableLiveData(0)
    val savedMonth = MutableLiveData(0)
    val savedYear = MutableLiveData(0)

    fun setBirthDate(day: Int, month: Int, year: Int) {
        savedDay.value = day
        savedMonth.value = month
        savedYear.value = year

        val stringBirthDate = if (month < 10) {
            "$day-0$month-$year"
        } else {
            "$day-$month-$year"
        }
        _birthDate.value = stringBirthDate
    }
}