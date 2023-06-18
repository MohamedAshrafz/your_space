package com.example.your_space.ui.authentication

import android.app.Application
import androidx.lifecycle.*
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.UserPropertyPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class SignUpViewModel(app: Application) : AndroidViewModel(app) {
    val newUserData = MutableLiveData<UserPropertyPost>()

    val _email = MutableLiveData<String>("")
    val email: LiveData<String>
        get() = _email

    val _firstName = MutableLiveData("")
    val firstName: LiveData<String>
        get() = _firstName

    val _lastName = MutableLiveData("")
    val lastName: LiveData<String>
        get() = _lastName

    val _password = MutableLiveData("")
    val password: LiveData<String>
        get() = _password

    val _rePassword = MutableLiveData("")
    val rePassword: LiveData<String>
        get() = _rePassword

    val _mobileNo = MutableLiveData("")
    val mobileNo: LiveData<String>
        get() = _mobileNo

    val _address = MutableLiveData("")
    val address: LiveData<String>
        get() = _address

    val _birthDate = MutableLiveData("")
    val birthDate: LiveData<String>
        get() = _birthDate

    val _username = MutableLiveData("")
    val username: LiveData<String>
        get() = _username

    val _signInStatus = MutableLiveData(false)
    val signInStatus: LiveData<Boolean>
        get() = _signInStatus

    private val _showSignedUpToast = MutableLiveData("")
    val showSignedUpToast: LiveData<String>
        get() = _showSignedUpToast

    fun setSignedIn() {
        _signInStatus.value = true
    }

    fun setSignedOut() {
        _signInStatus.value = false
    }

    private fun isAnyFieldEmpty(): Boolean {
        return email.value.isNullOrEmpty() || firstName.value.isNullOrEmpty() ||
                lastName.value.isNullOrEmpty() || username.value.isNullOrEmpty() ||
                password.value.isNullOrEmpty() || mobileNo.value.isNullOrEmpty() ||
                address.value.isNullOrEmpty() || birthDate.value.isNullOrEmpty()
    }

    fun allFields(): String {
        return email.value + firstName.value + lastName.value +
                username.value + password.value + address.value
    }

    fun postNewUser() {
        if (isAnyFieldEmpty()) {
            _showSignedUpToast.value = "Please fill all fields"
            return
        }
        if (!isValidPassword()) {
            _showSignedUpToast.value =
                "The password must contain 8 characters at lease, upper, lower letters and numbers"
            return
        }
        if (!isValidEmail()) {
            _showSignedUpToast.value = "Please enter a valid mail"
            return
        }
        if (!checkPasswordMatching()) {
            _showSignedUpToast.value = "Confirm Password does not match the password"
            return
        }

        val newUser = UserPropertyPost(
            email = email.value ?: "",
            firstName = firstName.value ?: "",
            lastName = lastName.value ?: "",
            password = password.value ?: "",
            mobileNo = mobileNo.value ?: "",
            address = address.value ?: "",
            birthDate = birthDate.value ?: "",
            username = username.value ?: ""
        )

        viewModelScope.launch {
            val response: Response<ResponseBody>
            withContext(Dispatchers.IO) {
                response = NetworkServices.addNewUser(newUser)
            }
            if (response.isSuccessful) {
                _showSignedUpToast.value = "You have successfully signed up"
            } else {
                _showSignedUpToast.value = "This email is already used"
            }
        }
    }

    fun checkPasswordMatching(): Boolean {
        return password.value == rePassword.value
    }

    fun isValidPassword(): Boolean {
        return Regex("[0-9]+").containsMatchIn(password.value ?: "") &&
                Regex(".{8,}").containsMatchIn(password.value ?: "") &&
                Regex("[a-z]+").containsMatchIn(password.value ?: "") &&
                Regex("[A-Z]+").containsMatchIn(password.value ?: "")
    }

    fun isValidEmail(): Boolean {
        return Regex("^.*[@].*[.].*$").containsMatchIn(email.value ?: "")
    }

    fun clearSignedUpToast() {
        _showSignedUpToast.value = ""
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