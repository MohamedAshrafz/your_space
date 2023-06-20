package com.example.your_space.ui.authentication

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.database.UserDB
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

enum class GET_USER_RESPONCE {
    NO_SUCH_USER,
    WRONG_PASSWORD,
    OKEY
}

class SignInViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    val signed = MutableLiveData(false)

    private val _currentUser = MutableLiveData<UserDB>()
    val currentUser: LiveData<UserDB>
        get() = _currentUser

    private val _showSignedInToast = MutableLiveData("")
    val showSignedUpToast: LiveData<String>
        get() = _showSignedInToast

    private suspend fun getUser(): Boolean {
        if (username.value != null && password.value != null) {
            _currentUser.value =
                repository.getUserWithUserNameAndPassword(username.value!!, password.value!!)
        }
        return currentUser.value != null
    }

    fun reGetTokenAndUser(userId: String): UserDB? {
        var currentUser: UserDB? = null
        viewModelScope.launch {
            currentUser =  repository.getUserWithUserId(userId)
        }
        return currentUser
    }

    fun checkNotEmpty(): Boolean {
        return !username.value.isNullOrEmpty() &&
                !password.value.isNullOrEmpty()
    }

    fun runSignInFlow() {
        viewModelScope.launch {
            if (getUser()) {
                _showSignedInToast.value = "You have successfully logged in"
                signed.value = true
            } else {
                _showSignedInToast.value = "Wrong username or password"
            }
        }
    }

    val _password = MutableLiveData("")
    val password: LiveData<String>
        get() = _password

    val _username = MutableLiveData("")
    val username: LiveData<String>
        get() = _username
}