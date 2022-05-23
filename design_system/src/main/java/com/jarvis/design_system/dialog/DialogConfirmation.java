package com.jarvis.design_system.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.jarvis.design_system.R;
import com.jarvis.design_system.databinding.DialogConfirmationBinding;


/**
 * Developed by Domingo Luan on 08/12/2018 Copyright © 2018.
 */
public class DialogConfirmation extends BaseAlertDialog<DialogConfirmationBinding> {
    public static int STYLE_NORMAL = 1;
    public static int STYLE_WARNING = 2;
    private OnConfirmDeleteDialogListener onConfirmDeleteDialogListener;
    private String description;
    private String title;
    private String positive, negative;
    private int dialogStyle;
    private int resPositiveColor;

    public DialogConfirmation(Context context) {
        super(context, R.layout.dialog_confirmation);
        this.listenerOnClick();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTextPositive(String positive) {
        this.positive = positive;
    }

    public void setTextNegative(String negative) {
        this.negative = negative;
    }

    public void setTitle(int titleResId) {
        this.title = getContext().getString(titleResId);
    }

    public void setDescription(int descriptionResId) {
        this.description = getContext().getString(descriptionResId);
    }

    public void setTextPositive(int positiveResId) {
        this.positive = getContext().getString(positiveResId);
    }

    public void setTextNegative(int negativeResId) {
        this.negative = getContext().getString(negativeResId);
    }

    public void setTextPositiveColor(int resPositiveColor) {
        this.resPositiveColor = resPositiveColor;
    }

    public void setDialogStyle(int style) {
        this.dialogStyle = style;
    }

    private void listenerOnClick() {
        this.binding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmDeleteDialogListener != null) {
                    onConfirmDeleteDialogListener.onCancelDialog();
                }
                dismiss();
            }
        });
        this.binding.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmDeleteDialogListener != null) {
                    onConfirmDeleteDialogListener.onConfirmDeleteDialog();
                }
                dismiss();
            }
        });
    }

    public void setOnConfirmDeleteDialogListener(OnConfirmDeleteDialogListener onConfirmDeleteDialogListener) {
        this.onConfirmDeleteDialogListener = onConfirmDeleteDialogListener;
    }

    @Override
    protected void onShowDialog() {
        if (this.binding == null) {
            return;
        }
        if (this.dialogStyle == STYLE_NORMAL) {
            this.binding.tvConfirm.setTextColor(ContextCompat.getColor(getContext(), R.color.pri_4));
            this.binding.tvConfirm.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.selector_button_plain_2));
        }
        if (!TextUtils.isEmpty(this.description)) {
            this.binding.tvDes.setText(this.description);
        }
        if (!TextUtils.isEmpty(this.title)) {
            this.binding.tvTitle.setVisibility(View.VISIBLE);
            this.binding.tvTitle.setText(this.title);
        } else {
            this.binding.tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(this.positive)) {
            this.binding.tvConfirm.setText(this.positive);
        }
        if (!TextUtils.isEmpty(this.negative)) {
            this.binding.tvCancel.setText(this.negative);
        }
        if (this.resPositiveColor > 0) {
            this.binding.tvConfirm.setTextColor(ContextCompat.getColor(getContext(), resPositiveColor));
        }
    }

    @Override
    protected void onDismissDialog() {

    }

    public interface OnConfirmDeleteDialogListener {
        void onCancelDialog();

        void onConfirmDeleteDialog();
    }
}
