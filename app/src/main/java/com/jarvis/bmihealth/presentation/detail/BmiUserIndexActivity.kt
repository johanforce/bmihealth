@file:Suppress("DEPRECATION", "unused")

package com.jarvis.bmihealth.presentation.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ActivityBmiUserBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.register.RegisterActivity
import com.jarvis.bmihealth.presentation.utilx.Constant
import com.jarvis.bmihealth.presentation.utilx.observe
import com.jarvis.design_system.toolbar.JxToolbar
import com.jarvis.heathcarebmi.utils.Consts
import com.jarvis.heathcarebmi.utils.HealthIndexUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BmiUserIndexActivity :
    BaseActivity<ActivityBmiUserBinding, DetailViewModel>(ActivityBmiUserBinding::inflate) {

    private val viewModel: DetailViewModel by viewModels()

    private val listStatusAdult = arrayListOf(
        R.string.bmi_very_under_weight,
        R.string.bmi_under_weight,
        R.string.bmi_healthy_weight,
        R.string.bmi_over_weight,
        R.string.bmi_moderatelt_1,
        R.string.bmi_moderatelt_2,
        R.string.bmi_moderatelt_3
    )
    private val listStatusDesAdult = arrayListOf(
        R.string.bmi_very_under_weight_des,
        R.string.bmi_under_weight_des,
        R.string.bmi_healthy_weight_des,
        R.string.bmi_over_weight_des,
        R.string.bmi_moderatelt_1_des,
        R.string.bmi_moderatelt_2_des,
        R.string.bmi_moderatelt_3_des
    )
    private val listStatusColorAdult =
        arrayListOf(
            R.color.secondary,
            R.color.bmi_level1,
            R.color.bmi_level2,
            R.color.bmi_level3,
            R.color.bmi_level4,
            R.color.bmi_level5,
            R.color.bmi_level5
        )

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    private val listStatusChild = arrayListOf(
        R.string.bmi_very_under_weight,
        R.string.bmi_under_weight,
        R.string.bmi_healthy_weight,
        R.string.bmi_over_weight,
        R.string.bmi_obesity,
    )
    private val listStatusDesChild = arrayListOf(
        R.string.bmi_very_under_weight_des_child,
        R.string.bmi_under_weight_des_child,
        R.string.bmi_healthy_weight_des_child,
        R.string.bmi_over_weight_des_child,
        R.string.bmi_obesity_des_child
    )
    private val listStatusColorChild =
        arrayListOf(
            R.color.secondary,
            R.color.bmi_level1,
            R.color.bmi_level2,
            R.color.bmi_level3,
            R.color.bmi_level5
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        initView()
        setOnClickView()
    }

    private fun initView() {
        viewModel.getProfile()
    }

    private fun setOnClickView() {
        this.binding.tvEdit.setOnClickListener {
            val intent = Intent()
            this.let { it1 -> intent.setClass(it1, RegisterActivity::class.java) }
            intent.putExtra(Constant.NEXT_SCREEN_TO_PROFILE, true)
            resultLauncher.launch(intent)
        }
    }

    override fun observeData() {
        super.observeData()
        observe(viewModel.profileUsers) {
            viewModel.updateDataView()
            initData()
        }
    }

    private fun initData() {
        val bmiUser = viewModel.getBMI()
        val profileUser = viewModel.profileUser
        if (HealthIndexUtils.calculateAgeInMonth(profileUser.birthday) < Consts.MAX_CHILD_AGE_IN_MONTH) {
            if (HealthIndexUtils.calculateAgeInYear(profileUser.birthday) < 10) {
                binding.toolbar.setTitle(getString(R.string.bmi_toolbar_child))
            } else if (HealthIndexUtils.calculateAgeInYear(profileUser.birthday) >= 10) {
                binding.toolbar.setTitle(getString(R.string.bmi_toolbar_teen))
            }
            updateForChild(bmiUser)
            binding.bmiView.setBMIValue(profileUser.birthday, bmiUser)
        } else {
            binding.toolbar.setTitle(getString(R.string.bmi_toolbar_adult))
            updateForAdult(bmiUser)
            binding.bmiView.setBMIValue(profileUser.birthday, bmiUser)
        }
    }

    private fun updateForAdult(bmi: Double) {
        this.binding.tvIndexWeight.text = HealthIndexUtils.roundBmiValue(bmi).toString()
        this.binding.tvAnswer.text = getString(R.string.bmi_answer_adult)
        this.binding.tvTitleQuestion.text = getString(R.string.bmi_question_adult)
        this.binding.tvVisit.movementMethod = LinkMovementMethod.getInstance()
        this.binding.tvVisit.text = Html.fromHtml(getString(R.string.bmi_visit_adult))
        this.binding.tableAdult.visibility = View.VISIBLE
        this.binding.tableChild.visibility = View.GONE
        updateViewBmiAdult(bmi)
    }

    @SuppressLint("SetTextI18n")
    private fun updateForChild(bmi: Double) {
        var stt = ""
        if (bmi < 1.5 && bmi >= 0.5) {
            stt = "st"
        } else if (bmi >= 1.5 && bmi < 2.5) {
            stt = "nd"
        } else if (bmi >= 2.5 && bmi < 3.5) {
            stt = "rd"
        } else if (bmi >= 4.5) {
            stt = "th"
        }
        this.binding.tvIndexWeight.text = "${HealthIndexUtils.roundBmiValueChild(bmi)} $stt"
        this.binding.tvAnswer.text = getString(R.string.bmi_answer_child)
        this.binding.tvTitleQuestion.text = getString(R.string.bmi_question_child)
        this.binding.tvVisit.movementMethod = LinkMovementMethod.getInstance()
        this.binding.tvVisit.text = Html.fromHtml(getString(R.string.bmi_visit_child))
        this.binding.tableAdult.visibility = View.GONE
        this.binding.tableChild.visibility = View.VISIBLE
        updateViewBmiChild(bmi)
    }

    private fun updateViewBmiAdult(bmiUser: Double) {
        if (bmiUser < 16) {
            statusBmiAdult(0)
        } else if (bmiUser < 18.5 && bmiUser >= 16) {
            statusBmiAdult(1)
        } else if (bmiUser < 25 && bmiUser >= 18.5) {
            statusBmiAdult(2)
        } else if (bmiUser < 30 && bmiUser >= 25) {
            statusBmiAdult(3)
        } else if (bmiUser < 35 && bmiUser >= 30) {
            statusBmiAdult(4)
        } else if (bmiUser < 40 && bmiUser >= 35) {
            statusBmiAdult(5)
        } else if (bmiUser > 40) {
            statusBmiAdult(6)
        }
    }

    private fun updateViewBmiChild(bmiUser: Double) {
        if (bmiUser < 3) {
            statusBmiChild(0)
        } else if (bmiUser < 15 && bmiUser >= 3) {
            statusBmiChild(1)
        } else if (bmiUser < 85 && bmiUser >= 15) {
            statusBmiChild(2)
        } else if (bmiUser < 97 && bmiUser >= 85) {
            statusBmiChild(3)
        } else if (bmiUser >= 97) {
            statusBmiChild(4)
        }
    }

    private fun statusBmiAdult(index: Int) {
        this.binding.tvStatus.setTextColor(
            ContextCompat.getColor(
                this,
                listStatusColorAdult[index]
            )
        )
        this.binding.tvStatus.setText(listStatusAdult[index])
        this.binding.tvStatusDes.text = getString(listStatusDesAdult[index])
    }

    private fun statusBmiChild(index: Int) {
        this.binding.tvStatus.setTextColor(
            ContextCompat.getColor(
                this,
                listStatusColorChild[index]
            )
        )
        this.binding.tvStatus.setText(listStatusChild[index])
        this.binding.tvStatusDes.text = getString(listStatusDesChild[index])
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                this.setResult(Activity.RESULT_OK, this.intent)
                viewModel.getProfile()
            }
        }
}

