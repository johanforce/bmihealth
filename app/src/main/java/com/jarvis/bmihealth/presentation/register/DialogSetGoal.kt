package com.jarvis.bmihealth.presentation.register;

import android.content.Context;
import android.view.View;

import com.jarvis.bmihealth.R;
import com.jarvis.bmihealth.databinding.DialogGoalBinding;
import com.jarvis.bmihealth.presentation.pref.AppPreference;
import com.jarvis.design_system.dialog.BaseAlertDialog;

public class DialogSetGoal extends BaseAlertDialog<DialogGoalBinding> implements View.OnClickListener {
    private static final String PACKAGENAME = "com.facebook.katana";
    private final Context context;
    private int temp;
    private final boolean isKmSetting;
    private GoalModel goalModel;
    private final AppPreference appPreference;
    private ListenerClick listenerClick;
    private final boolean isClickBackGround = true;

    public DialogSetGoal(Context context, int index, boolean isKmS) {
        super(context, R.layout.dialog_goal);
        this.context = context;
        this.listenerOnClick();
        this.appPreference = AppPreference.getInstance();
        this.isKmSetting = isKmS;
        binding.rvGoal.initView(context, this.isKmSetting, index);
//        binding.rvGoal.setDataChoose(index);
        binding.rvGoal.setListenerPosition(new ViewGoalEdit.OnListenerPosition() {
            @Override
            public void positionItem(int position) {
                temp = position;
            }

        });
    }

    public void onListener(ListenerClick listenerClick) {
        this.listenerClick = listenerClick;
    }

    private void listenerOnClick() {
        this.binding.btCancel.setOnClickListener(this);
        this.binding.btSave.setOnClickListener(this);
    }

    @Override
    protected void onShowDialog() {

    }

    @Override
    protected void onDismissDialog() {
        if (listenerClick != null) {
            listenerClick.closeDialog(isClickBackGround);
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btCancel){
            dismiss();
        }else if(v.getId() == R.id.btSave){
            listenerClick.saveGoal(temp);
            dismiss();
        }
    }



    public interface ListenerClick {
        void closeDialog(boolean isClickBackgroud);

        void saveGoal(int goal);
    }

}
