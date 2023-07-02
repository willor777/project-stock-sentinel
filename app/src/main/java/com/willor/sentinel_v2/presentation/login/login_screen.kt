package com.willor.sentinel_v2.presentation.login

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.sentinel_v2.R
import com.willor.sentinel_v2.presentation.common.*
import com.willor.sentinel_v2.ui.theme.MySizes
import com.willor.sentinel_v2.utils.showToast


/*
Idea for this screen...
Have a splash screen animation running with a transparent login form on top of it
 */
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
@RootNavGraph(start = true)
fun LoginScreen(
    navigator: DestinationsNavigator,
    viewModel: LoginViewModel = hiltViewModel()
) {

    val screenState = viewModel.uiState.collectAsState()
    val isRegistering = screenState.value.isRegistering

    LoginScreenContent(
        isRegistering,
        uiStateProvider = { screenState.value },
        navigationCalled = {
            navController(navigator, it)
        },
        onUiEvent = viewModel::handleUiEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LoginScreenContent(
    isRegistering: Boolean,
    uiStateProvider: () -> LoginUiState,
    navigationCalled: (Screens) -> Unit,
    onUiEvent: (LoginUiEvents) -> Unit,

    ) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    val coroutineScope = rememberCoroutineScope()
    val showContentOne = mutableStateOf(uiStateProvider().isRegistering)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            // TODO
        },
        bottomBar = {},             // TODO Might not use this
        floatingActionButton = {},
        drawerContent = {
            NavDrawer(
                currentDestination = Screens.Dashboard,
                destinationClicked = { navigationCalled(it) }
            )
        },
        drawerGesturesEnabled = true
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            LottieBackgroundAnimationLoop()

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(15.dp)),
            ) {

                AnimatedContent(
                    targetState = isRegistering,
                ) { ir ->

                    if (ir) {
                        RegisterContent(uiStateProvider = uiStateProvider, onUiEvent = onUiEvent)
                    } else {
                        LoginContent(uiStateProvider = uiStateProvider, onUiEvent = onUiEvent,
                            navigateAfterSuccessfulLogin = { navigationCalled(Screens.Dashboard) })
                    }
                }
            }
        }
    }
}

/* TODO
*   - When "or register..." link is clicked, the LoginContent should exit slide left and the
*   RegisterContent should enter slide left
*   - More info is needed when Login Error happens
* */
@Composable
fun LoginContent(
    uiStateProvider: () -> LoginUiState,
    onUiEvent: (LoginUiEvents) -> Unit,
    navigateAfterSuccessfulLogin: () -> Unit
) {

    val curContext = LocalContext.current
    val uiState = uiStateProvider()
    val pw = if (uiState.loginViewPassword) {
        uiState.loginPasswordTextActual
    } else {
        uiState.loginPasswordTextDisplayed
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xDDffffff))
            .border(Dp.Hairline, Color.Black)
            .padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = "Please Login",
            style = MaterialTheme.typography.titleLarge
        )
//        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))
        Text(
            "or register...",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Blue,
            modifier = Modifier.clickable {
                onUiEvent(LoginUiEvents.GoToRegisterClicked)
            }
        )

        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE * 2))

        // Email Text Field
        OutlinedTextField(
            value = uiStateProvider().loginEmailTextEditText,
            onValueChange = {
                onUiEvent(LoginUiEvents.LoginEmailTextChanged(it))
            },
            label = { Text("Email: ") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Blue,
                unfocusedLabelColor = Color.Blue,
                focusedLabelColor = Color.Blue,
            ),
        )

        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

        // Password Text Field
        OutlinedTextField(
            value = pw,
            onValueChange = {
                onUiEvent(LoginUiEvents.LoginPasswordTextChanged(it))
            },
            label = { Text("Password: ") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onUiEvent(LoginUiEvents.LoginViewPasswordClicked)
                    }
                ) {
                    Icon(Icons.Filled.Visibility, "view-password", tint = Color.Black)
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE * 2))


        Button(
            onClick = {
                onUiEvent(
                    LoginUiEvents.LoginSubmitClicked(
                        onSuccess = {
                            showToast(
                                message = "Login Successful!",
                                contextProvider = { curContext }
                            )
                            navigateAfterSuccessfulLogin()
                        },
                        onError = {
                            showToast(
                                message = "Login Failed!",
                                contextProvider = { curContext }
                            )
                        },
                        onLoading = {
                            // Not really using the Loading state right now, it's' fast so not needed
                            showToast(
                                message = "Login Requested",
                                contextProvider = { curContext }
                            )
                        }
                    )
                )
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Color.Blue.copy(blue = .5f))
        ) {
            Text("Submit", color = Color.White)
        }
    }


}


