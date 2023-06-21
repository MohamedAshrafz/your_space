package com.example.your_space.ui.messages

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.your_space.repository.AppRepository
import com.example.your_space.ui.authentication.AuthenticationActivity
import kotlinx.coroutines.launch

class MessagesViewModel(val app: Application) : AndroidViewModel(app) {
    private val repository = AppRepository.getInstance(app.applicationContext)

    val _messageField = MutableLiveData("")
    val messageField: LiveData<String>
        get() = _messageField

    private val _showSnackbarMessage = MutableLiveData("")
    val showSnackbarMessage: LiveData<String>
        get() = _showSnackbarMessage

    fun clearShowSnackbarMessage(){
        _showSnackbarMessage.value = ""
    }

    fun sentMessage() {

        val sp = app.getSharedPreferences(AuthenticationActivity.LOGIN_STATE, MODE_PRIVATE)
        val userId = sp.getString(AuthenticationActivity.USER_ID, null)

        if (!messageField.value.isNullOrEmpty() && userId != null) {

            viewModelScope.launch {
                val isSucceeded = repository.sendMessage(messageField.value ?: "", userId)
                if (isSucceeded){
                    _showSnackbarMessage.value = "We received your message"
                    _messageField.value = ""
                }else{
                    _showSnackbarMessage.value = "Sorry we couldn't sent the message"
                }
            }
        }
    }
}