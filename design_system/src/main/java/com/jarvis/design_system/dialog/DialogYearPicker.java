package com.jarvis.design_system.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.jarvis.design_system.R;
import com.jarvis.design_system.bottomsheet.JxBottomSheetHeader;
import com.jarvis.design_system.button.JxButton;
import com.jarvis.design_system.dialog.adapter.YearAdapter;
import com.jarvis.design_system.dialog.model.YearDataItem;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class DialogYearPicker extends BaseFragmentDialog {

    private RecyclerView rvListItem;
    private JxBottomSheetHeader viewHeader;
    private JxButton btnNegative, btnPositive;
    private YearAdapter yearAdapter;

    private Integer selectedYear;
    private Integer savedSelectedYear;

    private boolean needResetUI = false;
    private List<YearDataItem> mapYearData = new LinkedList<>();

    private RangeSelectListener listener;
    private Integer availableStartYear, availableEndYear;
    private String dialogTitle, positiveButtonText, negativeButtonText;

    @SuppressLint("SimpleDateFormat")
    public DialogYearPicker(Builder builder) {
        this.listener = builder.listener;
        this.dialogTitle = builder.dialogTitle;
        this.positiveButtonText = builder.positiveButtonText;
        this.negativeButtonText = builder.negativeButtonText;
        this.availableStartYear = builder.availableStartYear;
        this.availableEndYear = builder.availableEndYear;
        this.selectedYear = builder.selectedYear;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_year_picker;
    }

    @Override
    protected void initView(View contentView) {
        this.rvListItem = contentView.findViewById(R.id.rvListItem);
        this.viewHeader = contentView.findViewById(R.id.viewHeader);
        this.btnNegative = contentView.findViewById(R.id.tvNegative);
        this.btnNegative.setText(this.negativeButtonText);
        this.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedSelectedYear != null) {
                    selectedYear = savedSelectedYear;
                } else {
                    selectedYear = null;
                    needResetUI = true;
                }
                dismiss();
            }
        });

        this.btnPositive = contentView.findViewById(R.id.tvPositive);
        this.setDisableBtnPositive();
        this.btnPositive.setText(this.positiveButtonText);
        this.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedYear != null) {
                    if (listener != null) {
                        listener.onSelectedYear(selectedYear);
                    }
                    savedSelectedYear = selectedYear;
                }
                dismiss();
            }
        });

        this.viewHeader.setTitle(this.dialogTitle);

        this.initAvailableYearDefault();
        this.initYearData();
        this.initRecyclerView();

        int yearOffset = availableEndYear - availableStartYear;
        this.rvListItem.setLayoutManager(new GridLayoutManager(getContext(), Math.min(yearOffset, 3)));

        if (this.selectedYear != null || needResetUI) {
            if (this.selectedYear != null) {
                updateUIWithSelectYear(this.selectedYear);
            }
            resetUIWithSelectedYear();
        }

    }

    private void initYearData() {
        if (mapYearData != null && !mapYearData.isEmpty()) {
            return;
        }

        this.mapYearData = new LinkedList<>();
        for (int year = availableStartYear; year <= availableEndYear; year++) {
            mapYearData.add(new YearDataItem(year, YearDataItem.State.NORMAL));
        }
    }

    private void resetUIWithSelectedYear() {
        if (mapYearData == null || mapYearData.isEmpty()) {
            return;
        }

        for (YearDataItem item : mapYearData) {
            if (selectedYear != null && item.getYear() == selectedYear) {
                item.setState(YearDataItem.State.SELECTED);
            } else {
                item.setState(YearDataItem.State.NORMAL);
            }
        }
        if (this.yearAdapter != null) {
            this.yearAdapter.notifyDataSetChanged();
        }
        this.needResetUI = false;
    }

    private void initRecyclerView() {
        this.yearAdapter = new YearAdapter(getContext(), mapYearData);
        this.yearAdapter.setOnItemSelectListener(new YearAdapter.OnItemSelectListener() {
            @Override
            public void onSelect(int position, YearDataItem yearDataItem) {
                if (listener != null) {
                    listener.onSelect(yearDataItem.getYear());
                }

                if (selectedYear == null || yearDataItem.getYear() != selectedYear) {
                    selectedYear = yearDataItem.getYear();
                    updateUIWithSelectYear(selectedYear);
                }
                resetUIWithSelectedYear();
            }
        });
        this.rvListItem.setAdapter(yearAdapter);
    }

    private void initAvailableYearDefault() {
        if (availableStartYear != null && availableEndYear != null) {
            return;
        }
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        this.availableStartYear = currentYear;
        calendar.add(Calendar.YEAR, 1);
        this.availableEndYear = calendar.get(Calendar.YEAR);
    }

    public void updateAvailableRange(Integer startYear, Integer endYear) {
        if (startYear == null || endYear == null) {
            throw new NullPointerException("Invalid parameters");
        }
        if (startYear.compareTo(endYear) > 0) {
            throw new InvalidParameterException("Start month must be less than end month");
        }

        this.availableStartYear = startYear;
        this.availableEndYear = endYear;
        this.mapYearData = new LinkedList<>();
        this.selectedYear = null;
    }

    public void updateSelectedYear(Integer year) {
        if (year == null) {
            throw new NullPointerException("Invalid parameters");
        }

        this.selectedYear = year;
    }

    public void updateUIWithSelectYear(int year) {
        this.viewHeader.setTitle(String.valueOf(year));
        this.setEnableBtnPositive();
    }

    private void setDisableBtnPositive(){
        this.btnPositive.disable();
        this.btnPositive.setTextColorButton(R.color.pri_2);
    }
    private void setEnableBtnPositive(){
        this.btnPositive.enable();
        this.btnPositive.setTextColorButton(R.color.pri_4);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public interface RangeSelectListener {
        void onSelect(int year);

        void onSelectedYear(int year);
    }

    public static class Builder {
        private RangeSelectListener listener;
        private Integer availableStartYear, availableEndYear;
        private Integer selectedYear;
        private String dialogTitle, positiveButtonText, negativeButtonText;

        public Builder() {
        }

        public Builder setListener(RangeSelectListener listener) {
            this.listener = listener;
            return this;
        }

        public void setAvailableRange(Integer startYear, Integer endYear) {
            if (startYear == null || endYear == null) {
                throw new NullPointerException("Invalid parameters");
            }
            if (startYear.compareTo(endYear) > 0) {
                throw new InvalidParameterException("Start month must be less than end month");
            }

            this.availableStartYear = startYear;
            this.availableEndYear = endYear;
        }

        public void setSelectedYear(Integer year) {
            if (year == null) {
                throw new NullPointerException("Invalid parameters");
            }

            this.selectedYear = year;
        }

        public Builder setDialogTitle(String dialogTitle) {
            this.dialogTitle = dialogTitle;
            return this;
        }

        public Builder setPositiveButtonText(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
            return this;
        }

        public Builder setNegativeButtonText(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public DialogYearPicker build() {
            return new DialogYearPicker(this);
        }
    }
}
