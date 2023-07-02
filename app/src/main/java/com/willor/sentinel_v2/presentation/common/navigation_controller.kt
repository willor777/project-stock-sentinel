package com.willor.sentinel_v2.presentation.common

import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.willor.sentinel_v2.presentation.destinations.*


fun navController(
    navigator: DestinationsNavigator,
    destination: Screens,
    ticker: String? = null
){
    when (destination){
        Screens.Login -> {
            navigator.navigate(LoginScreenDestination)
        }
        Screens.Dashboard -> {
            navigator.navigate(DashboardScreenDestination)
        }
        Screens.Settings -> {
            navigator.navigate(SettingsScreenDestination)
        }
        Screens.Quotes -> {
            navigator.navigate(QuoteScreenDestination(ticker = ticker))
        }
        Screens.UnusualOptionsActivity -> {
            navigator.navigate(UoaScreenDestination)
        }
        Screens.Scanner -> {
            navigator.navigate(ScannerScreenDestination)
        }
        Screens.AnalystsChanges -> {
            navigator.navigate(AnalystsChangesScreenDestination)
        }
    }

}


enum class Screens(val displayName: String){
    Login("Login"),
    Dashboard("Dashboard"),
    Settings("Settings"),
    Quotes("Quotes"),
    UnusualOptionsActivity("Unusual Option Activity"),
    Scanner("Scanner"),
    AnalystsChanges("Analysts Changes")
}

