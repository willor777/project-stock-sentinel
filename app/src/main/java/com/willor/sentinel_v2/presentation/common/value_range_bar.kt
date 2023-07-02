package com.willor.sentinel_v2.presentation.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ValueRangeBar(
    settings: ValueRangeBarSettings
) {


    val convertedSettings = settings.extractValues()


    Column(
        modifier = convertedSettings.modifier
    ){

        // Top row (Current Value)
        if (convertedSettings.showTopCurValue){
            TopRowCurrentValue(
                curValue = convertedSettings.curValue,
                labelFontSize = convertedSettings.labelFontSize,
                fontFamily = convertedSettings.fontFamily,
                fontColor = convertedSettings.fontColor
            )
        }

        // Range Bar Row (Endcap - Range Bar - Endcap
        MiddleRowRangeBarWithEndcaps(
            lowLabel = convertedSettings.lowLabelText,
            lowValue = convertedSettings.lowValueText,
            highLabel = convertedSettings.highLabelText,
            highValue = convertedSettings.highValueText,
            labelFontSize = convertedSettings.labelFontSize,
            valueFontSize = convertedSettings.valueFontSize,
            fontColor = convertedSettings.fontColor,
            fontFamily = convertedSettings.fontFamily,
            barHeight = convertedSettings.barHeight,
            lowSideColor = convertedSettings.lowColor,
            highSideColor = convertedSettings.highColor,
            sliderPosition = convertedSettings.sliderPosition
        )

        // Bottom row (Midpoint + Line)
        if (convertedSettings.showMidPoint){
            BottomRowMidPoint(
                midValue = convertedSettings.midPoint,
                labelFontSize = convertedSettings.labelFontSize,
                fontFamily = convertedSettings.fontFamily,
                fontColor = convertedSettings.fontColor
            )
        }
    }
}


@Composable
fun TopRowCurrentValue(
    curValue: String,
    labelFontSize: TextUnit,
    fontFamily: FontFamily,
    fontColor: Color,
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ){
        Text(
            "Current: ${formatValue(curValue)}",
            fontSize = labelFontSize,
            fontFamily = fontFamily,
            color = fontColor,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun MiddleRowRangeBarWithEndcaps(
    lowLabel: String,
    lowValue: String,
    highLabel: String,
    highValue: String,
    labelFontSize: TextUnit,
    valueFontSize: TextUnit,
    fontColor: Color,
    fontFamily: FontFamily,
    barHeight: Dp,
    lowSideColor: Color,
    highSideColor: Color,
    sliderPosition: Float,
){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom
    ){

        // Low end cap
        LabelValueEndcap(
            label = lowLabel,
            value = lowValue,
            labelFontSize = labelFontSize,
            valueFontSize = valueFontSize,
            fontFamily = fontFamily,
            fontColor = fontColor
        )

        // Range bar
        RangeBar(
            sliderPosition = sliderPosition,
            highSideColor = highSideColor,
            lowSideColor = lowSideColor,
            barHeight = barHeight
        )

        // High end cap
        LabelValueEndcap(
            label = highLabel,
            value = highValue,
            labelFontSize = labelFontSize,
            valueFontSize = valueFontSize,
            fontFamily = fontFamily,
            fontColor = fontColor
        )
    }

}


@Composable
fun BottomRowMidPoint(
    midValue: String,
    labelFontSize: TextUnit,
    fontFamily: FontFamily,
    fontColor: Color,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            "|",
            fontWeight = FontWeight.ExtraBold,
            fontSize = labelFontSize,
            color = fontColor
        )

        Text(
            "Mid Point: ${formatValue(midValue)}",
            fontFamily = fontFamily,
            fontSize = labelFontSize,
            color = fontColor,
            fontWeight = FontWeight.Bold
        )

    }

}


