package com.willor.sentinel_v2.presentation.login


sealed interface LoginUiEvents {

    object InitialLoad: LoginUiEvents

    // Login
    data class LoginEmailTextChanged(val text: String): LoginUiEvents
    data class LoginPasswordTextChanged(val text: String): LoginUiEvents
    object LoginViewPasswordClicked: LoginUiEvents
    class LoginSubmitClicked(
        val onSuccess: () -> Unit, val onError: () -> Unit, val onLoading: () -> Unit
    ): LoginUiEvents
    object GoToRegisterClicked: LoginUiEvents

    // Register
    data class RegisterEmailTextChanged(val text: String): LoginUiEvents
    data class RegisterPasswordTextChanged(val text: String): LoginUiEvents
    data class RegisterPasswordConfirmationTextChanged(val text: String): LoginUiEvents
    class RegisterSubmitClicked(
        val onSuccess: () -> Unit, val onError: () -> Unit, val onLoading: () -> Unit
    ): LoginUiEvents
    object RegisterViewPasswordClicked: LoginUiEvents
    object RegisterViewConfirmPasswordClicked: LoginUiEvents
    object GoToLoginClicked: LoginUiEvents

}