package com.gws.ussd.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gws.networking.repository.UssdRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val ussdRepository: UssdRepository
) : ViewModel() {


    private var loginLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val login: LiveData<Boolean> = loginLiveData
    fun login(login: String, password: String) {
        viewModelScope.launch {
            ussdRepository.login(login, password).collect { result ->
                loginLiveData.value = result
            }
        }
    }

}
