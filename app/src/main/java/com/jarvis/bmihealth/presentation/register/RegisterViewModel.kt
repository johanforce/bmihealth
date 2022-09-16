package com.jarvis.bmihealth.presentation.register

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.model.ProfileUserModel
import com.jarvis.bmihealth.domain.usecase.UserProfileUseCase
import com.jarvis.bmihealth.presentation.utilx.Constant.MALE
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("unused")
@HiltViewModel
class RegisterViewModel @Inject constructor(private val userProfileUseCase: UserProfileUseCase) :
    BaseViewModel() {
}