@Composable
private fun RangeBar(
    sliderPosition: Float,
    highSideColor: Color,
    lowSideColor: Color,
    barHeight: Dp,
){

    LinearProgressIndicator(
        progress = sliderPosition,
        color = lowSideColor,
        backgroundColor = highSideColor,
        modifier = Modifier
            .fillMaxWidth(.8f)      // Leave room for end caps
            .height(barHeight)            // NOTE this was 15.dp originally
    )

}


/**
 * Creates a Column of Text composables. Label on top with a Value on bottom. Both are aligned to the bottom
 */
@Composable
private fun LabelValueEndcap(
    label: String,
    value: String,
    labelFontSize: TextUnit,
    valueFontSize: TextUnit,
    fontFamily: FontFamily,
    fontColor: Color,
){

    Column(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Bottom
//                .height(maxHeight)
    ){

        // Label
        Text(
            label,
            fontSize = labelFontSize,
            fontFamily = fontFamily,
            color = fontColor,
            fontWeight = FontWeight.Bold
        )

        // Value
        Text(
            formatValue(value),
            fontSize =  valueFontSize,
            fontFamily = fontFamily,
            color = fontColor,
            fontWeight = FontWeight.Bold
        )
    }
}


/**
 * Settings for the ValueRangeBar
 */
@Suppress("unused")
sealed class  ValueRangeBarSettings{
    data class DoubleValueRange(
        val modifier: Modifier,
        val highValue: Double,
        val highLabel: String,
        val curValue: Double,
        val lowValue: Double,
        val lowLabel: String,
        val barHeight: Dp = 15.dp,
        val highSideColor: Color = Color.DarkGray,
        val lowSideColor: Color = Color.Green,
        val valueFontSize: TextUnit = 12.sp,
        val labelFontSize: TextUnit = 10.sp,
        val fontColor: Color = Color.Black,
        val fontFamily: FontFamily = FontFamily.Default,
        val showTopCurValue: Boolean = true,
        val showMidPoint: Boolean = true
    ): ValueRangeBarSettings()


    data class IntValueRange(
        val modifier: Modifier,
        val highValue: Int,
        val highLabel: String,
        val curValue: Int,
        val lowValue: Int,
        val lowLabel: String,
        val barHeight: Dp = 15.dp,
        val highSideColor: Color = Color.DarkGray,
        val lowSideColor: Color = Color.Green,
        val valueFontSize: TextUnit = 12.sp,
        val labelFontSize: TextUnit = 10.sp,
        val fontColor: Color = Color.Black,
        val fontFamily: FontFamily = FontFamily.Default,
        val showTopCurValue: Boolean = true,
        val showMidPoint: Boolean = true
    ): ValueRangeBarSettings()


    data class LongValueRange(
        val modifier: Modifier,
        val highValue: Long,
        val highLabel: String,
        val curValue: Long,
        val lowValue: Long,
        val lowLabel: String,
        val barHeight: Dp = 15.dp,
        val highSideColor: Color = Color.DarkGray,
        val lowSideColor: Color = Color.Green,
        val valueFontSize: TextUnit = 12.sp,
        val labelFontSize: TextUnit = 10.sp,
        val fontColor: Color = Color.Black,
        val fontFamily: FontFamily = FontFamily.Default,
        val showTopCurValue: Boolean = true,
        val showMidPoint: Boolean = true
    ): ValueRangeBarSettings()
}


/**
 * ValueRangeBarSettings converted to String format no matter if they are Int, Long, or Double.
 */
private data class ConvertedRangeValues(
    val modifier: Modifier,
    val lowValueText: String,
    val lowLabelText: String,
    val curValue: String,
    val highValueText: String,
    val highLabelText: String,
    val barHeight: Dp,
    val highColor: Color,
    val lowColor: Color,
    val valueFontSize: TextUnit,
    val labelFontSize: TextUnit,
    val fontColor: Color,
    val fontFamily: FontFamily,
    val showTopCurValue: Boolean,
    val showMidPoint: Boolean,
    val midPoint: String,
    val sliderPosition: Float,
)


