//package com.jarvis.bmihealth.presentation.bmiother
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.result.ActivityResultLauncher
//import androidx.core.content.ContextCompat
//import com.jarvis.bmihealth.R
//import com.jarvis.bmihealth.databinding.ActivityResultOtherBinding
//import com.jarvis.bmihealth.domain.model.ProfileUserModel
//import com.jarvis.bmihealth.presentation.base.BaseActivity
//import com.jarvis.bmihealth.presentation.home.ViewHomeBMI
//import com.jarvis.design_system.toolbar.OnToolbarActionListener
//import com.jarvis.heathcarebmi.utils.HealthIndexUtils
//import com.well.being.pref.AppPreference
//import com.well.being.profile.manage.view.AddProfileActivity
//import com.well.being.profile.manage.view.ManageProfileActivity
//import com.well.being.room.entity.UserProfile
//import com.well.designsystem.view.toolbar.WellToolbar
//import com.well.unitlibrary.UnitConverter
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.util.*
//
//class ResultOtherActivity :
//    BaseActivity<ActivityResultOtherBinding, ResultViewModel>(ActivityResultOtherBinding::inflate),
//    OnToolbarActionListener {
//
//    private val listRequired = arrayListOf(
//        R.string.onboarding_strict_loos,
//        R.string.onboarding_mormal_weight,
//        R.string.onboarding_comfortable,
//        R.string.onboarding_maintain,
//        R.string.onboarding_normal,
//        R.string.onboarding_strict
//    )
//    private var startForResult: ActivityResultLauncher<Intent>? = null
//
//    var listUser: List<UserProfile> = listOf()
//
//    override fun getToolbar(): WellToolbar {
//        return binding.toolbar
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding.resultViewModel = viewModel
//        binding.lifecycleOwner = this
//
//        initView()
//    }
//
//    private fun initView() {
//        this.binding.toolbar.setOnToolbarActiontListener(this)
//        appPreference = AppPreference.getInstance()
//        binding.viewBMI.init(
//            this,
//            this.otherModel.weight,
//            this.otherModel.weight,
//            this.otherModel.birthday,
//            this.otherModel.gender,
//            isKmSetting,
//            null
//        )
//        binding.viewBMI.disableClickView()
//        binding.btReCal.setOnClickListener {
//
//            finish()
//        }
//        binding.btShare.setOnClickListener {
//            val intent = Intent(this@ResultOtherActivity, PreviewActivity::class.java)
//            intent.putExtra("RESULT_OTHER", otherModel)
//            intent.putExtra("RESULT_AGE", HealthIndexUtils.calculateAgeInYear(otherModel.birthday))
//            intent.putExtra("RESULT_BMR", bmrOther)
//            intent.putExtra("RESULT_MAINTAIN", maintainOther)
//            intent.putExtra("RESULT_UNIT", isKmSetting)
//            startActivity(intent)
//        }
//
//        launch(Dispatchers.Main) {
//            listUser = withContext(Dispatchers.IO) {
//                appDAO.getAllActiveProfile()
//            }
//
//            binding.btnSave.setOnClickListener {
//                if (!listUser.isNullOrEmpty() && listUser.size >= 5) {
//                    val dialog = FullProfileDialog(this@ResultOtherActivity)
//                    dialog.init()
//                    dialog.setListener(object : FullProfileDialog.ListenerClick {
//                        override fun onManageProfileClick() {
//                            startActivity(Intent(this@ResultOtherActivity, ManageProfileActivity::class.java))
//                        }
//                    })
//                    dialog.show(true, false)
//                } else {
//                    val intent = Intent(this@ResultOtherActivity, AddProfileActivity::class.java)
//                    intent.putExtra("BMI_OTHER", otherModel)
//                    intent.putExtra("KEY_TO_ADD_PROFILE", 2)
//                    startForResult?.launch(intent)
//                }
//            }
//        }
//        loadDataOther(otherModel)
//    }
//
//    private fun loadDataOther(otherModel: ProfileUserModel) {
//        if ((otherModel.goal ?: 0) < 1) {
//            otherModel.goal = 1
//        } else if (otherModel.goal > listRequired.size) {
//            otherModel.goal = listRequired.size
//        }
//        this.binding.viewBMI.loadData(
//            otherModel.weight,
//            otherModel.height,
//            otherModel.birthday,
//            otherModel.gender,
//            isKmSetting,
//            null
//        )
//
//        val name =
//            otherModel.firstname + " " + otherModel.lastname
//        this.binding.tvName.text = name
//
//        binding.ivMainTain.setTitle(getString(listRequired[otherModel.goal - 1]))
//        this.binding.tvAge.text = getString(
//            R.string.all_years_old,
//            HealthIndexUtils.calculateAgeInYear(otherModel.birthday)
//        )
//        if (isKmSetting) {
//            this.binding.ivWeight.setIndex(
//                UnitConverter.formatDoubleToString(otherModel.weight, 1).toString()
//            )
//            this.binding.ivWeight.setUnit(getString(R.string.unit_kg))
//            this.binding.ivHeight.setIndex(
//                UnitConverter.formatDoubleToString(otherModel.height, 0).toString()
//            )
//            this.binding.ivHeight.setUnit(getString(R.string.unit_cm))
//        } else {
//            this.binding.ivWeight.setIndex(
//                UnitConverter.formatDoubleToString(
//                    UnitConverter.convertKgToLbs(
//                        otherModel.weight
//                    ), 1
//                ).toString()
//            )
//            this.binding.ivWeight.setUnit(getString(R.string.unit_lbs))
//            this.binding.ivHeight.setIndex(
//                UnitConverter.convertCmToFtStringIfNeed(
//                    otherModel.height, false
//                ).toString()
//            )
//            this.binding.ivHeight.setUnit("")
//        }
//        bmrOther = HealthIndexUtils.getBmr(
//            otherModel.weight,
//            otherModel.height,
//            otherModel.birthday,
//            otherModel.gender
//        ).toInt()
//        this.binding.ivBMR.setIndex(UnitConverter.formatLong(bmrOther).toString())
//        var tdee = HealthIndexUtils.getTdee(
//            HealthIndexUtils.getBmr(
//                otherModel.weight,
//                otherModel.height,
//                otherModel.birthday,
//                otherModel.gender
//            ),
//            otherModel.activityLevel
//        )
//        maintainOther = HealthIndexUtils.getCaloRequired(tdee, otherModel.goals).toInt()
//        this.binding.ivMainTain.setIndex(UnitConverter.formatLong(maintainOther).toString())
//        if (otherModel.gender == 0) {
//            this.binding.ivGender.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.others_gender_male_on))
//        } else if (otherModel.gender == 1) {
//            this.binding.ivGender.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.others_gender_female_on))
//        } else {
//            this.binding.ivGender.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.gender_unknown_on))
//        }
//
//        this.binding.viewBMI.setOnClick(object : ViewHomeBMI.OnClickListener {
//            override fun onClickHealthy() {
//            }
//
//            override fun onClickBody() {
//            }
//
//            override fun onClickBMI() {
//            }
//
//        })
//    }
//
//    override fun onToolbarTextCtaClick() {
//
//    }
//
//    override fun onToolbarAction1Click() {
//
//    }
//
//    override fun onToolbarAction2Click() {
//
//    }
//}