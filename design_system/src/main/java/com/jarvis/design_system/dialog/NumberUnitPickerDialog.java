package com.jarvis.design_system.dialog;

import android.graphics.Typeface;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.jarvis.design_system.R;
import com.jarvis.design_system.bottomsheet.JxBottomSheetHeader;
import cn.carbswang.android.numberpickerview.library.NumberPickerView;

public class NumberUnitPickerDialog extends BaseFragmentDialog {
    private NumberPickerView pickerViewLeft;
    private NumberPickerView pickerViewRight;
    private TextView tvUnit;
    private JxBottomSheetHeader viewHeader;
    private TextView tvFoot;
    private TextView tvOkay;
    private TextView tvCancel;
    private TextView tvChangeUnit;
    private View viewChangeUnit;
    private int typeUnit;
    private Pair<Integer, Integer> currentValue;
    private OnDialogClickListener listener;
    private String title, negativeButtonText, positiveButtonText;
    private NumberPickerModel pickerModelUnitOne, pickerModelUnitTwo;
    private Pair<Integer, Integer> valueNumberUnitOne, valueNumberUnitTwo;

    public NumberUnitPickerDialog() {
    }

    private NumberUnitPickerDialog(Builder builder) {
        this.listener = builder.listener;
        this.typeUnit = builder.typeUnit;
        this.currentValue = builder.currentValue;
        this.title = builder.title;
        this.negativeButtonText = builder.negativeButtonText;
        this.positiveButtonText = builder.positiveButtonText;
        this.pickerModelUnitOne = builder.pickerModelUnitOne;
        this.pickerModelUnitTwo = builder.pickerModelUnitTwo;
        if (this.pickerModelUnitOne == null){
            this.pickerModelUnitOne = new NumberPickerModel();
        }
        if (this.pickerModelUnitTwo == null){
            this.pickerModelUnitTwo = new NumberPickerModel();
        }
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_number_picker;
    }

