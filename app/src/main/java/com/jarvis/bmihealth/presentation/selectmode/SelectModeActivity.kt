package com.jarvis.bmihealth.presentation.selectmode

import androidx.activity.viewModels
import androidx.core.view.WindowInsetsControllerCompat
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.enums.ThemeMode
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.common.helper.ThemeHelper
import com.jarvis.bmihealth.common.extensions.observe
import com.jarvis.bmihealth.databinding.ActivitySelectModeBinding
import com.jarvis.bmihealth.presentation.base.BaseActivity
import com.jarvis.bmihealth.presentation.common.pref.AppPreferenceKey
import com.jarvis.design_system.toolbar.JxToolbar
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by LongDT on 9/13/2021.
 */
@AndroidEntryPoint
class SelectModeActivity :
    BaseActivity<ActivitySelectModeBinding, SelectModeViewModel>(ActivitySelectModeBinding::inflate) {

    private val viewModel: SelectModeViewModel by viewModels()

    override fun initDarkMode() {
        setTheme(R.style.DSAppTheme)
    }

    override fun setUpViews() {
        super.setUpViews()
        binding.lifecycleOwner = this
        setOnClickView()
        viewModel.getProfile()
    }

    private fun setOnClickView() {
        binding.viewSelectLightMode.click {
            viewModel.selectMode(ThemeMode.LIGHT.index)
        }

        binding.viewSelectDarkMode.click {
            viewModel.selectMode(ThemeMode.DARK.index)
        }

        binding.viewSelectSystemMode.click {
            viewModel.selectMode(ThemeMode.FOLLOW_SYSTEM.index)
        }
    }

    override fun observeData() {
        super.observeData()

        observe(viewModel.profileUsers) {
            viewModel.updateDataView()
        }

        observe(viewModel.isChangeThemeMode) {
            if (it) {
                viewModel.themeMode.value = viewModel.profileUser.themeMode
            }
        }

        observe(viewModel.themeMode) { mode ->
            updateSelectModeButton(mode)
            appPreference?.putSync(AppPreferenceKey.KEY_THEMEMODE, mode)
        }
    }

    private fun updateSelectModeButton(index: Int) {
        when (index) {
            ThemeMode.LIGHT.index -> {
                binding.viewSelectLightMode.viewBinder.imageViewStartIcon.isSelected = true
                binding.viewSelectDarkMode.viewBinder.imageViewStartIcon.isSelected = false
                binding.viewSelectSystemMode.viewBinder.imageViewStartIcon.isSelected = false
                WindowInsetsControllerCompat(
                    window,
                    binding.viewParent
                ).isAppearanceLightStatusBars = true
            }
            ThemeMode.DARK.index -> {
                binding.viewSelectLightMode.viewBinder.imageViewStartIcon.isSelected = false
                binding.viewSelectDarkMode.viewBinder.imageViewStartIcon.isSelected = true
                binding.viewSelectSystemMode.viewBinder.imageViewStartIcon.isSelected = false
                WindowInsetsControllerCompat(
                    window,
                    binding.viewParent
                ).isAppearanceLightStatusBars = false
            }
            ThemeMode.FOLLOW_SYSTEM.index -> {
                binding.viewSelectLightMode.viewBinder.imageViewStartIcon.isSelected = false
                binding.viewSelectDarkMode.viewBinder.imageViewStartIcon.isSelected = false
                binding.viewSelectSystemMode.viewBinder.imageViewStartIcon.isSelected = true
                WindowInsetsControllerCompat(
                    window,
                    binding.viewParent
                ).isAppearanceLightStatusBars = !isDarkTheme()
            }
        }
        ThemeHelper.applyTheme(index)
    }

    override fun getToolbar(): JxToolbar {
        return binding.toolbar
    }

}
