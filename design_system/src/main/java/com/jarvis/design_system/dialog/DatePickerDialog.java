package com.jarvis.design_system.dialog;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.jarvis.design_system.R;
import com.jarvis.design_system.bottomsheet.JxBottomSheetHeader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;

public class DatePickerDialog extends BaseFragmentDialog {
    private SimpleDateFormat sdfToYear = new SimpleDateFormat("yyyy", Locale.getDefault());
    private SimpleDateFormat sdfToMonth = new SimpleDateFormat("MM", Locale.getDefault());
    private SimpleDateFormat sdfToDay = new SimpleDateFormat("dd", Locale.getDefault());

    private NumberPickerView pickerView1;
    private NumberPickerView pickerView2;
    private NumberPickerView pickerView3;
    private JxBottomSheetHeader viewHeader;
    private TextView tvOkay;
    private TextView tvCancel;

    private int day;
    private int month;
    private int year;

    private long birthday;
    private String negativeButtonText, positiveButtonText;
    private OnDialogClickDate onDialogClickDate;
    private String title;
    private List<String> listMonth = new ArrayList();

    public DatePickerDialog(Builder builder) {
        this.birthday = builder.birthday;
        this.title = builder.title;
        this.negativeButtonText = builder.negativeButtonText;
        this.positiveButtonText = builder.positiveButtonText;
        this.onDialogClickDate = builder.onDialogClickDate;
    }

    public List<String> getListMonth(int year, int month) {
        SimpleDateFormat fmt = new SimpleDateFormat("MMM");
        List listMonth = new ArrayList();
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(year, month - 1, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.MONTH);
        for (int i = 0; i <= daysInMonth; i++) {
            listMonth.add(fmt.format(cal.getTime()));
            cal.add(Calendar.MONTH, 1);
        }
        return listMonth;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_date_picker;
    }

