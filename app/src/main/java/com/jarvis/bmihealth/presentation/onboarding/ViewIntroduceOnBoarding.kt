package com.jarvis.bmihealth.presentation.onboarding

import android.content.Context
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.gson.GsonBuilder
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewIntroduceOnboardingBinding
import com.jarvis.bmihealth.presentation.onboarding.adapter.SlideAdapter
import java.util.*

class ViewIntroduceOnBoarding : ConstraintLayout {
    var binding: ViewIntroduceOnboardingBinding? = null
    var slideAdapter: SlideAdapter? = null

    companion object {
        var onClickListenerMain: OnClickListener? = null
    }

    fun onClickListener(onClickListener: OnClickListener) {
        onClickListenerMain = onClickListener
    }

    constructor(context: Context) :
            super(context, null, 0, 0) {
        this.initView()

    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0, 0) {
        this.initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr,
        0
    ) {
        this.initView()
    }

    fun initView() {
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewIntroduceOnboardingBinding.inflate(layoutInflater, this, true)
        this.initViewPager(context)
        this.addDataViewPager(context)
        this.inflaterView(binding!!.tvTermOfService, context)
    }

    private fun initViewPager(context: Context) {
        try {
            this.slideAdapter = SlideAdapter(
                (context as AppCompatActivity).supportFragmentManager,
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )
            this.binding?.vpLine?.adapter = slideAdapter
            binding!!.dotsIndicator.setViewPager(binding!!.vpLine)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addDataViewPager(context: Context) {
        val onBoardingModels: MutableList<OnBoardingModel> = ArrayList<OnBoardingModel>()
        val modelTab1 = OnBoardingModel()
        modelTab1.drawable = R.drawable.onboarding_ic_tab1temp
        modelTab1.string = context.getString(R.string.onboarding_des_tab1)
        modelTab1.title = context.getString(R.string.onboarding_title_tab1)
        onBoardingModels.add(modelTab1)

        val modelTab2 = OnBoardingModel()
        modelTab2.drawable = R.drawable.onboarding_ic_tab2
        modelTab2.string = context.getString(R.string.onboarding_des_tab2)
        modelTab2.title = context.getString(R.string.onboarding_title_tab2)
        onBoardingModels.add(modelTab2)

        val modelTab3 = OnBoardingModel()
        modelTab3.drawable = R.drawable.onboarding_ic_tab3
        modelTab3.string = context.getString(R.string.onboarding_des_tab3)
        modelTab3.title = context.getString(R.string.onboarding_title_tab3)
        onBoardingModels.add(modelTab3)

        val listFragment: MutableList<Fragment> = ArrayList<Fragment>()
        for (onBoardingModel in onBoardingModels) {
            val fragmentHomeTop = OnBoardingFragment()
            val bundle = Bundle()
            val toJson =
                GsonBuilder().serializeSpecialFloatingPointValues().create().toJson(onBoardingModel)
            bundle.putString(OnBoardingFragment.DATA_CLASS, toJson)
            fragmentHomeTop.arguments = bundle
            listFragment.add(fragmentHomeTop)
        }
        slideAdapter!!.addData(listFragment)
    }

    private fun inflaterView(textView: TextView?, context: Context) {
        if (textView == null) {
            return
        }
        try {
            val spanBuilder = SpannableStringBuilder(
                resources.getString(R.string.onboarding_by_get_start).trim { it <= ' ' } + " ")
            spanBuilder.append(
                resources.getString(R.string.all_term_of_service).trim { it <= ' ' } + "")
            spanBuilder.setSpan(
                MyClickableSpanTermOfService(context),
                spanBuilder.length - resources.getString(R.string.all_term_of_service).length,
                spanBuilder.length,
                0
            )
            spanBuilder.append(" ")
            spanBuilder.append(
                resources.getString(R.string.onboarding_view_our).trim { it <= ' ' } + " ")
            spanBuilder.append(
                resources.getString(R.string.onboarding_privacy_policy).trim { it <= ' ' } + "")
            spanBuilder.setSpan(
                MyClickableSpanPrivacyPolicy(context),
                spanBuilder.length - resources.getString(R.string.onboarding_privacy_policy).length,
                spanBuilder.length,
                0
            )
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.highlightColor = 0
            textView.setText(spanBuilder, TextView.BufferType.SPANNABLE)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    class MyClickableSpanTermOfService(var context: Context) : ClickableSpan() {
        override fun onClick(tv: View) {
            if (onClickListenerMain != null) {
                onClickListenerMain?.onClickTermOfService()
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color =
                ContextCompat.getColor(context, R.color.pri_1) // you can use custom color
            ds.isUnderlineText = false // set to false to remove underline
        }
    }

    class MyClickableSpanPrivacyPolicy(var context: Context) : ClickableSpan() {
        override fun onClick(tv: View) {
            if (onClickListenerMain != null) {
                onClickListenerMain?.onClickPrivacyPolicy()
            }
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = ContextCompat.getColor(context, R.color.pri_1)// you can use custom color
            ds.isUnderlineText = false // set to false to remove underline
        }
    }

    interface OnClickListener {
        fun onClickTermOfService()

        fun onClickPrivacyPolicy()
    }

}