package com.jarvis.design_system.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.LayoutRes;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

/**
 * Created by Tunv on 07/04/2020.
 */
public abstract class BaseAlertDialog<BD extends ViewDataBinding> {
    protected BD binding;
    private AlertDialog mDialog;
    private Context context;
    private AlertDialog.Builder mDialogBuilder;

    public BaseAlertDialog(Context context, @LayoutRes int layoutId) {
        this.context = context;
        this.mDialogBuilder = new AlertDialog.Builder(context);
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false);
    }

    public void show(boolean isCancelable, boolean showKeyboard) {
        mDialogBuilder.setView(binding.getRoot());

        if (mDialog == null) {
            mDialog = mDialogBuilder.create();
            mDialog.setOnDismissListener(dialog -> onDismissDialog());
        }

        mDialog.setCanceledOnTouchOutside(isCancelable);
        mDialog.setCancelable(isCancelable);

        try {
            Window window = mDialog.getWindow();
            if (window != null) {
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (showKeyboard) {
                    window.setSoftInputMode(5);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Activity activity = null;
        try {
            activity = ((Activity) context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (activity != null && activity.isFinishing()) {
            return;
        }

        try {
            mDialog.show();
        } catch (WindowManager.BadTokenException e) {
            //use a log message
        }

        onShowDialog();
    }

    protected void setCancelAble(boolean cancelAble) {
        if (mDialog != null) {
            mDialog.setCancelable(cancelAble);
        }
    }

    protected abstract void onShowDialog();

    protected abstract void onDismissDialog();

    public void show() {
        Activity activity = null;
        try {
            activity = ((Activity) context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (activity != null && activity.isFinishing()) {
            return;
        }
        this.show(true, false);
    }

    public boolean isShowing() {
        if (mDialog != null && mDialog.isShowing()) {
            return mDialog.isShowing();
        }
        return false;
    }

    public void dismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public void hide() {
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    public Context getContext() {
        return context;
    }
}
