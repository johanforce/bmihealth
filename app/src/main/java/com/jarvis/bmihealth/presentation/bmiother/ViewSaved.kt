@file:Suppress("unused")

package com.jarvis.bmihealth.presentation.bmiother

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.databinding.ViewSavedDialogBinding
import com.jarvis.design_system.dialog.BaseAlertDialog

class ViewSaved(context: Context, bitmap: Bitmap) :
    BaseAlertDialog<ViewSavedDialogBinding?>(
        context, R.layout.view_saved_dialog
    ), View.OnClickListener {
    private val bitmap: Bitmap
    private var isClickBackGround = true
    private var listenerClick: ListenerClick? = null
    fun onListener(listenerClick: ListenerClick?) {
        this.listenerClick = listenerClick
    }

    private fun listenerOnClick() {
        binding!!.btClose.setOnClickListener(this)
    }

    override fun onShowDialog() {}
    override fun onDismissDialog() {
        if (listenerClick != null) {
            listenerClick!!.closeDialog(isClickBackGround)
        }
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btClose) {
            isClickBackGround = false
            dismiss()
        }
    }

    interface ListenerClick {
        fun closeDialog(isClickBackground: Boolean)
    }

    init {
        listenerOnClick()
        this.bitmap = bitmap
        binding!!.ivImage.setImageBitmap(bitmap)
    }
}