package com.jarvis.bmihealth.presentation.register;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jarvis.bmihealth.R;
import com.jarvis.bmihealth.databinding.ViewRpeBinding;
import com.jarvis.bmihealth.presentation.pref.AppPreference;
import com.warkiz.widget.IndicatorSeekBar;
import com.warkiz.widget.OnSeekChangeListener;
import com.warkiz.widget.SeekParams;

public class ViewRPE extends ConstraintLayout {
    private ViewRpeBinding binding;
    int currentRPE = 3;
    private Context context;
    private boolean isKmSetting;
    private AppPreference appPreference;
    private String stringTitle, stringDes;
    private ListenerClick listenerClick;
    private  int goalTemp = -1;
    private  int activityTemp = -1;
    public ViewRPE(@NonNull Context context) {
        super(context);
    }

    public ViewRPE(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewRPE(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ViewRPE(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void onListener(ListenerClick listenerClick) {
        this.listenerClick = listenerClick;
    }


    public void init(Context context, boolean isSetting, int activity, int goal) {
        this.context = context;
        goalTemp = goal;
        appPreference = AppPreference.getInstance();
        isKmSetting = isSetting;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.binding = ViewRpeBinding.inflate(layoutInflater, this, true);
        this.currentRPE = activity;
        setActivityLevel(activity);
        updateRPE(activity);
        this.binding.sbEffortNew.setOnSeekChangeListener(new OnSeekChangeListener() {
            @Override
            public void onSeeking(SeekParams seekParams) {
//                if (seekParams.progress < 2) {
//                    binding.sbEffortNew.setProgress(1f);
//                    stringTitle = getContext().getString(R.string.rpe_sedentary);
//                    stringDes = getContext().getString(R.string.rpe_sedentary_des);
//                    setValueRPE(stringTitle, stringDes);
//                }
//
//                LogUtil.INSTANCE.ct("-----progess------" + seekParams.progress);
//                LogUtil.INSTANCE.ct("-----level------" + progessToLevel(seekParams.progress));
//                updateRPE(seekParams.progress);
//                if (listenerClick != null) {
//                    listenerClick.dataActivityLevel(progessToLevel(seekParams.progress));
//                }

                if (seekParams.thumbPosition < 1) {
                    binding.sbEffortNew.setProgress(1f);
                    stringTitle = getContext().getString(R.string.rpe_sedentary);
                    stringDes = getContext().getString(R.string.rpe_sedentary_des);
                    setValueRPE(stringTitle, stringDes);
                }

                if (seekParams.thumbPosition < 1) {
                    activityTemp = 1;
                    updateRPE(1);
                } else {
                    activityTemp = seekParams.thumbPosition;
                    updateRPE(seekParams.thumbPosition);
                }
                if (listenerClick != null) {
                    listenerClick.dataActivityLevel(activityTemp);
                }

            }

            @Override
            public void onStartTrackingTouch(IndicatorSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(IndicatorSeekBar seekBar) {
            }
        });
        setTextGoal(goalTemp, isKmSetting);
        this.binding.viewGoal.setOnListenerClick(() -> {
//            listenerClick.onDialogClick();
            DialogSetGoal dialogSetGoal = new DialogSetGoal(context, goalTemp, isKmSetting);
            dialogSetGoal.show();
            dialogSetGoal.onListener(new DialogSetGoal.ListenerClick() {
                @Override
                public void closeDialog(boolean isClickBackgroud) {
                }

                @Override
                public void saveGoal(int goal) {
                    if (listenerClick != null) {
                        goalTemp= goal;
                        listenerClick.dataGoal(goal);
                    }
                    setTextGoal(goal, isKmSetting);
                }
            });
        });
    }

    private int progessToLevel(int progess) {
        if (progess < 2) {
            return 1;
        } else if (progess < 4) {
            return 1;
        } else if (progess < 6) {
            return 2;
        } else if (progess < 8) {
            return 3;
        } else if (progess < 10) {
            return 4;
        } else {
            return 5;
        }
    }

    public int getCurrentRPE() {
        return currentRPE;
    }

    public void updateRPE(int rpe) {
        switch (rpe) {
            case 1:
                stringTitle = getContext().getString(R.string.rpe_sedentary);
                stringDes = getContext().getString(R.string.rpe_sedentary_des);
                setValueRPE(stringTitle, stringDes);
                break;
            case 2:
                stringTitle = getContext().getString(R.string.rpe_lightly);
                stringDes = getContext().getString(R.string.rpe_lightly_des);
                setValueRPE(stringTitle, stringDes);
                break;
            case 3:
                stringTitle = getContext().getString(R.string.rpe_moderately);
                stringDes = getContext().getString(R.string.rpe_moderately_des);
                setValueRPE(stringTitle, stringDes);
                break;
            case 4:
                stringTitle = getContext().getString(R.string.rpe_very_active);
                stringDes = getContext().getString(R.string.rpe_very_active_des);
                setValueRPE(stringTitle, stringDes);
                break;
            case 5:
                stringTitle = getContext().getString(R.string.rpe_extremely);
                stringDes = getContext().getString(R.string.rpe_extremely_des);
                setValueRPE(stringTitle, stringDes);
                break;

        }
        this.currentRPE = rpe;
    }

    public void setValueRPE(String title, String des) {
        this.binding.tvDesStatus.setText(des);
        this.binding.tvStatus.setText(title);

    }

    public void setUnit(boolean unit) {
        this.isKmSetting = unit;
    }

    public void setShowDialogUnit(int index, boolean isSetting) {
        this.binding.viewGoal.setOnClickListener(view -> {
            DialogSetGoal dialogSetGoal = new DialogSetGoal(context, index, isKmSetting);
            dialogSetGoal.show();
            dialogSetGoal.onListener(new DialogSetGoal.ListenerClick() {
                @Override
                public void closeDialog(boolean isClickBackgroud) {
                }

                @Override
                public void saveGoal(int goal) {
                    if (listenerClick != null) {
                        listenerClick.dataGoal(goal);
                    }
                }
            });
        });
    }

    public interface ListenerClick {
        void dataActivityLevel(int level);

        void dataGoal(int goal);

        void onDialogClick();
    }

    public void setActivityLevel(int level) {
        this.binding.sbEffortNew.setProgress(level * 2);
    }

    public void setTextGoal(int temp, boolean isSetting) {
        String unit;
        String valueWeight;
        String des;
        if (isSetting) unit = context.getString(R.string.unit_kg);
        else {
            unit = context.getString(R.string.unit_lbs);
        }
        switch (temp) {
            case 1:
                valueWeight = isSetting ? "1" : "2.2";
                des = "(-" + valueWeight + unit + "/" + context.getString(R.string.all_week).toLowerCase() + ")";
                binding.viewGoalTitle.setText(context.getString(R.string.onboarding_strict_loos));
                binding.viewGoalDes.setText(des);
                binding.viewGoalDes.setVisibility(VISIBLE);
                break;
            case 2:
                valueWeight = isSetting ? "0.5" : "1.1";
                des = "(-" + valueWeight + unit + "/" + context.getString(R.string.all_week).toLowerCase() + ")";
                binding.viewGoalTitle.setText(context.getString(R.string.onboarding_mormal_weight));
                binding.viewGoalDes.setText(des);
                binding.viewGoalDes.setVisibility(VISIBLE);

                break;
            case 3:
                valueWeight = isSetting ? "0.25" : "0.55";
                des = "(-" + valueWeight + unit + "/" + context.getString(R.string.all_week).toLowerCase() + ")";
                binding.viewGoalTitle.setText(context.getString(R.string.onboarding_comfortable));
                binding.viewGoalDes.setText(des);
                binding.viewGoalDes.setVisibility(VISIBLE);
                break;
            case 4:
                valueWeight = isSetting ? "" : "";
                des = "";
                binding.viewGoalTitle.setText(context.getString(R.string.onboarding_maintain));
                binding.viewGoalDes.setVisibility(GONE);
                break;
            case 5:
                valueWeight = isSetting ? "0.5" : "1.1";
                des = "(+" + valueWeight + unit + "/" + context.getString(R.string.all_week).toLowerCase() + ")";
                binding.viewGoalTitle.setText(context.getString(R.string.onboarding_normal));
                binding.viewGoalDes.setText(des);
                binding.viewGoalDes.setVisibility(VISIBLE);
                break;
            case 6:
                valueWeight = isSetting ? "1" : "2.2";
                des = "(+" + valueWeight + unit + "/" + context.getString(R.string.all_week).toLowerCase() + ")";
                binding.viewGoalTitle.setText(context.getString(R.string.onboarding_strict));
                binding.viewGoalDes.setText(des);
                binding.viewGoalDes.setVisibility(VISIBLE);
                break;
        }
    }

}

