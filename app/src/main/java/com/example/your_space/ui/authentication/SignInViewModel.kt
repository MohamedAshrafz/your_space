package com.example.your_space.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.R
import com.example.your_space.database.UserDB
import com.example.your_space.repository.AppRepository
import com.example.your_space.plugins.AnimationDialogPlugin
import kotlinx.coroutines.launch

enum class GET_USER_RESPONCE {
    NO_SUCH_USER,
    WRONG_PASSWORD,
    OKEY
}

enum class LOGIN_RETURN_TYPE(val stringResource: Int) {
    NO_SUCH_USER(R.string.wrong_username_or_password),
    CONNECTION_FAILED(R.string.connection_error),
    SUCCESS(R.string.login_success)
}

class SignInViewModel(val app: Application) : AndroidViewModel(app) {

    private val repository = AppRepository.getInstance(app.applicationContext)

    val navigateToMainActivity = MutableLiveData(false)

    val loginResponseEvent = MutableLiveData(Pair<Boolean, LOGIN_RETURN_TYPE?>(false, null))

    fun clearNavigate() {
        navigateToMainActivity.value = false
    }

    fun clearLoginResponseEvent() {
        loginResponseEvent.value = Pair(false, null)
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
        AnimationDialogPlugin.getInstance().show("Login Progressing...")
        viewModelScope.launch {
            val user = getUser()
            AnimationDialogPlugin.getInstance().dismiss()
            if (user == null) {
                loginResponseEvent.postValue(Pair(true, LOGIN_RETURN_TYPE.CONNECTION_FAILED))
            } else if (user.userId == "-1") {
                loginResponseEvent.postValue(Pair(true, LOGIN_RETURN_TYPE.NO_SUCH_USER))
            } else {
                loginResponseEvent.postValue(Pair(true, LOGIN_RETURN_TYPE.SUCCESS))
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

    fun setLoginButtonPressed() {
        _loginButtonPressed.value = true
    }

    fun clearLoginButtonPressed() {
        _loginButtonPressed.value = false
    }
}