    @Override
    protected void initView(View contentView) {
        this.pickerViewLeft = contentView.findViewById(R.id.npv_number1);
        this.pickerViewRight = contentView.findViewById(R.id.npv_number2);
        this.tvUnit = contentView.findViewById(R.id.tv_unit);
        this.viewHeader = contentView.findViewById(R.id.viewHeader);
        this.tvOkay = contentView.findViewById(R.id.tvPositive);
        this.tvCancel = contentView.findViewById(R.id.tvNegative);
        this.tvFoot = contentView.findViewById(R.id.tv_foot);
        this.viewChangeUnit = contentView.findViewById(R.id.view_change_unit);
        this.tvChangeUnit = contentView.findViewById(R.id.tv_change_unit);
        this.setUpLayout();

        this.viewChangeUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentValue = getCurrentValue();
                changeUnit();
            }
        });

        this.tvOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onPositionClick(getCurrentValue(), typeUnit);
                }
            }
        });

        this.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        Typeface typefaceMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/GoogleSans-Medium.otf");
        if (this.pickerViewLeft != null) {
            this.pickerViewLeft.setContentTextTypeface(typefaceMedium);
        }
        if (this.pickerViewRight != null) {
            this.pickerViewRight.setContentTextTypeface(typefaceMedium);
        }
    }

    private void setUpLayout() {
        if (this.viewHeader != null) {
            this.viewHeader.setTitle(this.title);
        }
        if (this.tvCancel != null) {
            this.tvCancel.setText(negativeButtonText);
        }

        if (this.tvOkay != null) {
            this.tvOkay.setText(positiveButtonText);
        }
        if (this.tvUnit != null) {
            this.tvUnit.setVisibility(View.VISIBLE);
        }
        if (this.tvFoot != null) {
            this.tvFoot.setVisibility(View.VISIBLE);
        }
        this.initPicker(this.currentValue);
        this.setCurrentPickerValue();
        this.pickerViewLeft.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                int value = pickerViewRight.getValue();
                NumberPickerModel currentPickerModel = (typeUnit == pickerModelUnitOne.getUnit()) ? pickerModelUnitOne : pickerModelUnitTwo;

                if (newVal == currentPickerModel.getMaxValue().first) {
                    if (value > currentPickerModel.getMaxValue().second || value < currentPickerModel.getMaxValue().second) {
                        value = currentPickerModel.getMaxValue().second;
                    }
                } else if (newVal == currentPickerModel.getMinValue().first) {
                    if (value > currentPickerModel.getMinValue().second || value < currentPickerModel.getMinValue().second) {
                        value = currentPickerModel.getMinValue().second;
                    }
                }

                if (typeUnit == pickerModelUnitOne.getUnit()) {
                    valueNumberUnitOne = new Pair<>(newVal, pickerModelUnitOne.getTotalPicker() == 1 ? 0 : value);
                    valueNumberUnitTwo = null;
                } else {
                    valueNumberUnitTwo = new Pair<>(newVal, pickerModelUnitTwo.getTotalPicker() == 1 ? 0 : value);
                    valueNumberUnitOne = null;
                }
                if (oldVal < newVal) {
                    Pair<Integer, Integer> valueRangeForPickerRight = currentPickerModel.getValueRange().get(1);
                    if (newVal == currentPickerModel.getMaxValue().first) {
                        //=> Update Picker Right by max range
                        applyPickerValueRange(pickerViewRight, valueRangeForPickerRight.first, currentPickerModel.getMaxValue().second);
                    } else {
                        applyPickerValueRange(pickerViewRight, valueRangeForPickerRight.first, valueRangeForPickerRight.second);
                    }
                } else if (oldVal > newVal) {
                    Pair<Integer, Integer> valueRangeForPickerRight = currentPickerModel.getValueRange().get(1);
                    if (newVal == currentPickerModel.getMinValue().first) {
                        //=> Update Picker Right by max range
                        applyPickerValueRange(pickerViewRight, currentPickerModel.getMinValue().second, valueRangeForPickerRight.second);
                    } else {
                        applyPickerValueRange(pickerViewRight, valueRangeForPickerRight.first, valueRangeForPickerRight.second);
                    }
                }
            }
        });
        this.pickerViewRight.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                int value = pickerViewLeft.getValue();
                if (typeUnit == pickerModelUnitOne.getUnit()) {
                    valueNumberUnitOne = new Pair<>(value, pickerModelUnitOne.getTotalPicker() == 1 ? 0 : newVal);
                    valueNumberUnitTwo = null;
                } else {
                    valueNumberUnitTwo = new Pair<>(value, pickerModelUnitTwo.getTotalPicker() == 1 ? 0 : newVal);
                    valueNumberUnitOne = null;
                }
            }
        });
    }
    private void initPicker(Pair<Integer, Integer> currentValue) {
        NumberPickerModel numberPickerModel;
        if (pickerModelUnitOne == null){
            pickerModelUnitOne = new NumberPickerModel();
        }
        if (this.typeUnit == pickerModelUnitOne.getUnit()) {
            numberPickerModel = pickerModelUnitOne;
        } else {
            numberPickerModel = pickerModelUnitTwo;
        }
        this.pickerViewLeft.setVisibility(View.VISIBLE);
        this.tvFoot.setVisibility((numberPickerModel.getTotalPicker() == 2) ? View.VISIBLE : View.GONE);
        this.pickerViewRight.setVisibility((numberPickerModel.getTotalPicker() == 2) ? View.VISIBLE : View.GONE);

        int totalPicker = numberPickerModel.getTotalPicker();

        for (int i = 0; i < totalPicker; i++) {
            Pair<Integer, Integer> valueRange = numberPickerModel.getValueRange().get(i);
            if (i == 0) {
                applyPickerValueRange(pickerViewLeft, valueRange.first, valueRange.second);
            } else {
                if (currentValue.first.compareTo(numberPickerModel.getMaxValue().first) == 0) {
                    //=> Max
                    applyPickerValueRange(pickerViewRight, valueRange.first, numberPickerModel.getMaxValue().second);
                } else if (currentValue.first.compareTo(numberPickerModel.getMinValue().first) == 0) {
                    //=> Min
                    applyPickerValueRange(pickerViewRight, numberPickerModel.getMinValue().second, valueRange.second);
                } else {
                    applyPickerValueRange(pickerViewRight, valueRange.first, valueRange.second);
                }
            }
        }
        this.tvChangeUnit.setText(numberPickerModel.getUnitTitle());
        this.tvFoot.setText(numberPickerModel.getSplitText());
        this.tvUnit.setText(numberPickerModel.getUnitText());

        if (currentValue.first < numberPickerModel.getMinValue().first) {
            this.pickerViewLeft.setValue(numberPickerModel.getMinValue().first);
        } else {
            if (currentValue.first < numberPickerModel.getMaxValue().first) {
                this.pickerViewLeft.setValue(currentValue.first);
            } else {
                this.pickerViewLeft.setValue(numberPickerModel.getMaxValue().first);
            }
        }
        this.pickerViewRight.setValue(currentValue.second);
    }

    private void applyPickerValueRange(NumberPickerView numberPickerView, int min, int max) {
        String[] pickerDisplayValue = new String[max - min + 1];
        for (int value = min; value <= max; value++) {
            pickerDisplayValue[value - min] = String.valueOf(value);
        }
        if (pickerDisplayValue.length < (numberPickerView.getMaxValue() - numberPickerView.getMinValue() + 1)) {
            refreshPicker(numberPickerView);
        }
        numberPickerView.refreshByNewDisplayedValues(pickerDisplayValue);
        numberPickerView.setMinValue(min);
        numberPickerView.setMaxValue(max);
    }

    private void setCurrentPickerValue() {
        if (currentValue.first >= pickerViewLeft.getMinValue() && currentValue.first <= pickerViewLeft.getMaxValue()) {
            pickerViewLeft.setValue(currentValue.first);
        }

        if (currentValue.second >= pickerViewRight.getMinValue() && currentValue.second <= pickerViewRight.getMinValue()) {
            pickerViewRight.setValue(currentValue.second);
        }
        if (this.typeUnit == this.pickerModelUnitOne.getUnit()) {
            valueNumberUnitOne = new Pair<>(currentValue.first, currentValue.second);
        } else {
            valueNumberUnitTwo = new Pair<>(currentValue.first, currentValue.second);
        }
    }

    private void changeUnit() {
        try {
            Pair<Integer, Integer> convertedValue = new Pair<>(0, 0);
            if (this.typeUnit == pickerModelUnitOne.getUnit()) {
                this.typeUnit = pickerModelUnitTwo.getUnit();
                if (listener != null) {
                    if (this.valueNumberUnitTwo != null) {
                        convertedValue = new Pair<>(valueNumberUnitTwo.first, valueNumberUnitTwo.second);
                    } else {
                        convertedValue = listener.convertValueFromUnitOneToUnitTwo(this.currentValue);
                        this.valueNumberUnitTwo = new Pair<>(convertedValue.first, convertedValue.second);
                    }
                }
            } else {
                this.typeUnit = pickerModelUnitOne.getUnit();
                if (listener != null) {
                    if (this.valueNumberUnitOne != null) {
                        convertedValue = new Pair<>(valueNumberUnitOne.first, valueNumberUnitOne.second);
                    } else {
                        convertedValue = listener.convertValueFromUnitTwoToUnitOne(this.currentValue);
                        this.valueNumberUnitOne = new Pair<>(convertedValue.first, convertedValue.second);
                    }
                }
            }
            this.initPicker(convertedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void show(FragmentManager fragmentManager) {
        this.show(fragmentManager, "NumberUnitPickerDialog");
    }

    public Pair<Integer, Integer> getCurrentValue() {
        if (this.typeUnit == this.pickerModelUnitOne.getUnit()) {
            int leftValue = this.pickerViewLeft.getValue();
            int rightValue = this.pickerModelUnitOne.getTotalPicker() == 2 ? this.pickerViewRight.getValue() : 0;
            return new Pair<>(leftValue, rightValue);
        }
        if (this.typeUnit == this.pickerModelUnitTwo.getUnit()) {
            int leftValue = this.pickerViewLeft.getValue();
            int rightValue = this.pickerModelUnitTwo.getTotalPicker() == 2 ? this.pickerViewRight.getValue() : 0;
            return new Pair<>(leftValue, rightValue);
        }
        return new Pair<>(0, 0);
    }

    private void refreshPicker(NumberPickerView numberPickerView) {
        try {
            numberPickerView.setMinValue(0);
            numberPickerView.setMaxValue(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnDialogClickListener {
        void onPositionClick(Pair<Integer, Integer> value, int typeUnit);

        Pair<Integer, Integer> convertValueFromUnitOneToUnitTwo(Pair<Integer, Integer> unitOneValue);

        Pair<Integer, Integer> convertValueFromUnitTwoToUnitOne(Pair<Integer, Integer> unitTwoValue);
    }

    public static class Builder {
        private String title, negativeButtonText, positiveButtonText;
        private int typeUnit;
        private Pair<Integer, Integer> currentValue;
        private OnDialogClickListener listener;
        private NumberPickerModel pickerModelUnitOne, pickerModelUnitTwo;

        public Builder(int typeUnit) {
            this.typeUnit = typeUnit;
        }

        public Builder(int typeUnit, Pair<Integer, Integer> currentValue) {
            this.typeUnit = typeUnit;
            this.currentValue = currentValue;
        }

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

        public Builder setPickerModelUnitOne(NumberPickerModel pickerModelUnitOne) {
            this.pickerModelUnitOne = pickerModelUnitOne;
            return this;
        }

        public Builder setPickerModelUnitTwo(NumberPickerModel pickerModelUnitTwo) {
            this.pickerModelUnitTwo = pickerModelUnitTwo;
            return this;
        }

        public Builder setCurrentValue(Pair<Integer, Integer> currentValue) {
            this.currentValue = currentValue;
            return this;
        }

        public Builder setTypeUnit(int typeUnit) {
            this.typeUnit = typeUnit;
            return this;
        }

        public Builder setOnActionClickListener(OnDialogClickListener listener) {
            this.listener = listener;
            return this;
        }

        public NumberUnitPickerDialog builder() {
            return new NumberUnitPickerDialog(this);
        }
    }
}
