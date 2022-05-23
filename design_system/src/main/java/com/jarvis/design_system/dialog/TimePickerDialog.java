package com.jarvis.design_system.dialog;

import android.graphics.Typeface;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.jarvis.design_system.R;
import com.jarvis.design_system.bottomsheet.JxBottomSheetHeader;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

public class TimePickerDialog extends BaseFragmentDialog {

    private NumberPickerView pickerView1;
    private NumberPickerView pickerView2;
    private JxBottomSheetHeader viewHeader;
    private TextView tvOkay;
    private TextView tvCancel;

    private int hours = 12;
    private int minutes = 12;
    private Pair<Integer, Integer> currentValue;

    private String negativeButtonText, positiveButtonText;
    private OnDialogClickTime onDialogClickTime;
    private String title;

    public TimePickerDialog(Builder builder) {
        this.currentValue = builder.currentValue;
        this.title = builder.title;
        this.negativeButtonText = builder.negativeButtonText;
        this.positiveButtonText = builder.positiveButtonText;
        this.onDialogClickTime = builder.onDialogClickTime;
    }


    @Override
    public int getLayout() {
        return R.layout.dialog_number_time;
    }

    @Override
    protected void initView(View contentView) {
        this.pickerView1 = contentView.findViewById(R.id.npv_number1);
        this.pickerView2 = contentView.findViewById(R.id.npv_number2);
        this.tvOkay = contentView.findViewById(R.id.tvPositive);
        this.tvCancel = contentView.findViewById(R.id.tvNegative);
        this.viewHeader = contentView.findViewById(R.id.viewHeader);

        if (this.viewHeader != null) {
            this.viewHeader.setTitle(title);
        }

        if (this.tvCancel != null) {
            this.tvCancel.setText(negativeButtonText);
        }

        if (this.tvOkay != null) {
            this.tvOkay.setText(positiveButtonText);
        }

        this.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        this.tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDialogClickTime != null) {
                    hours = Integer.parseInt(pickerView1.getContentByCurrValue());
                    minutes = Integer.parseInt(pickerView2.getContentByCurrValue());
                    Pair<Integer, Integer> currentValue = new Pair<>(hours, minutes);
                    onDialogClickTime.onPositionCLickDate(currentValue);
                }
                dismiss();
            }
        });

        Typeface typefaceMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GoogleSans-Medium.otf");
        if (this.pickerView1 != null) {
            this.pickerView1.setContentTextTypeface(typefaceMedium);
        }
        if (this.pickerView2 != null) {
            this.pickerView2.setContentTextTypeface(typefaceMedium);
        }
        initViewData();
    }

    public void show(FragmentManager fragmentManager) {
        this.show(fragmentManager, "TimePickerDialog");
    }

    private void setUpValuePicker1(int sizeValue, int minValue) {
        try {
            if (sizeValue < 1) {
                return;
            }
            String[] arrDay = new String[sizeValue];
            for (int i = 0; i < sizeValue; i++) {
                if (i < 10) {
                    arrDay[i] = 0 + String.valueOf(i + minValue);
                } else {
                    arrDay[i] = String.valueOf(i + minValue);
                }
            }
            this.pickerView1.setDisplayedValues(arrDay);
            this.pickerView1.refreshByNewDisplayedValues(arrDay);
            this.pickerView1.setMinValue(0);
            this.pickerView1.setMaxValue(sizeValue - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpValuePicker2(int sizeValue, int minValue) {
        try {
            if (sizeValue < 1) {
                return;
            }
            String[] arrMonth = new String[sizeValue];
            for (int i = 0; i < sizeValue; i++) {
                if (i < 10) {
                    arrMonth[i] = 0 + String.valueOf(i + minValue);
                } else {
                    arrMonth[i] = String.valueOf(i + minValue);
                }
            }

            this.pickerView2.setDisplayedValues(arrMonth);
            this.pickerView2.refreshByNewDisplayedValues(arrMonth);
            this.pickerView2.setMinValue(0);
            this.pickerView2.setMaxValue(sizeValue - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViewData() {
        try {
            this.hours = currentValue.first - 1;
            this.minutes = currentValue.second;

            this.setUpValuePicker1(24, 0);
            this.setUpValuePicker2(60, 0);

            int positionPicker1 = getPositionPicker1(24, 0);
            int positionPicker2 = getPositionPicker2(60, 0);
            this.setValueCurrent(positionPicker1, positionPicker2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setValueCurrent(int positionPicker1, int positionPicker2) {
        this.pickerView1.setPickedIndexRelativeToRaw(positionPicker1);
        this.pickerView2.setPickedIndexRelativeToRaw(positionPicker2);
    }

    private int getPositionPicker1(int maxValue, int minValue) {
        int inPart;
        if (hours == 0) {
            inPart = 0;
        } else {
            inPart = this.hours + 1;
        }
        int positionPicker = inPart - minValue;
        if (positionPicker < 0) {
            return 0;
        }
        if (positionPicker > maxValue) {
            return maxValue - 1;
        }
        return positionPicker;
    }

    private int getPositionPicker2(int maxValue, int minValue) {
        int inPart = this.minutes;
        int positionPicker = inPart - minValue;
        if (positionPicker < 0) {
            return 0;
        }
        if (positionPicker > maxValue) {
            return maxValue - 1;
        }
        return positionPicker;
    }

    public interface OnDialogClickTime {
        void onPositionCLickDate(Pair<Integer, Integer> currentValue);
    }

    public static class Builder {

        private OnDialogClickTime onDialogClickTime;
        private String title;
        private String negativeButtonText, positiveButtonText;
        private Pair<Integer, Integer> currentValue;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setNegativeButtonText(String text) {
            this.negativeButtonText = text;
            return this;
        }

        public Builder setPositiveButtonText(String text) {
            this.positiveButtonText = text;
            return this;
        }

        public Builder setCurrentValue(Pair<Integer, Integer> currentValue) {
            this.currentValue = currentValue;
            return this;
        }


        public Builder setOnDialogClickTime(OnDialogClickTime onDialogClickTime) {
            this.onDialogClickTime = onDialogClickTime;
            return this;
        }

        public TimePickerDialog builder() {
            return new TimePickerDialog(this);
        }
    }
}
