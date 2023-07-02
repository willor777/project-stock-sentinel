package com.willor.sentinel_v2.presentation.login

data class LoginUiState(
    val loginEmailTextEditText: String = "",
    val loginPasswordTextActual: String = "",
    val loginPasswordTextDisplayed: String = "",
    val loginViewPassword: Boolean = false,
    val registerEmailText: String = "",
    val registerPasswordTextActual: String = "",
    val registerPasswordTextDisplayed: String = "",
    val registerViewPassword: Boolean = false,
    val registerConfirmPasswordTextActual: String = "",
    val registerConfirmPasswordTextDisplayed: String = "",
    val registerViewConfirmPassword: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isRegistering: Boolean = false
    )