/**
 * Used to convert all subclasses of the ValueRangeBarSettings to Uniform format.
 */
private fun ValueRangeBarSettings.extractValues(): ConvertedRangeValues{
    val settings = this
    return when(settings){
        is ValueRangeBarSettings.IntValueRange -> {
            ConvertedRangeValues(
                modifier = settings.modifier,
                lowValueText = settings.lowValue.toString(),
                lowLabelText = settings.lowLabel,
                curValue = settings.curValue.toString(),
                highValueText = settings.highValue.toString(),
                highLabelText = settings.highLabel,
                barHeight = settings.barHeight,
                highColor = settings.highSideColor,
                lowColor = settings.lowSideColor,
                valueFontSize = settings.valueFontSize,
                labelFontSize = settings.labelFontSize,
                fontColor = settings.fontColor,
                fontFamily = settings.fontFamily,
                showTopCurValue = settings.showTopCurValue,
                showMidPoint = settings.showMidPoint,
                midPoint = (
                        ((settings.highValue - settings.lowValue) / 2) + settings.lowValue
                        ).toString(),
                sliderPosition = (
                        (settings.curValue - settings.lowValue).toDouble() /
                                (settings.highValue - settings.lowValue).toDouble()
                        ).toFloat()
            )
        }

        is ValueRangeBarSettings.LongValueRange -> {
            ConvertedRangeValues(
                modifier = settings.modifier,
                lowValueText = settings.lowValue.toString(),
                lowLabelText = settings.lowLabel,
                curValue = settings.curValue.toString(),
                highValueText = settings.highValue.toString(),
                highLabelText = settings.highLabel,
                barHeight = settings.barHeight,
                highColor = settings.highSideColor,
                lowColor = settings.lowSideColor,
                valueFontSize = settings.valueFontSize,
                labelFontSize = settings.labelFontSize,
                fontColor = settings.fontColor,
                fontFamily = settings.fontFamily,
                showTopCurValue = settings.showTopCurValue,
                showMidPoint = settings.showMidPoint,
                midPoint = (
                        ((settings.highValue - settings.lowValue) / 2) + settings.lowValue
                        ).toString(),
                sliderPosition = (
                        (settings.curValue - settings.lowValue).toDouble() /
                                (settings.highValue - settings.lowValue).toDouble()
                        ).toFloat()
            )
        }

        is ValueRangeBarSettings.DoubleValueRange -> {
            ConvertedRangeValues(
                modifier = settings.modifier,
                lowValueText = settings.lowValue.toString(),
                lowLabelText = settings.lowLabel,
                curValue = settings.curValue.toString(),
                highValueText = settings.highValue.toString(),
                highLabelText = settings.highLabel,
                barHeight = settings.barHeight,
                highColor = settings.highSideColor,
                lowColor = settings.lowSideColor,
                valueFontSize = settings.valueFontSize,
                labelFontSize = settings.labelFontSize,
                fontColor = settings.fontColor,
                fontFamily = settings.fontFamily,
                showTopCurValue = settings.showTopCurValue,
                showMidPoint = settings.showMidPoint,
                midPoint = (
                        ((settings.highValue - settings.lowValue) / 2) + settings.lowValue
                        ).toString(),
                sliderPosition = (
                        (settings.curValue - settings.lowValue).toDouble() /
                                (settings.highValue - settings.lowValue).toDouble()
                        ).toFloat()
            )
        }
    }
}


/**
 * This fun is mainly for Double values. It just simply returns all others as they are.
 * For Doubles, it will return a 2 decimal place string, padded with 0's if needed.
 */
private fun formatValue(s: String): String{
    if (!s.contains(".")){
        return s
    }

    val splt = s.split(".")

    val whole = splt[0]
    val dec = splt[1]

    val formatedDec = if(dec.length > 2) {
        dec.substring(0, 2)
    }else{
        dec.padEnd(2, '0')
    }

    return "$whole.$formatedDec"
}