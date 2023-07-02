package com.willor.sentinel_v2.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.willor.sentinel_v2.ui.theme.MySizes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun NavDrawer(
    currentDestination: Screens,
    destinationClicked: (Screens) -> Unit,
){
    val currentDestinationDisplayname = currentDestination.displayName
    val allDestinations = mutableListOf<String>().apply {
        this.add(currentDestinationDisplayname)
        Screens.values().forEach {
            if (it != currentDestination){ this.add(it.displayName) }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(MySizes.HORIZONTAL_EDGE_PADDING,
            MySizes.VERTICAL_EDGE_PADDING),
        verticalArrangement = Arrangement.Center
    ){
        allDestinations.forEach{ destinationDisplayName ->

            val textColor = if (destinationDisplayName == currentDestinationDisplayname){
                Color.Blue
            }else{
                Color.Black
            }
            val fontSize = if (destinationDisplayName == currentDestinationDisplayname){
                MaterialTheme.typography.titleLarge.fontSize
            }else{
                MaterialTheme.typography.bodyLarge.fontSize
            }
            Text(
                modifier = Modifier.fillMaxWidth().clickable {
                        destinationClicked(
                            Screens.values().find {
                                it.displayName == destinationDisplayName
                            }!!
                        )
                    },
                text = destinationDisplayName,
                fontSize = fontSize,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = MaterialTheme.typography.bodyLarge.fontFamily,
                fontStyle = MaterialTheme.typography.bodyLarge.fontStyle,
                color = textColor
            )

            Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_LARGE))

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
fun openNavDrawer(
    coroutineScope: CoroutineScope,
    drawerState: DrawerState
) = coroutineScope.launch {
    drawerState.open()
}








