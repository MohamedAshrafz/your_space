package com.example.your_space.ui.authentication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.network.Network.NetworkServices
import com.example.your_space.network.networkdatamodel.UserPropertyPost
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class SignUpViewModel(app: Application) : AndroidViewModel(app) {
    val newUserData = MutableLiveData<UserPropertyPost>()

    val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String>
        get() = _firstName

    val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String>
        get() = _lastName

    val _password = MutableLiveData<String>()
    val password: LiveData<String>
        get() = _password

    val _mobileNo = MutableLiveData<String>()
    val mobileNo: LiveData<String>
        get() = _mobileNo

    val _address = MutableLiveData<String>()
    val address: LiveData<String>
        get() = _address

    val _birthDate = MutableLiveData<String>()
    val birthDate: LiveData<String>
        get() = _birthDate

    val _username = MutableLiveData<String>()
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

    fun allFieldsNotEmpty(): Boolean {
        return !email.value.isNullOrEmpty() && !firstName.value.isNullOrEmpty() &&
                !lastName.value.isNullOrEmpty() && !username.value.isNullOrEmpty() &&
                !password.value.isNullOrEmpty()
//                && !mobileNo.value.isNullOrEmpty() &&
//                !address.value.isNullOrEmpty() && !birthDate.value.isNullOrEmpty()
    }

    fun allFields(): String {
        return email.value + firstName.value + lastName.value +
                username.value + password.value + address.value
    }

    fun postNewUser(newUser: UserPropertyPost){
        viewModelScope.launch {
            val response: Response<ResponseBody>
            withContext(Dispatchers.IO) {
                response = NetworkServices.addNewUser(newUser)
            }
            if (response.isSuccessful) {
                _showSignedUpToast.value = "You successfully signed up"
            } else {
                _showSignedUpToast.value = "There was an error in the signing up"
            }
        }
    }

    fun clearSignedUpToast(){
        _showSignedUpToast.value = ""
    }
}