package com.willor.sentinel_v2.presentation.scanner.scanner_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.entities.TriggerEntity
import com.willor.sentinel_v2.ui.theme.MySizes


@Composable
fun TriggersList(
    uiStateProvider: () -> ScannerUiState,
){
    uiStateProvider().triggers.takeIf { it.isNotEmpty() }?.let{triggers ->

        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_MEDIUM))
            ColumnLabelsRow()
            Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_MEDIUM))
            Divider()
            Spacer(modifier = Modifier.height(MySizes.VERTICAL_CONTENT_PADDING_MEDIUM))
            TriggersLazyCol(triggers = triggers)

        }

    }

}


@Composable
private fun ColumnLabelsRow(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        Text(
            text = "Ticker",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM)
        )


        Text(
            text = "Time",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM)
        )


        Text(
            text = "Signal",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = MySizes.HORIZONTAL_CONTENT_PADDING_MEDIUM)
        )
    }
}


@Composable
private fun TriggersLazyCol(
    triggers: List<TriggerEntity>
){
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
    ){
        items(triggers.size){i ->
            TriggerItem(
                trigger = triggers[i],
                searchTickerClick = {
                    // TODO
                }
                )
        }
    }
}