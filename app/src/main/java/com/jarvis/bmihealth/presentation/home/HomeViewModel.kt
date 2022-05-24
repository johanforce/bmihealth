package com.jarvis.bmihealth.presentation.home

import androidx.lifecycle.MutableLiveData
import com.jarvis.bmihealth.domain.model.ProfileUser
import com.jarvis.bmihealth.domain.repository.NoteRepository
import com.jarvis.bmihealth.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val noteRepository: NoteRepository): BaseViewModel() {
    var profileUser = MutableLiveData<ProfileUser>()
    fun getProfile(){
        launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                profileUser.postValue(noteRepository.getProfileById(1))
            }
        }
    }
}