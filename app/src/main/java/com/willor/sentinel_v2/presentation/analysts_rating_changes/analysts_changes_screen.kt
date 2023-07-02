package com.willor.sentinel_v2.presentation.analysts_rating_changes

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Composable
@Destination
fun AnalystsChangesScreen(
    navigator: DestinationsNavigator,
    viewModel: AnalystsChangesViewModel = hiltViewModel()
){

    Text("Analysts Changes Screen")
}