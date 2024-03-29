@file:Suppress("MemberVisibilityCanBePrivate")

package com.jarvis.bmihealth.presentation.register.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Pair
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.enums.GenderEnum
import com.jarvis.bmihealth.common.enums.GenderEnum.MALE
import com.jarvis.bmihealth.common.enums.TypeUnits
import com.jarvis.bmihealth.common.enums.TypeView
import com.jarvis.bmihealth.common.utils.TimeUtil
import com.jarvis.bmihealth.databinding.ViewInputHealthInfoBinding
import com.jarvis.bmihealth.domain.model.model.OtherProfile
import com.jarvis.bmihealth.domain.model.model.ProfileUserModel
import com.jarvis.bmihealth.presentation.register.model.ProfilePickerDialogModel
import com.jarvis.design_system.dialog.DatePickerDialog
import com.jarvis.design_system.dialog.NumberPickerModel
import com.jarvis.design_system.dialog.NumberUnitPickerDialog
import com.jarvis.heathcarebmi.utils.UnitConverter
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
class ViewInputHealthInfo : FrameLayout {
    var birthDay: Long = 788961600000
    var height: Double = 180.0
    var weight: Double = 75.0
    var gender = MALE.index
    var binding: ViewInputHealthInfoBinding? = null
    var unit: Int = TypeUnits.METRIC.index
    var profilePickerDialogModel: ProfilePickerDialogModel? = null
    private var listenerClick: ViewRPE.ListenerClick? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }


    var onProfileListener: OnClickProfileListener? = null


    fun setOnClickListener(onClickProfile: OnClickProfileListener) {
        onProfileListener = onClickProfile
    }

    interface OnClickProfileListener {
        fun unitProfileUser(isKm: Boolean)
        fun clickGender()
        fun clickDialog()
    }

    fun setHeightValue(heightInCm: Double?, unit: Int) {
        val height = UnitConverter.convertCmToFtStringIfNeed(
            heightInCm ?: 0.0, unit == TypeUnits.METRIC.index
        )
        this.binding?.viewHeight?.value = height
        this.height = heightInCm ?: 0.0
        binding?.viewHeight?.setEndText(
            if (unit == TypeUnits.METRIC.index) context.getString(R.string.unit_cm) else ""
        )
        onProfileListener?.unitProfileUser(unit == TypeUnits.METRIC.index)
    }

    fun setWeightValue(weightInKg: Double?, unit: Int) {
        val weight =
            UnitConverter.convertKgToLbsIfNeed(weightInKg ?: 0.0, unit == TypeUnits.METRIC.index)
        this.binding?.viewWeight?.value = UnitConverter.formatDoubleToStringDown(weight, 1)
        this.weight = weightInKg ?: 0.0
        binding?.viewWeight?.setEndText(
            if (unit == TypeUnits.METRIC.index) context.getString(R.string.unit_kg) else context.getString(
                R.string.unit_lbs
            )
        )
        onProfileListener?.unitProfileUser(unit == TypeUnits.METRIC.index)
    }

    fun getWeightValue(): String? {
        return this.binding?.viewWeight?.value
    }

    fun getHeightValue(): String? {
        return this.binding?.viewHeight?.value
    }

    fun setValueTextBirthday(valueTextBirthday: String?) {
        binding?.tvValueBirthday?.text = valueTextBirthday
    }

    fun getValueBirthDay(): String? {
        return binding?.tvValueBirthday?.value
    }

    fun setUserInfo(userProfile: ProfileUserModel?) {
        userProfile ?: return

        height = userProfile.height
        weight = userProfile.weight
        birthDay = userProfile.birthday
        gender = userProfile.gender
        unit = userProfile.unit


        setSelectedGender(userProfile.gender)
        setValueTextBirthday(TimeUtil.formatDateYYYY(userProfile.birthday, context))

        setHeightValue(userProfile.height, userProfile.unit)

        setWeightValue(userProfile.weight, userProfile.unit)
    }

    fun clearSelectedGender() {
        binding?.ivFemale?.isSelected = false
        binding?.ivMale?.isSelected = false
        binding?.ivUnknown?.isSelected = false
    }

    fun setSelectedGender(gender: Int) {
        clearSelectedGender()
        when (gender) {
            MALE.index -> {
                binding?.ivMale?.isSelected = true
                setHeightValue(180.0, unit)
                setWeightValue(75.0, unit)
                onProfileListener?.clickGender()
            }
            GenderEnum.FEMALE.index -> {
                binding?.ivFemale?.isSelected = true
                setHeightValue(165.0, unit)
                setWeightValue(60.0, unit)
                onProfileListener?.clickGender()
            }
            GenderEnum.OTHER.index -> {
                binding?.ivUnknown?.isSelected = true
                setHeightValue(170.0, unit)
                setWeightValue(68.0, unit)
                onProfileListener?.clickGender()
            }
        }
        this.gender = gender
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        val inflate = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewInputHealthInfoBinding.inflate(inflate, this, true)
        initClickListener()
    }

    fun initDefaultValue(profile: OtherProfile?) {
        val userProfile = ProfileUserModel(
            1,
            "Health",
            "for you",
            profile?.gender ?: MALE.index,
            profile?.birthday ?: 0L,
            0,
            null,
            "",
            profile?.weightInKilograms ?: 0.0,
            TypeUnits.METRIC.index,
            profile?.heightInCentimeters ?: 0.0,
            "",
            "VN"
        )

        setUserInfo(userProfile)
    }

    private fun initClickListener() {
        binding?.ivFemale?.setOnClickListener {
            setSelectedGender(GenderEnum.FEMALE.index)
        }
        binding?.ivMale?.setOnClickListener {
            setSelectedGender(MALE.index)
        }
        binding?.ivUnknown?.setOnClickListener {
            setSelectedGender(GenderEnum.OTHER.index)
        }
        binding?.tvValueBirthday?.setOnClickListener {
            openDialogBirthday(birthDay)
        }
        binding?.viewHeight?.setOnClickListener {
            openDialogHeight()
        }
        binding?.viewWeight?.setOnClickListener {
            openDialogWeight()
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun openDialogBirthday(birthDay: Long) {
        val datePickerDialog = DatePickerDialog.Builder()
            .setBirthday(birthDay)
            .setTitle(context.getString(R.string.all_birthday))
            .setNegativeButtonText(context.getString(R.string.all_cancel))
            .setPositiveButtonText(context.getString(R.string.all_save))
            .setOnDialogClickDate { birthday: Long ->
                val formatter = SimpleDateFormat("MMM dd, yyyy")
                val dateString = formatter.format(Date(birthday))
                this.binding?.tvValueBirthday?.text = dateString
                this.birthDay = birthday
            }
            .builder()
        val activity = context as AppCompatActivity
        datePickerDialog.show(activity.supportFragmentManager)
        onProfileListener?.clickDialog()
    }

    fun openDialogHeight() {
        onProfileListener?.clickDialog()
        profilePickerDialogModel = ProfilePickerDialogModel(context)
        try {
            val currentValueInLengthField: Pair<Int, Int> =
                if (unit == TypeUnits.METRIC.index) {
                    this.profilePickerDialogModel!!.getCurrentUserHeight(height, unit)
                } else {
                    this.profilePickerDialogModel!!.getCurrentUserHeight(
                        UnitConverter.convertCmToInch(
                            height
                        ), unit
                    )
                }
            val numberPickerModelOne: NumberPickerModel =
                this.profilePickerDialogModel!!.heightPickerModelUnitOne
            val numberPickerModelTwo: NumberPickerModel =
                this.profilePickerDialogModel!!.heightPickerModelUnitTwo
            val numberPickerOneMinValue = numberPickerModelOne.minValue.first
            val numberPickerTwoMinValue = numberPickerModelTwo.minValue.first
            val numberPickerDialog = NumberUnitPickerDialog.Builder(unit)
                .setTitle(context.getString(R.string.all_height))
                .setNegativeButtonText(context.getString(R.string.all_cancel))
                .setPositiveButtonText(context.getString(R.string.all_save))
                .setPickerModelUnitOne(numberPickerModelOne)
                .setPickerModelUnitTwo(numberPickerModelTwo)
                .setCurrentValue(currentValueInLengthField)
                .setOnActionClickListener(object : NumberUnitPickerDialog.OnDialogClickListener {
                    override fun onPositionClick(value: Pair<Int, Int>, typeUnit: Int) {
                        val valueFinal: Double =
                            profilePickerDialogModel!!.convertPairValueToDouble(
                                value,
                                typeUnit,
                                TypeView.HEIGHT.index
                            )
                        if (unit != typeUnit) {
                            unit = typeUnit
                            setWeightValue(weight, unit)
                        }
                        if (typeUnit == TypeUnits.METRIC.index) {
                            binding?.viewHeight?.value =
                                UnitConverter.formatDoubleToStringDown(valueFinal, 0).toString()
                            binding?.viewHeight?.setEndText(context.getString(R.string.unit_cm))
                            height = valueFinal
                        } else {
                            binding?.viewHeight?.value = String.format(
                                context.getString(
                                    R.string.edit_profile_height_in,
                                    value.first,
                                    value.second
                                )
                            )
                            height = UnitConverter.convertInchToCm(valueFinal)
                            binding?.viewHeight?.setEndText("")
                        }
                    }

                    override fun convertValueFromUnitOneToUnitTwo(unitOneValue: Pair<Int, Int>): Pair<Int, Int> {
                        return profilePickerDialogModel!!.convertLengthValueFromUnitOneToTwo(
                            unitOneValue,
                            numberPickerTwoMinValue
                        )
                    }

                    override fun convertValueFromUnitTwoToUnitOne(unitTwoValue: Pair<Int, Int>): Pair<Int, Int> {
                        return profilePickerDialogModel!!.convertLengthValueFromUnitTwoToOne(
                            unitTwoValue,
                            numberPickerOneMinValue
                        )
                    }
                })
                .builder()
            val activity = context as AppCompatActivity
            numberPickerDialog.show(activity.supportFragmentManager)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun openDialogWeight() {
        onProfileListener?.clickDialog()
        profilePickerDialogModel = ProfilePickerDialogModel(context)
        try {
            val currentValueInLengthField: Pair<Int, Int> =
                if (unit == TypeUnits.METRIC.index) {
                    this.profilePickerDialogModel!!.getCurrentUserWeight(weight)
                } else {
                    this.profilePickerDialogModel!!.getCurrentUserWeight(
                        UnitConverter.convertKgToLbs(
                            weight
                        )
                    )
                }
            val numberPickerModelOne: NumberPickerModel =
                this.profilePickerDialogModel!!.weightPickerModelUnitOne
            val numberPickerModelTwo: NumberPickerModel =
                this.profilePickerDialogModel!!.weightPickerModelUnitTwo
            val numberPickerOneMinValue = numberPickerModelOne.minValue.first
            val numberPickerTwoMinValue = numberPickerModelTwo.minValue.first
            val numberPickerDialog = NumberUnitPickerDialog.Builder(unit)
                .setTitle(context.getString(R.string.all_weight))
                .setNegativeButtonText(context.getString(R.string.all_cancel))
                .setPositiveButtonText(context.getString(R.string.all_save))
                .setPickerModelUnitOne(numberPickerModelOne)
                .setPickerModelUnitTwo(numberPickerModelTwo)
                .setCurrentValue(currentValueInLengthField)
                .setOnActionClickListener(object : NumberUnitPickerDialog.OnDialogClickListener {
                    override fun onPositionClick(value: Pair<Int, Int>, typeUnit: Int) {
                        val valueFinal: Double =
                            profilePickerDialogModel!!.convertPairValueToDouble(
                                value,
                                typeUnit,
                                TypeView.WEIGHT.index
                            )
                        if (unit != typeUnit) {
                            unit = typeUnit
                            setHeightValue(height, unit)
                        }

                        binding?.viewWeight?.value =
                            UnitConverter.formatDoubleToStringDown(valueFinal, 1).toString()
                        if (typeUnit == TypeUnits.METRIC.index) {
                            binding?.viewWeight?.setEndText(context.getString(R.string.unit_kg))
                            weight = valueFinal
                        } else {
                            weight = UnitConverter.convertLbsToKg(valueFinal)
                            binding?.viewWeight?.setEndText(context.getString(R.string.unit_lbs))
                        }
                    }

                    override fun convertValueFromUnitOneToUnitTwo(unitOneValue: Pair<Int, Int>): Pair<Int, Int> {
                        return profilePickerDialogModel!!.convertWeightValueFromUnitOneToTwo(
                            unitOneValue,
                            numberPickerTwoMinValue
                        )
                    }

                    override fun convertValueFromUnitTwoToUnitOne(unitTwoValue: Pair<Int, Int>): Pair<Int, Int> {
                        return profilePickerDialogModel!!.convertWeightValueFromUnitTwoToOne(
                            unitTwoValue,
                            numberPickerOneMinValue
                        )
                    }
                })
                .builder()
            val activity = context as AppCompatActivity
            numberPickerDialog.show(activity.supportFragmentManager)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }
}
