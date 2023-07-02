package com.willor.sentinel_v2.presentation.uoa.uoa_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.willor.lib_data.domain.dataobjs.UoaFilterOptions


@Composable
fun UoaColumnHeaderRow(
    uoaScreenStateProvider: () -> UoaScreenState,
) {
    val sortedBy = uoaScreenStateProvider().sortBy
    val screenWidth = LocalConfiguration.current.screenWidthDp
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {

        Spacer(modifier = Modifier.width(15.dp))

        Text(
            text = "Ticker",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            modifier = Modifier.width(75.dp),
        )

        Text(
            text = "Expiry",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            modifier = Modifier.width(88.dp),
        )

        Text(
            text = "Type",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            modifier = Modifier.width(54.dp),
        )

        Text(
            text = "Strike",
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            modifier = Modifier.width(70.dp),
        )

        Text(
            text = formatUoaFilterOptionName(sortedBy),
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            modifier = Modifier.width(70.dp),
        )

    }
}


private fun formatUoaFilterOptionName(uoaFilter: UoaFilterOptions): String {
    return when (uoaFilter) {
        UoaFilterOptions.OTM_Percentage -> {
            "OTM %"
        }
        UoaFilterOptions.Implied_Volatility -> {
            "IV"
        }
        UoaFilterOptions.Volume_OI_Ratio -> {
            "Vol/OI"
        }
        UoaFilterOptions.Open_Int -> {
            "OI"
        }

        // The following 3 are part of the base columns, so no need to display it in xtra column 5
        UoaFilterOptions.Expiry -> {
            ""
        }
        UoaFilterOptions.Type -> {
            ""
        }
        UoaFilterOptions.Strike -> {
            ""
        }
        else -> {
            uoaFilter.name.replace('_', ' ')
        }
    }
}


















