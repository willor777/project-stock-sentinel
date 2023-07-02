package com.willor.sentinel_v2.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.willor.lib_data.data.local.prefs.UserPreferences
import com.willor.lib_data.domain.dataobjs.DataResourceState
import com.willor.lib_data.domain.usecases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usecases: UseCases
) : ViewModel() {

    private val tag = LoginViewModel::class.java.simpleName

    // This doesn't really need to be a stateflow, It is only used here in the view model
    private val _userPrefs = MutableStateFlow(UserPreferences())
    private val userPrefs get() = _userPrefs.asStateFlow()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState get() = _uiState.asStateFlow()

    fun handleUiEvent(event: LoginUiEvents) {
        when (event) {
            is LoginUiEvents.InitialLoad -> {
                initialLoad()
            }
            is LoginUiEvents.LoginEmailTextChanged -> {
                updateLoginEmailText(event.text)
            }
            is LoginUiEvents.LoginPasswordTextChanged -> {
                updateLoginPasswordText(event.text)
            }
            is LoginUiEvents.LoginViewPasswordClicked -> {
                updateLoginViewPassword()
            }
            is LoginUiEvents.LoginSubmitClicked -> {
                loginUser(
                    onSuccess = event.onSuccess,
                    onError = event.onError,
                    onLoading = event.onLoading
                )
            }
            is LoginUiEvents.GoToRegisterClicked -> {
                updateShowRegister()
            }
            is LoginUiEvents.RegisterEmailTextChanged -> {
                updateRegisterEmailText(event.text)
            }
            is LoginUiEvents.RegisterPasswordTextChanged -> {
                updateRegisterPasswordText(event.text)
            }
            is LoginUiEvents.RegisterPasswordConfirmationTextChanged -> {
                updateRegisterConfirmPasswordText(event.text)
            }
            is LoginUiEvents.RegisterViewPasswordClicked -> {
                updateRegisterViewPassword()
            }
            is LoginUiEvents.RegisterViewConfirmPasswordClicked -> {
                updateRegisterViewConfirmPassword()
            }
            is LoginUiEvents.RegisterSubmitClicked -> {
                registerUser(
                    onLoading = event.onLoading,
                    onSuccess = event.onSuccess,
                    onError = event.onError
                )
            }
            is LoginUiEvents.GoToLoginClicked -> {
                updateShowLogin()
            }
        }
    }

    /**
     * Loads User Prefs
     */
    private fun initialLoad() {

        viewModelScope.launch {

            // Load user prefs
            usecases.getUserPreferencesUsecase().collectLatest {
                when (it){
                    is DataResourceState.Success -> {
                        _userPrefs.value = it.data

                        if (it.data.username.isNotEmpty()){
                            _uiState.update { state ->
                                state.copy(loginEmailTextEditText = it.data.username)
                            }
                        }
                    }
                    else -> {
                        Log.d(tag, "initialLoad() Failed to load user prefs")
                    }
                }
            }
        }
    }


    /*
    * TODO
    *   - Default username + password has been added below for easier development
    * */
    private fun loginUser(onSuccess: () -> Unit, onError: () -> Unit, onLoading: () -> Unit){
        viewModelScope.launch(Dispatchers.IO) {

            // TODO Un-comment this out when in production Mode
//            usecases.loginUsecase(
//                _uiState.value.loginEmailTextEditText,
//                _uiState.value.loginPasswordTextActual
//            ).collectLatest {

//            // TODO And comment this out
            usecases.loginUsecase(
                username = "scoobydoo33",
                password = "Qwerty11!"
            ).collectLatest {


            when (it) {
                    is DataResourceState.Success -> {
                        // Save api key + expiry
                        val curPrefs = _userPrefs.value
                        curPrefs.apiKey = it.data.token
                        curPrefs.apiKeyExpiry = it.data.expiresAt
                        usecases.saveUserPreferencesUsecase(curPrefs)

                        // Call the onSuccess callback
                        viewModelScope.launch(Dispatchers.Main){
                            onSuccess()
                        }
                    }
                    is DataResourceState.Loading -> {
                        viewModelScope.launch(Dispatchers.Main){
                            onLoading()
                        }
                    }
                    else -> {
                        viewModelScope.launch(Dispatchers.Main){
                            onError()
                        }
                    }
                }
            }
        }
    }

    /**
     * Registers user + on success: Saves user name to userprefs + changes to login
     */
    private fun registerUser(onSuccess: () -> Unit, onError: () -> Unit, onLoading: () -> Unit){
        // Check the fields (verify username and password)

        // Submit data
        viewModelScope.launch(Dispatchers.IO){
            usecases.registerUsecase(
                _uiState.value.registerEmailText,
                _uiState.value.registerPasswordTextActual
            ).collectLatest {
                    when (it){
                        is DataResourceState.Success -> {
                            _uiState.update  { state ->
                                state.copy(
                                    loginEmailTextEditText = _uiState.value.registerEmailText,
                                    isRegistering = false           // This should switch it to Login
                                )
                            }

                            // Update user prefs
                            val cur = _userPrefs.value
                            cur.username = _uiState.value.registerEmailText
                            usecases.saveUserPreferencesUsecase(cur)

                            viewModelScope.launch(Dispatchers.Main) { onSuccess() }
                        }
                        is DataResourceState.Loading -> {
                            viewModelScope.launch(Dispatchers.Main) { onLoading() }
                        }
                        is DataResourceState.Error -> {
                            viewModelScope.launch(Dispatchers.Main) { onError() }
                        }
                    }
            }

        }




    }

    private fun updateRegisterViewPassword() {
        _uiState.update {
            it.copy(registerViewPassword = !it.registerViewPassword)
        }
    }

    private fun updateRegisterViewConfirmPassword() {
        _uiState.update {
            it.copy(registerViewConfirmPassword = !it.registerViewConfirmPassword)
        }
    }

    private fun updateRegisterEmailText(txt: String) {
        _uiState.update {
            it.copy(registerEmailText = txt)
        }
    }

    private fun updateRegisterPasswordText(txt: String) {
        var dispText: String = ""
        var actualText = _uiState.value.registerPasswordTextActual

        // If user added a character, add that character only to ActualText
        if (txt.length > actualText.length) {
            actualText += txt[txt.lastIndex]

            // Build display text with leading *'s and last letter visible
            dispText = ""
            actualText.forEachIndexed { index, c ->
                if (index == actualText.lastIndex) {
                    dispText += c
                } else {
                    dispText += "*"
                }
            }
        }
        // If user deleted a character
        else if (txt.length < actualText.length) {
            actualText = actualText.dropLast(1)
            dispText = ""
            // Build display text
            actualText.forEachIndexed { index, c ->
                if (index == actualText.lastIndex) {
                    dispText += c
                } else {
                    dispText += "*"
                }
            }
        }

        _uiState.update {
            it.copy(
                registerPasswordTextActual = actualText,
                registerPasswordTextDisplayed = dispText
            )
        }
    }

    private fun updateRegisterConfirmPasswordText(txt: String) {
        var dispText: String = ""
        var actualText = _uiState.value.registerConfirmPasswordTextActual

        // If user added a character, add that character only to ActualText
        if (txt.length > actualText.length) {
            actualText += txt[txt.lastIndex]

            // Build display text with leading *'s and last letter visible
            dispText = ""
            actualText.forEachIndexed { index, c ->
                if (index == actualText.lastIndex) {
                    dispText += c
                } else {
                    dispText += "*"
                }
            }
        }
        // If user deleted a character
        else if (txt.length < actualText.length) {
            actualText = actualText.dropLast(1)
            dispText = ""
            // Build display text
            actualText.forEachIndexed { index, c ->
                if (index == actualText.lastIndex) {
                    dispText += c
                } else {
                    dispText += "*"
                }
            }
        }

        _uiState.update {
            it.copy(
                registerConfirmPasswordTextActual = actualText,
                registerConfirmPasswordTextDisplayed = dispText,
            )
        }
    }

    /**
     * Update Login Form Email Text
     */
    private fun updateLoginEmailText(txt: String) {
        _uiState.update {
            it.copy(loginEmailTextEditText = txt)
        }
    }

    /**
     * Update Login Form Password Text
     */
    private fun updateLoginPasswordText(txt: String) {
        var dispText: String = ""
        var actualText = _uiState.value.loginPasswordTextActual

        // If user added a character, add that character only to ActualText
        if (txt.length > actualText.length) {
            actualText += txt[txt.lastIndex]

            // Build display text with leading *'s and last letter visible
            dispText = ""
            actualText.forEachIndexed { index, c ->
                if (index == actualText.lastIndex) {
                    dispText += c
                } else {
                    dispText += "*"
                }
            }
        }
        // If user deleted a character
        else if (txt.length < actualText.length) {
            actualText = actualText.dropLast(1)
            dispText = ""
            // Build display text
            actualText.forEachIndexed { index, c ->
                if (index == actualText.lastIndex) {
                    dispText += c
                } else {
                    dispText += "*"
                }
            }
        }

        _uiState.update {
            it.copy(loginPasswordTextActual = actualText, loginPasswordTextDisplayed = dispText)
        }
    }

    /**
     * In Login Screen, Show Password instead of *'s
     */
    private fun updateLoginViewPassword() {
        _uiState.update {
            it.copy(loginViewPassword = !it.loginViewPassword)
        }
    }

    /**
     * Show Registration Form
     */
    private fun updateShowRegister() {
        Log.d(tag, "Register Clicked")
        _uiState.update {
            it.copy(isRegistering = true)
        }
    }

    /**
     * Show Login Form
     */
    private fun updateShowLogin() {
        _uiState.update {
            it.copy(isRegistering = false)
        }
    }
}

























