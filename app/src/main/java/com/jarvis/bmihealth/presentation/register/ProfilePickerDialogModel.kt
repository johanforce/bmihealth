package com.jarvis.bmihealth.presentation.register

import android.content.Context
import android.util.Pair
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.presentation.utilx.ProfileConstants.MAX_HEIGHT_CM
import com.jarvis.bmihealth.presentation.utilx.ProfileConstants.MAX_STRIDE_LENGTH_CM
import com.jarvis.bmihealth.presentation.utilx.ProfileConstants.MAX_WEIGHT_KG
import com.jarvis.bmihealth.presentation.utilx.ProfileConstants.MIN_HEIGHT_CM
import com.jarvis.bmihealth.presentation.utilx.ProfileConstants.MIN_STRIDE_LENGTH_CM
import com.jarvis.bmihealth.presentation.utilx.ProfileConstants.MIN_WEIGHT_KG
import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.IMPERIAL
import com.jarvis.bmihealth.presentation.utilx.TypeUnit.Companion.METRIC
import com.jarvis.bmihealth.presentation.utilx.TypeView
import com.jarvis.design_system.dialog.NumberPickerModel
import com.well.unitlibrary.UnitConverter
import java.util.*
import kotlin.math.roundToInt

class ProfilePickerDialogModel(private val context: Context) {
    val heightPickerModelUnitOne: NumberPickerModel
        get() {
            val numberPickerModelOne = NumberPickerModel()
            numberPickerModelOne.unit = METRIC
            numberPickerModelOne.unitTitle =
                context.getString(R.string.all_metric)
            numberPickerModelOne.unitText = context.getString(R.string.unit_cm)
            numberPickerModelOne.splitText = "."
            numberPickerModelOne.totalPicker = 1
            numberPickerModelOne.minValue =
                Pair(MIN_HEIGHT_CM, 0)
            numberPickerModelOne.maxValue =
                Pair(MAX_HEIGHT_CM, 0)
            numberPickerModelOne.valueRange = listOf(
                Pair(
                    MIN_HEIGHT_CM,
                    MAX_HEIGHT_CM
                ), Pair(0, 0)
            )
            return numberPickerModelOne
        }
    val heightPickerModelUnitTwo: NumberPickerModel
        get() {
            val numberPickerModelTwo = NumberPickerModel()
            numberPickerModelTwo.unit = IMPERIAL
            numberPickerModelTwo.unitTitle = context.getString(R.string.all_imperial)
            numberPickerModelTwo.unitText = context.getString(R.string.unit_in_symbol)
            numberPickerModelTwo.splitText = "'"
            numberPickerModelTwo.totalPicker = 2
            val minInch = UnitConverter.convertCmToInch(MIN_HEIGHT_CM.toDouble())
            val maxInch = UnitConverter.convertCmToInch(MAX_HEIGHT_CM.toDouble())
            val minFeetPair = UnitConverter.convertInchToFeetPair(minInch)
            val maxFeetPair = UnitConverter.convertInchToFeetPair(maxInch)
            numberPickerModelTwo.minValue = minFeetPair
            numberPickerModelTwo.maxValue = maxFeetPair
            numberPickerModelTwo.valueRange = listOf(
                Pair(
                    minFeetPair.first,
                    maxFeetPair.first
                ), Pair(0, 11)
            )
            return numberPickerModelTwo
        }

    fun getCurrentUserHeight(height: Double, currentUnit: Int): Pair<Int, Int> {
        val currentValue: Pair<Int, Int> = if (currentUnit == METRIC) {
            Pair(height.toInt(), 0)
        } else {
            val valueFt = UnitConverter.convertInchToFeet(height)
            val foot = valueFt.toInt()
            val inch = UnitConverter.convertFeetToInch(valueFt - foot).toInt()
            Pair(foot, inch)
        }
        return currentValue
    }

    val strideLengthPickerModelUnitOne: NumberPickerModel
        get() {
            val numberPickerModelOne = NumberPickerModel()
            numberPickerModelOne.unit = METRIC
            numberPickerModelOne.unitTitle = context.getString(R.string.all_metric)
            numberPickerModelOne.unitText = context.getString(R.string.unit_cm)
            numberPickerModelOne.totalPicker = 1
            numberPickerModelOne.splitText = "."
            numberPickerModelOne.minValue =
                Pair(MIN_STRIDE_LENGTH_CM, 0)
            numberPickerModelOne.maxValue =
                Pair(MAX_STRIDE_LENGTH_CM, 0)
            numberPickerModelOne.valueRange = listOf(
                Pair(
                    MIN_STRIDE_LENGTH_CM,
                    MAX_STRIDE_LENGTH_CM
                ), Pair(0, 0)
            )
            return numberPickerModelOne
        }
    val strideLengthPickerModelUnitTwo: NumberPickerModel
        get() {
            val numberPickerModelTwo = NumberPickerModel()
            numberPickerModelTwo.unit = IMPERIAL
            numberPickerModelTwo.unitTitle = context.getString(R.string.all_imperial)
            numberPickerModelTwo.unitText = context.getString(R.string.unit_in_symbol)
            numberPickerModelTwo.splitText = "'"
            numberPickerModelTwo.totalPicker = 2
            val minInch = UnitConverter.convertCmToInch(MIN_STRIDE_LENGTH_CM.toDouble())
            val maxInch = UnitConverter.convertCmToInch(MAX_STRIDE_LENGTH_CM.toDouble())
            val minFeetPair = UnitConverter.convertInchToFeetPair(minInch)
            val maxFeetPair = UnitConverter.convertInchToFeetPair(maxInch)
            numberPickerModelTwo.minValue = minFeetPair
            numberPickerModelTwo.maxValue = maxFeetPair
            numberPickerModelTwo.valueRange =
                mutableListOf(
                    Pair(
                        minFeetPair.first,
                        maxFeetPair.first
                    ), Pair(0, 11)
                )
            return numberPickerModelTwo
        }