    @Override
    protected void initView(View contentView) {
        this.listMonth = this.getListMonth(2021, 1);
        this.pickerView1 = contentView.findViewById(R.id.npv_number1);
        this.pickerView2 = contentView.findViewById(R.id.npv_number2);
        this.pickerView3 = contentView.findViewById(R.id.npv_number3);
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
                if (onDialogClickDate != null) {
                    onDialogClickDate.onPositionCLickDate(birthday);
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
        if (this.pickerView3 != null) {
            this.pickerView3.setContentTextTypeface(typefaceMedium);
        }
        initViewData();
    }

    public void show(FragmentManager fragmentManager) {
        this.show(fragmentManager, "DatePickerDialog");
    }

    private void setUpValuePicker1(int sizeValue, int minValue) {
        try {
            if (sizeValue < 1) {
                return;
            }
            String[] arrDay = new String[sizeValue];
            for (int i = 0; i < sizeValue; i++) {
                if (i < 9) {
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

    private void setUpValuePicker2(int sizeValue) {
        try {
            if (sizeValue < 1) {
                return;
            }
            String[] arrMonth = new String[sizeValue];
            for (int i = 0; i < sizeValue; i++) {
                arrMonth[i] = listMonth.get(i);
            }

            this.pickerView2.setDisplayedValues(arrMonth);
            this.pickerView2.refreshByNewDisplayedValues(arrMonth);
            this.pickerView2.setMinValue(0);
            this.pickerView2.setMaxValue(sizeValue - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpValuePicker3(int sizeValue, int minValue) {

        try {
            if (sizeValue < 1) {
                return;
            }
            String[] arrYear = new String[sizeValue];
            for (int i = 0; i < sizeValue; i++) {
                arrYear[i] = String.valueOf(i + minValue);
            }
            this.pickerView3.setDisplayedValues(arrYear);
            this.pickerView3.refreshByNewDisplayedValues(arrYear);
            this.pickerView3.setMinValue(0);
            this.pickerView3.setMaxValue(sizeValue - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViewData() {
        try {
            Calendar calendar = Calendar.getInstance();
            int maxYear = Integer.parseInt(sdfToYear.format(calendar.getTime()));
            calendar.setTimeInMillis(birthday);
            year = Integer.parseInt(sdfToYear.format(calendar.getTime()));
            month = Integer.parseInt(sdfToMonth.format(calendar.getTime()));
            day = Integer.parseInt(sdfToDay.format(calendar.getTime()));
            int numberDays = calendar.getActualMaximum(Calendar.DATE);

            this.setUpValuePicker1(numberDays, 1);
            this.setUpValuePicker2(12);
            this.setUpValuePicker3(maxYear - 1899, 1900);

            int positionPicker1 = getPositionPicker1(numberDays, 1);
            int positionPicker2 = getPositionPicker2(12, 1);
            int positionPicker3 = getPositionPicker3(year - 1900, 1900);
            this.setValueCurrent(positionPicker1, positionPicker2, positionPicker3);

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setPickerUnitChange();
    }

    private void setValueCurrent(int positionPicker1, int positionPicker2, int positionPicker3) {
        this.pickerView1.setPickedIndexRelativeToRaw(positionPicker1);
        this.pickerView2.setPickedIndexRelativeToRaw(positionPicker2);
        this.pickerView3.setPickedIndexRelativeToRaw(positionPicker3);
    }

    private int getPositionPicker1(int maxValue, int minValue) {
        int inPart = this.day;
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
        int inPart = this.month;
        int positionPicker = inPart - minValue;
        if (positionPicker < 0) {
            return 0;
        }
        if (positionPicker > maxValue) {
            return maxValue - 1;
        }
        return positionPicker;
    }

    private int getPositionPicker3(int maxValue, int minValue) {
        int inPart = this.year;
        int positionPicker = inPart - minValue;
        if (positionPicker < 0) {
            return 0;
        }
        if (positionPicker > maxValue) {
            return maxValue - 1;
        }
        return positionPicker;
    }

    private void setPickerUnitChange() {
        this.pickerView1.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                day = Integer.parseInt(DatePickerDialog.this.pickerView1.getContentByCurrValue());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                birthday = calendar.getTimeInMillis();
            }
        });
        this.pickerView2.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                try {
                    String contentByCurrValue = DatePickerDialog.this.pickerView2.getContentByCurrValue();
                    if (listMonth.contains(contentByCurrValue)) {
                        month = listMonth.indexOf(contentByCurrValue) + 1;
                    } else {
                        month = 1;
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    birthday = calendar.getTimeInMillis();
                    DatePickerDialog.this.refreshPicker();
                    DatePickerDialog.this.initViewData();
                } catch (Exception e) {
                    month = 1;
                }
            }
        });
        this.pickerView3.setOnValueChangedListener(new NumberPickerView.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPickerView picker, int oldVal, int newVal) {
                year = Integer.parseInt(DatePickerDialog.this.pickerView3.getContentByCurrValue());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month - 1);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                birthday = calendar.getTimeInMillis();
                DatePickerDialog.this.refreshPicker();
                DatePickerDialog.this.initViewData();
            }
        });
    }

    private void refreshPicker() {
        try {
            this.pickerView1.setMinValue(0);
            this.pickerView1.setMaxValue(1);
            this.pickerView2.setMinValue(0);
            this.pickerView2.setMaxValue(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnDialogClickDate {
        void onPositionCLickDate(long birthday);
    }

    public static class Builder {

        private long birthday = Calendar.getInstance().getTimeInMillis();
        private OnDialogClickDate onDialogClickDate;
        private String title;
        private String negativeButtonText, positiveButtonText;

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

        public Builder setBirthday(long birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder setOnDialogClickDate(OnDialogClickDate onDialogClickDate) {
            this.onDialogClickDate = onDialogClickDate;
            return this;
        }

        public DatePickerDialog builder() {
            return new DatePickerDialog(this);
        }
    }
}
