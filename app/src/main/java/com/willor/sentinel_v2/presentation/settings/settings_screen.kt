package com.willor.sentinel_v2.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.sentinel_v2.presentation.common.NavDrawer
import com.willor.sentinel_v2.presentation.common.Screens
import com.willor.sentinel_v2.presentation.common.navController
import com.willor.sentinel_v2.presentation.settings.SettingsViewModel



@Destination
@Composable
fun SettingsScreen(
    navigator: DestinationsNavigator,
    viewModel: SettingsViewModel = hiltViewModel()
){

    SettingsContent(
        navDrawerDestinationClicked = { navController(navigator, it) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    navDrawerDestinationClicked: (Screens) -> Unit
){

    val contentScrollState = rememberScrollState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {},
        bottomBar = {},             // TODO Might not use this
        floatingActionButton = {},
        drawerContent = {
            NavDrawer(
                currentDestination = Screens.Settings,
                destinationClicked = { navDrawerDestinationClicked(it) }
            )
        },
        drawerGesturesEnabled = true
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(contentScrollState)
        ){

            Text("Settings Screen")

            Button(
                onClick = { }
            ){
                Text("Click Me")
            }

        }               // --- Root Column Content Scope End
    }                       // --- Scaffold Scope End
}