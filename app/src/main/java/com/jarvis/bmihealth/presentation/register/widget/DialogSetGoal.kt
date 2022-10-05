package com.jarvis.bmihealth.presentation.register.widget

import android.content.Context
import android.view.View
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.DialogGoalBinding
import com.jarvis.bmihealth.presentation.register.widget.ViewGoalEdit.OnListenerPosition
import com.jarvis.design_system.dialog.BaseAlertDialog

class DialogSetGoal(context: Context, index: Int, isKmS: Boolean) :
    BaseAlertDialog<DialogGoalBinding?>(context, R.layout.dialog_goal), View.OnClickListener {
    private var temp = 0
    private val isKmSetting: Boolean

    private var listenerClick: ListenerClick? = null
    private val isClickBackGround = true

    fun onListener(listenerClick: ListenerClick?) {
        this.listenerClick = listenerClick
    }

    private fun listenerOnClick() {
        binding!!.btCancel.setOnClickListener(this)
        binding!!.btSave.setOnClickListener(this)
    }

    override fun onShowDialog() {
        //do nothing
    }

    override fun onDismissDialog() {
        if (listenerClick != null) {
            listenerClick!!.closeDialog(isClickBackGround)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btCancel) {
            dismiss()
        } else if (v.id == R.id.btSave) {
            listenerClick!!.saveGoal(temp)
            dismiss()
        }
    }

    interface ListenerClick {
        fun closeDialog(isClickBackgroud: Boolean)
        fun saveGoal(goal: Int)
    }

    init {
        listenerOnClick()
        isKmSetting = isKmS
        temp = index
        binding!!.rvGoal.initView(context, isKmSetting, temp)
        binding!!.rvGoal.setListenerPosition(object : OnListenerPosition {
            override fun positionItem(position: Int) {
                temp = if(position == 0){
                    0
                }else{
                    position -1
                }
            }
        })
    }
}