    fun getCurrentUserStrideLength(strideLength: Double, currentUnit: Int): Pair<Int, Int> {
        return if (currentUnit == METRIC) {
            Pair(strideLength.toInt(), 0)
        } else UnitConverter.convertInchToFeetPair(strideLength)
    }

    fun convertLengthValueFromUnitOneToTwo(
        value: Pair<Int, Int>,
        minIntValue: Int
    ): Pair<Int, Int> {
        var valueFt = UnitConverter.convertCmToInch(value.first?.toDouble()?:0.0)
        if (valueFt < minIntValue) {
            valueFt = valueFt.roundToInt().toDouble()
        }
        return UnitConverter.convertInchToFeetPair(valueFt)
    }

    fun convertLengthValueFromUnitTwoToOne(
        value: Pair<Int, Int>,
        minIntValue: Int
    ): Pair<Int, Int> {
        var valueCm =
            UnitConverter.convertFtToCm(value.first!!) + UnitConverter.convertInchToCm(value.second?.toDouble()?:0.0)
        if (valueCm < minIntValue) {
            valueCm = valueCm.roundToInt().toDouble()
        }
        return Pair(valueCm.toInt(), 0)
    }

    fun convertWeightValueFromUnitOneToTwo(
        value: Pair<Int, Int>,
        minIntValue: Int
    ): Pair<Int, Int> {
        val unitOneValueDouble = value.first + value.second / 10.0
        var valueLbs = UnitConverter.convertKgToLbs(unitOneValueDouble)
        if (valueLbs < minIntValue) {
            valueLbs = valueLbs.roundToInt().toDouble()
        }
        return Pair(valueLbs.toInt(), ((valueLbs - valueLbs.toInt()) * 10).toInt())
    }

    fun convertWeightValueFromUnitTwoToOne(
        value: Pair<Int, Int>,
        minIntValue: Int
    ): Pair<Int, Int> {
        val unitTwoValueDouble = value.first + value.second / 10.0
        var valueKg = UnitConverter.convertLbsToKg(unitTwoValueDouble)
        if (valueKg < minIntValue) {
            valueKg = valueKg.roundToInt().toDouble()
        }
        return Pair(valueKg.toInt(), ((valueKg - valueKg.toInt()) * 10).toInt())
    }

    val weightPickerModelUnitOne: NumberPickerModel
        get() {
            val numberPickerModelOne = NumberPickerModel()
            numberPickerModelOne.unit = METRIC
            numberPickerModelOne.unitTitle = context.getString(R.string.all_metric)
            numberPickerModelOne.unitText = context.getString(R.string.unit_kg)
            numberPickerModelOne.totalPicker = 2
            numberPickerModelOne.splitText = "."
            numberPickerModelOne.minValue =
                Pair(MIN_WEIGHT_KG, 0)
            numberPickerModelOne.maxValue =
                Pair(MAX_WEIGHT_KG, 0)
            numberPickerModelOne.valueRange =
                listOf(
                    Pair(
                        MIN_WEIGHT_KG,
                        MAX_WEIGHT_KG
                    ), Pair(0, 9)
                )
            return numberPickerModelOne
        }
    val weightPickerModelUnitTwo: NumberPickerModel
        get() {
            val numberPickerModelTwo = NumberPickerModel()
            numberPickerModelTwo.unit = IMPERIAL
            numberPickerModelTwo.unitTitle =
                context.getString(R.string.all_imperial)
            numberPickerModelTwo.unitText = context.getString(R.string.unit_lbs)
            numberPickerModelTwo.totalPicker = 2
            numberPickerModelTwo.splitText = "."
            val minWeightLbs = UnitConverter.convertKgToLbs(
                MIN_WEIGHT_KG.toDouble()
            ).roundToInt().toDouble()
            val maxWeightLbs = UnitConverter.convertKgToLbs(MAX_WEIGHT_KG.toDouble())
            numberPickerModelTwo.minValue = Pair(
                minWeightLbs.toInt(),
                ((minWeightLbs - minWeightLbs.toInt()) * 10).toInt()
            )
            numberPickerModelTwo.maxValue = Pair(
                maxWeightLbs.toInt(),
                ((maxWeightLbs - maxWeightLbs.toInt()) * 10).toInt()
            )
            numberPickerModelTwo.valueRange =
                listOf(
                    Pair(
                        minWeightLbs.toInt(),
                        maxWeightLbs.toInt()
                    ), Pair(0, 9)
                )
            return numberPickerModelTwo
        }

    fun getCurrentUserWeight(weight: Double): Pair<Int, Int> {
        return Pair(weight.toInt(), (weight * 10 % 10).toInt())
    }

    fun convertPairValueToDouble(pairValue: Pair<Int, Int>, typeUnit: Int, typeView: Int): Double {
        var valueFinal = pairValue.first.toDouble()
        if (typeUnit == IMPERIAL) {
            if (typeView == TypeView.height || typeView == TypeView.strideLength) {
                valueFinal =
                    UnitConverter.convertFeetToInch(pairValue.first.toDouble()) + pairValue.second
            }
        }
        if (typeView == TypeView.weight) {
            valueFinal = pairValue.first + pairValue.second / 10.0
        }
        return valueFinal
    }
}