package com.example.your_space.ui.authentication

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.R
import com.example.your_space.database.UserDB
import com.example.your_space.repository.AppRepository
import kotlinx.coroutines.launch

enum class GET_USER_RESPONCE {
    NO_SUCH_USER,
    WRONG_PASSWORD,
    OKEY
}

class SignInViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    val signed = MutableLiveData(false)

    val navigateToMainActivity = MutableLiveData(false)

    fun clearSigned() {
        signed.value = false
    }

    fun clearNavigate() {
        navigateToMainActivity.value = false
    }

    private val _currentUser = MutableLiveData<UserDB>()
    val currentUser: LiveData<UserDB>
        get() = _currentUser

    private val _showSignedInToast = MutableLiveData("")
    val showSignedUpToast: LiveData<String>
        get() = _showSignedInToast

    private suspend fun getUser(): UserDB? {
        if (username.value != null && password.value != null) {
            _currentUser.value =
                repository.loginAndGetTokenForUserWith(username.value!!, password.value!!)
        }
        return currentUser.value
    }

//    fun reGetTokenAndUser(userId: String) {
//        var currentUser: UserDB? = null
//        viewModelScope.launch {
//            currentUser = repository.updateTokenForUserWithUserId(userId)
//            if (currentUser != null) {
//                navigateToMainActivity.value = true
//            }
//        }
//    }

    fun checkNotEmpty(): Boolean {
        return !username.value.isNullOrEmpty() &&
                !password.value.isNullOrEmpty()
    }

    fun runSignInFlow() {
        viewModelScope.launch {
            val user = getUser()
            if (user == null) {
                _showSignedInToast.value =
                    app.applicationContext.getString(R.string.connection_error)
            } else if (user.userId == "-1") {
                _showSignedInToast.value =
                    app.applicationContext.getString(R.string.wrong_username_or_password)
            } else {
                _showSignedInToast.value =
                    app.applicationContext.getString(R.string.you_have_successfully_logged_in)
                signed.value = true
            }
        }
    }

    val _password = MutableLiveData("")
    val password: LiveData<String>
        get() = _password

    val _username = MutableLiveData("")
    val username: LiveData<String>
        get() = _username

    private val _loginButtonPressed = MutableLiveData(false)
    val loginButtonPressed: LiveData<Boolean>
        get() = _loginButtonPressed

    fun setLoginButtonPressed(){
        _loginButtonPressed.value = true
    }

    fun clearLoginButtonPressed(){
        _loginButtonPressed.value = false
    }
}