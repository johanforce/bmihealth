package com.jarvis.bmihealth.presentation.heartrate.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.jarvis.bmihealth.R
import com.jarvis.bmihealth.common.extensions.click
import com.jarvis.bmihealth.databinding.DialogNoFingerFoundBinding

class DialogFingerFound : BottomSheetDialogFragment(), View.OnClickListener {
    private var listener: ClickListener? = null
    private var binding: DialogNoFingerFoundBinding? = null

    fun setListener(listener: ClickListener?) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_no_finger_found, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogNoFingerFoundBinding.bind(view)
        initView()
    }

    fun initView() {
        binding?.btStop?.click {
            dismiss()
            listener?.clickStop()
        }

        binding?.btContinue?.click {
            dismiss()
            listener?.clickContinue()
        }
    }

    interface ClickListener {
        fun clickStop()

        fun clickContinue()
    }

    override fun onClick(p0: View?) {
        TODO("Not yet implemented")
    }
}
