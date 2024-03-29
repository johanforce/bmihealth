package com.jarvis.bmihealth.presentation.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ActivityMainBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.home.HomeFragment
import com.jarvis.bmihealth.presentation.onboarding.OnBoardingActivity
import com.jarvis.bmihealth.presentation.common.pref.AppPreferenceKey
import com.jarvis.bmihealth.presentation.profile.ProfileFragment
import com.jarvis.bmihealth.presentation.common.Constant
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.extensions.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {

    private var isBackPress = false
    private var homeFragment: HomeFragment? = null
    private var profileFragment: ProfileFragment? = null
    private val fragments: MutableList<Fragment> = arrayListOf()
    private var currentIndex: Int = 0

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
    }

    override fun setUpViews() {
        this.checkLanguageChanged()
        initFragment()
        clickShowFragment(Constant.KEY_HOME)
        setOnClickView()
    }

    private fun setOnClickView() {
        binding.viewControlHealth.click {
            viewModel.tempFrag.value = Constant.KEY_HOME
        }
        binding.viewControlProfile.click {
            viewModel.tempFrag.value = Constant.KEY_PROFILE
        }
    }

    private fun initFragment() {
        homeFragment = HomeFragment()
        profileFragment = ProfileFragment()
        fragments.add(homeFragment ?: ProfileFragment())
        fragments.add(profileFragment?: HomeFragment())

        val size = fragments.size
        val supportFragmentManager = supportFragmentManager

        for (index in 0 until size) {
            val fragment = fragments[index]
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            if (!fragment.isAdded) {
                fragmentTransaction.add(R.id.frag, fragment, "fragment$index")
            }
            if (index != 0) {
                fragmentTransaction.hide(fragment).commitAllowingStateLoss()
            } else {
                fragmentTransaction.commitAllowingStateLoss()
            }
        }
    }

    private fun clickShowFragment(position: Int) {
        try {
            val targetFragment = fragments[position]
            val currentFragment: Fragment = getCurrentFragment()
            if (currentFragment.isAdded) {
                currentFragment.onPause()
            }
            if (!isFinishing) {
                supportFragmentManager.beginTransaction().hide(currentFragment).show(targetFragment)
                    .commitAllowingStateLoss()
                currentIndex = position
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getCurrentFragment(): Fragment {
        return fragments[currentIndex]
    }

    override fun observeData() {
        super.observeData()
        observe(viewModel.tempFrag) { position ->
            if (position != null) {
                clickShowFragment(position)
            }
            val colorProfileText =
                if (position == Constant.KEY_PROFILE) ContextCompat.getColor(
                    this,
                    R.color.pri_5
                ) else ContextCompat.getColor(this, R.color.ink_2)
            binding.tvProfile.setTextColor(colorProfileText)

            val colorHomeText =
                if (position == Constant.KEY_HOME) ContextCompat.getColor(
                    this,
                    R.color.pri_5
                ) else ContextCompat.getColor(this, R.color.ink_2)
            binding.tvHealth.setTextColor(colorHomeText)

            binding.ivHealth.isSelected = position == Constant.KEY_HOME
            binding.ivProfile.isSelected = position == Constant.KEY_PROFILE
        }
    }

    private fun checkLanguageChanged() {
        if (this.appPreference?.get(AppPreferenceKey.KEY_IS_CHANGE_LANGUAGE, Boolean::class.java)!!
        ) {
            this.appPreference?.put(AppPreferenceKey.KEY_IS_CHANGE_LANGUAGE, false)
            restartActivity()
        }
    }

    private fun restartActivity() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        finish()
        startActivity(intent)
    }


    override fun onBackPressed() {
        if (this.currentIndex != Constant.KEY_HOME) {
            this.viewModel.tempFrag.value = 1
            clickShowFragment(Constant.KEY_HOME)
            return
        }
        if (!isBackPress) {
            Toast.makeText(this, resources.getString(R.string.home_exit), Toast.LENGTH_SHORT).show()
            isBackPress = true
            Handler(Looper.getMainLooper()).postDelayed({
                isBackPress = false
            }, 2000)
            return
        }
        super.onBackPressed()
    }
}
