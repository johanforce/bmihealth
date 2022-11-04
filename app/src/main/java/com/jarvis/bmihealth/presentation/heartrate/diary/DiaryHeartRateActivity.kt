@file:Suppress("DEPRECATION")

package com.jarvis.bmihealth.presentation.heartrate.diary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout
import com.jarvis.bmihealth.databinding.ActivityDiaryBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.heartrate.HeartRateTrackingViewModel
import com.jarvis.bmihealth.presentation.onboarding.adapter.SlideAdapter
import com.jarvis.design_system.toolbar.JxToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiaryHeartRateActivity :
    BaseActivity<ActivityDiaryBinding, HeartRateTrackingViewModel>(
        ActivityDiaryBinding::inflate
    ) {

    private val viewModel: HeartRateTrackingViewModel by viewModels()

    var tabIndex = 0
    private val fragmentWeek = 0
    private val fragmentMonth = 1
    private var tabWeek: TabLayout.Tab? = null
    private var tabMonth: TabLayout.Tab? = null
    private val fragments: MutableList<Fragment> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

    override fun setUpViews() {
        super.setUpViews()

        initData()
    }

    private fun initData() {
        viewModel.getProfile()
        setupViewPager()
        setOnClickListener()
    }

    private fun setupViewPager() {
        val diaryWeekFragment = DiaryWeekFragment()
        val diaryMonthFragment = DiaryMonthFragment()
        this.fragments.add(diaryWeekFragment)
        this.fragments.add(diaryMonthFragment)
        binding.viewPager.offscreenPageLimit = 2
        val adapter = SlideAdapter(
            supportFragmentManager,
            FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        )
        adapter.addData(fragments)
        binding.viewPager.adapter = adapter
        this.tabWeek =
            binding.viewTab.getTabAt(fragmentWeek)
        this.tabMonth =
            binding.viewTab.getTabAt(fragmentMonth)
        binding.viewPager.currentItem = this.tabIndex
        setupTabLayout()
    }

    fun setupTabLayout() {
        when (tabIndex) {
            fragmentMonth -> {
                binding.viewTab.selectTab(tabMonth)
            }
            else -> {
                binding.viewTab.selectTab(tabWeek)
            }
        }
    }

    private fun setOnClickListener() {
        binding.viewTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                //do nothing
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                //do nothing
            }
        })
        binding.viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //do nothing
            }

            override fun onPageSelected(position: Int) {
                tabIndex = position
                setupTabLayout()
            }

            override fun onPageScrollStateChanged(state: Int) {
                //do nothing
            }
        })
    }
}



