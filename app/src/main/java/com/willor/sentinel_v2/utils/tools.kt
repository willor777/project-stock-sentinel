package com.willor.sentinel_v2.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.willor.sentinel_v2.ui.theme.GainGreen
import com.willor.sentinel_v2.ui.theme.LossRed
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun formatChangeDollarAndChangePct(
    changeDollar: Double,
    changePct: Double
): String{

    val plusMinus = if (changeDollar > 0){
        "+"
    }else{
        "-"
    }

    // Example "+ $1.20 ( %0.01 )"
    return "$plusMinus $${formatDoubleToTwoDecimalPlaceString(changeDollar)}" +
            " ( %${formatDoubleToTwoDecimalPlaceString(changePct)} )"

}


fun formatDoubleToTwoDecimalPlaceString(d: Double): String{
    if (d.isInfinite()){
        return "0.00"
    }

    val strDouble = d.toString().split(".")

    val whole = strDouble[0]

    val dec = if (strDouble[1].length > 2){
        strDouble[1].substring(0, 2)
    }else{
        strDouble[1].padEnd(2, '0')
    }
    return "$whole.$dec"
}



fun formatWatchlistNameForDisplay(wlName: String): String {
    val wlNameSplit = wlName.lowercase().split("_")
    val titledNames = mutableListOf<String>()
    wlNameSplit.forEach {
        titledNames.add(it.replaceFirstChar { character ->
            character.uppercase()
        }
        )
    }
    return titledNames.joinToString(" ")
}


fun formatIntegerToString(i: Int): String{
    return NumberFormat.getIntegerInstance().format(i)
}


fun formatDateToStringMMDDYYYY(d: String): String{
    val stageOneFormat = SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a")
    val date = stageOneFormat.parse(d)

    val sdf = SimpleDateFormat("MM/dd/yyyy")
    return sdf.format(date!!)
}


fun formatTimestampToStringDateWithTime(ts: Long): String{
    val sdf = SimpleDateFormat("MM-dd-yyyy @ hh:mm:ss a")
    val d = Date(ts)

    return sdf.format(d)
}


fun determineGainLossColor(d: Double): Color {
    return if (d > 0){
        GainGreen
    }else{
        LossRed
    }
}


fun calculateRatio(curValue: Double, avgValue: Double): Double{
    if (avgValue == 0.0){
        return curValue
    }
    return curValue / avgValue
}


// TODO Doesn't work
fun isStockMarketOpen(): Boolean {
    return false
}



fun dateStringFromTimestamp(ts: Long): String{
    val sdf = SimpleDateFormat("MMM-dd-yyyy hh:mm:ss a")
    return sdf.format(Date(ts))
}




fun showToast(
    message: String,
    contextProvider: () -> Context,
    toastLength: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(contextProvider(), message, toastLength).show()
}






















