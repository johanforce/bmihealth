package com.jarvis.bmihealth.presentation.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ActivityMainBinding
import com.jarvis.bmihealth.presentation.Utils.Constant
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.bmiother.OtherFragment
import com.jarvis.bmihealth.presentation.home.HomeFragment
import com.jarvis.bmihealth.presentation.profile.ProfileFragment


class MainActivity :
    BaseActivity<ActivityMainBinding, MainViewModel>(ActivityMainBinding::inflate) {
    override fun getViewModelClass() = MainViewModel::class.java

    private var isBackPress = false
    private var homeFragment: HomeFragment? = null
    private var profileFragment: ProfileFragment? = null
    private var otherFragment: OtherFragment? = null
    private val fragments: MutableList<Fragment> = arrayListOf()
    private var currentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.mainViewModel = viewModel
        binding.lifecycleOwner = this

        initFragment()
        clickShowFragment(Constant.KEY_HOME)
        clickControlView()
        binding.viewControl.setItemSelected(R.id.home, true)
    }

    private fun initFragment() {
        try {
            homeFragment = HomeFragment()
            profileFragment = ProfileFragment()
            otherFragment = OtherFragment()
            fragments.add(otherFragment!!)
            fragments.add(homeFragment!!)
            fragments.add(profileFragment!!)

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

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun clickShowFragment(position: Int) {
        try {
            if (fragments.size == 0) {
                return
            }

            Toast.makeText(this, "----------" + position, Toast.LENGTH_SHORT).show()

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


    override fun onBackPressed() {
        if (this.currentIndex != 1) {
            this.viewModel.tempFrag.value = 1
            clickShowFragment(Constant.KEY_HOME)
            binding.viewControl.setItemSelected(R.id.home, true)
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


    private fun clickControlView() {
        binding.viewControl.setOnItemSelectedListener { id ->
           when (id) {
                R.id.home -> {
                    R.color.pri_app to getString(R.string.home)
                    clickShowFragment(Constant.KEY_HOME)
                }
                R.id.other -> {
                    R.color.pri_app to getString(R.string.bmi_other)
                    clickShowFragment(Constant.KEY_OTHER)
                }
                R.id.profile -> {
                    R.color.pri_app to getString(R.string.profile)
                    clickShowFragment(Constant.KEY_PROFILE)
                }
                else -> R.color.white to ""
            }
        }

    }
}
