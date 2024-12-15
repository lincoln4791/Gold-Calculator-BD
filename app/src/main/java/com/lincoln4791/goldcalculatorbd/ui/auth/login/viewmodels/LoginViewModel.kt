package com.lincoln4791.goldcalculatorbd.ui.auth.login.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginRequest
import com.lincoln4791.goldcalculatorbd.ui.auth.login.models.LoginResponse
import com.lincoln4791.goldcalculatorbd.ui.auth.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<Result<LoginResponse>?>(null)
    val uiState: StateFlow<Result<LoginResponse>?> = _uiState

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _uiState.value = repository.login(loginRequest)
        }
    }
}