/* TODO
*   - When "or register..." link is clicked, the LoginContent should exit slide left and the
*   RegisterContent should enter slide left
*   - onError lambda function below (on RegistrationSubmit) needs info about why it failed
* */
@Composable
fun RegisterContent(
    uiStateProvider: () -> LoginUiState,
    onUiEvent: (LoginUiEvents) -> Unit
) {
    val curContext = LocalContext.current
    val uiState = uiStateProvider()
    val pw = if (uiState.registerViewPassword) {
        uiState.registerPasswordTextActual
    } else {
        uiState.registerPasswordTextDisplayed
    }
    val cpw = if (uiState.registerViewConfirmPassword) {
        uiState.registerConfirmPasswordTextActual
    } else {
        uiState.registerConfirmPasswordTextDisplayed
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = .02f))
            .border(Dp.Hairline, Color.Black)
            .padding(MySizes.HORIZONTAL_EDGE_PADDING, MySizes.VERTICAL_EDGE_PADDING),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Text(
            text = "Please Register",
            style = MaterialTheme.typography.titleLarge
        )
//        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))
        Text(
            "or login...",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Blue,
            modifier = Modifier.clickable {
                onUiEvent(LoginUiEvents.GoToLoginClicked)
            }
        )

        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_SMALL))

        // Email Text Field
        OutlinedTextField(
            value = uiStateProvider().registerEmailText,
            onValueChange = {
                onUiEvent(LoginUiEvents.RegisterEmailTextChanged(it))
            },
            label = { Text("Email: ") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Blue,
                unfocusedLabelColor = Color.Blue,
                focusedLabelColor = Color.Blue,
            ),
        )

        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_SMALL))

        // Password Text Field
        OutlinedTextField(
            value = pw,
            onValueChange = {
                onUiEvent(LoginUiEvents.RegisterPasswordTextChanged(it))
            },
            label = { Text("Password: ") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onUiEvent(LoginUiEvents.RegisterViewPasswordClicked)
                    }
                ) {
                    Icon(Icons.Filled.Visibility, "view-password", tint = Color.Black)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Blue,
                unfocusedLabelColor = Color.Blue,
                focusedLabelColor = Color.Blue,
            ),
        )

        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_SMALL))

        // Password Confirm Text Field
        OutlinedTextField(
            value = cpw,
            onValueChange = {
                onUiEvent(LoginUiEvents.RegisterPasswordConfirmationTextChanged(it))
            },
            label = { Text("Confirm Password: ") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        onUiEvent(LoginUiEvents.RegisterViewConfirmPasswordClicked)
                    }
                ) {
                    Icon(Icons.Filled.Visibility, "view-password", tint = Color.Black)
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.Blue,
                unfocusedBorderColor = Color.Blue,
                unfocusedLabelColor = Color.Blue,
                focusedLabelColor = Color.Blue,
            ),
        )
        Spacer(Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_SMALL * 2))


        Button(
            onClick = {
                onUiEvent(
                    LoginUiEvents.RegisterSubmitClicked(
                        onSuccess = {
                            showToast(
                                message = "Registration Successful",
                                contextProvider = { curContext }
                            )
                        },
                        onLoading = {

                        },
                        onError = {
                            showToast(
                                message = "Login Failed!",        // TODO More Info For User On Fail
                                contextProvider = { curContext }
                            )
                        }
                    )
                )
            },
            shape = RoundedCornerShape(5.dp),
            colors = ButtonDefaults.buttonColors(Color.Blue.copy(blue = .5f))
        ) {
            Text("Submit", color = Color.White)
        }
    }
}

@Composable
fun LottieBackgroundAnimationLoop() {

    var isPlaying by remember {
        mutableStateOf(true)
    }

    var speed by remember {
        mutableStateOf(.5f)
    }

    // remember lottie composition, which
    // accepts the lottie composition result
    val composition by rememberLottieComposition(

        LottieCompositionSpec
            // here `code` is the file name of lottie file
            // use it accordingly
            .RawRes(R.raw.potion2)
    )

    // to control the animation
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying,
        speed = speed,
        restartOnPlay = false
    )

    LottieAnimation(
        modifier = Modifier
            .fillMaxHeight(.4f)
            .fillMaxWidth(),
        composition = composition, progress = { progress })
}





