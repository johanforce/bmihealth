package com.jarvis.design_system.dialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jarvis.design_system.R;
import com.jarvis.design_system.bottomsheet.JxBottomSheetHeader;
import com.jarvis.design_system.button.JxButton;
import com.jarvis.design_system.dialog.adapter.MonthAdapter;
import com.jarvis.design_system.dialog.model.MonthDataItem;

import java.security.InvalidParameterException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DialogMonthPicker extends BaseFragmentDialog {

    private final String DATE_TIME_PATTERN = "MMM, yyyy";
    private final String TITLE_PLACEHOLDER_WITH_START = "%s - MMM, yyyy";
    private final String TITLE_PLACEHOLDER_WITH_START_END = "%s - %s";

    private SimpleDateFormat sdf;
    private RecyclerView rvListItem;
    private ImageButton btnCalendarBack, btnCalendarNext;
    private TextView tvYear;
    private JxBottomSheetHeader viewHeader;
    private JxButton btnNegative, btnPositive;
    private MonthAdapter monthAdapter;

    private Pair<Integer, Integer> selectedStartMonth, selectedEndMonth;
    private Pair<Integer, Integer> savedSelecteStartMonth, savedSelecteEndMonth;
    private int availableStartYear, availableEndYear, currentYear, currentMonth;
    private int currentSelectYear;
    private boolean needResetUI = false;
    private Map<Integer, List<MonthDataItem>> mapMonthData = new LinkedHashMap<>();

    private RangeSelectListener listener;
    private Pair<Integer, Integer> availableStartMonth, availableEndMonth;
    private String dialogTitle, positiveButtonText, negativeButtonText;

    @SuppressLint("SimpleDateFormat")
    public DialogMonthPicker(Builder builder) {
        this.sdf = new SimpleDateFormat(DATE_TIME_PATTERN);
        this.listener = builder.listener;
        this.dialogTitle = builder.dialogTitle;
        this.positiveButtonText = builder.positiveButtonText;
        this.negativeButtonText = builder.negativeButtonText;
        this.availableStartMonth = builder.availableStartMonth;
        this.availableEndMonth = builder.availableEndMonth;
        this.selectedStartMonth = builder.selectedStartMonth;
        this.selectedEndMonth = builder.selectedEndMonth;
    }

    @Override
    public int getLayout() {
        return R.layout.dialog_month_picker;
    }

    @Override
    protected void initView(View contentView) {
        this.rvListItem = contentView.findViewById(R.id.rvListItem);
        this.tvYear = contentView.findViewById(R.id.tvYear);
        this.viewHeader = contentView.findViewById(R.id.viewHeader);
        this.btnNegative = contentView.findViewById(R.id.tvNegative);
        this.btnNegative.setText(this.negativeButtonText);
        this.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedSelecteStartMonth != null && savedSelecteEndMonth != null) {
                    selectedStartMonth = new Pair<>(savedSelecteStartMonth.first, savedSelecteStartMonth.second);
                    selectedEndMonth = new Pair<>(savedSelecteEndMonth.first, savedSelecteEndMonth.second);
                } else {
                    selectedStartMonth = null;
                    selectedEndMonth = null;
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
                if (selectedStartMonth != null && selectedEndMonth != null) {
                    if (listener != null) {
                        listener.onSelectRange(selectedStartMonth, selectedEndMonth);
                    }
                    savedSelecteStartMonth = new Pair<>(selectedStartMonth.first, selectedStartMonth.second);
                    savedSelecteEndMonth = new Pair<>(selectedEndMonth.first, selectedEndMonth.second);
                }
                dismiss();
            }
        });
        this.btnCalendarBack = contentView.findViewById(R.id.btnCalendarBack);
        this.btnCalendarNext = contentView.findViewById(R.id.btnCalendarNext);
        this.btnCalendarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectYear <= availableStartYear) {
                    return;
                }
                selectMonthInYear(currentSelectYear - 1);
            }
        });
        this.btnCalendarNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectYear >= availableEndYear) {
                    return;
                }
                selectMonthInYear(currentSelectYear + 1);
            }
        });

        this.viewHeader.setTitle(this.dialogTitle);

        this.initAvailableMonthDefault();
        this.initMonthData();

        this.rvListItem.setLayoutManager(new GridLayoutManager(getContext(), 3));

        int selectYear = availableStartYear;
        if ((this.selectedStartMonth != null && this.selectedEndMonth != null) || needResetUI) {
            if (this.selectedStartMonth != null && this.selectedEndMonth != null) {
                updateUIWithSelectStartEnd(this.selectedStartMonth, this.selectedEndMonth);
                selectYear = selectedStartMonth.second;
            }
            resetUIWithSelectedMonth();
        } else {
            this.selectedStartMonth = null;
            this.selectedEndMonth = null;
        }
        selectMonthInYear(selectYear);
    }

    //Pair<Integer, Integer> availableStartMonth, Pair<Integer, Integer> availableEndMonth
    private void initMonthData() {
        if (mapMonthData != null && !mapMonthData.isEmpty()) {
            return;
        }
        int startMonth = availableStartMonth.first;
        int startYear = availableStartMonth.second;
        int endYear = availableEndMonth.second;
        int endMonth = availableEndMonth.first;

        for (int year = startYear; year <= endYear; year++) {
            List<MonthDataItem> listMonthData = new LinkedList<>();
            for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++) {
                MonthDataItem.State state = MonthDataItem.State.NORMAL;
                if ((year <= startYear && month < startMonth) || (year == endYear && month > endMonth)) {
                    state = MonthDataItem.State.DISABLE;
                }
                listMonthData.add(new MonthDataItem(month, year, state));
            }
            mapMonthData.put(year, listMonthData);
        }
    }

    private void resetUIWithSelectedMonth() {
        if (mapMonthData == null || mapMonthData.isEmpty()) {
            return;
        }
        int startMonth = availableStartMonth.first;
        int startYear = availableStartMonth.second;
        int endMonth = availableEndMonth.first;
        int endYear = availableEndMonth.second;

        //Trường hợp chỉ chọn start month -> Disable hết các tháng trước start month
        //Trường hợp chọn cả start và end month -> enable lại các tháng trừ tháng trong quá khứ

        Set<Map.Entry<Integer, List<MonthDataItem>>> entries = mapMonthData.entrySet();
        for (Map.Entry<Integer, List<MonthDataItem>> entry : entries) {
            List<MonthDataItem> values = entry.getValue();
            for (MonthDataItem value : values) {
                int year = value.getYear();
                int month = value.getMonth();

                MonthDataItem.State state = MonthDataItem.State.NORMAL;
                //=> Fill Disable
                if (year < startYear || (year == startYear && month < startMonth)) {
                    state = MonthDataItem.State.DISABLE;
                }
                if (year > endYear || (year == endYear && month > endMonth)) {
                    state = MonthDataItem.State.DISABLE;
                }
                //=> Fill
                if (selectedStartMonth != null) {
                    if (selectedEndMonth != null) {
                        if (year >= selectedStartMonth.second && year <= selectedEndMonth.second) {
                            boolean isStartEndYearEqual = selectedStartMonth.second.compareTo(selectedEndMonth.second) == 0;
                            boolean isMonthInRange = month >= selectedStartMonth.first && month <= selectedEndMonth.first;
                            if (isStartEndYearEqual) {
                                if (isMonthInRange) {
                                    state = MonthDataItem.State.SELECTED;
                                }
                            } else {
                                if (year == selectedStartMonth.second) {
                                    if (month >= selectedStartMonth.first) {
                                        state = MonthDataItem.State.SELECTED;
                                    }
                                } else if (year == selectedEndMonth.second) {
                                    if (month <= selectedEndMonth.first) {
                                        state = MonthDataItem.State.SELECTED;
                                    }
                                } else {
                                    state = MonthDataItem.State.SELECTED;
                                }
                            }
                        }
                    } else {
                        if (year == selectedStartMonth.second) {
                            if (month == selectedStartMonth.first) {
                                state = MonthDataItem.State.SELECTED;
                            } else if (month < selectedStartMonth.first) {
                                state = MonthDataItem.State.DISABLE;
                            }
                        } else if (year < selectedStartMonth.second) {
                            state = MonthDataItem.State.DISABLE;
                        }
                    }
                }
                value.setState(state);
            }
        }
        if (this.monthAdapter != null) {
            this.monthAdapter.notifyDataSetChanged();
        }
        this.needResetUI = false;
    }

    private void selectMonthInYear(int selectYear) {
        if (selectYear == currentSelectYear) {
            return;
        }
        this.monthAdapter = new MonthAdapter(getContext(), mapMonthData.get(selectYear));
        monthAdapter.setOnItemSelectListener(new MonthAdapter.OnItemSelectListener() {
            @Override
            public void onSelect(int position, MonthDataItem monthDataItem) {
                if (listener != null) {
                    listener.onSelect(monthDataItem.getMonth(), monthDataItem.getYear());
                }
                if (selectedStartMonth != null && selectedEndMonth != null) {
                    selectedStartMonth = null;
                    selectedEndMonth = null;
                }

                if (selectedStartMonth == null) {
                    selectedStartMonth = new Pair<>(monthDataItem.getMonth(), monthDataItem.getYear());
                    updateUIWithSelectStart(selectedStartMonth);
                    selectedEndMonth = null;
                } else if (selectedEndMonth == null) {
                    selectedEndMonth = new Pair<>(monthDataItem.getMonth(), monthDataItem.getYear());
                    updateUIWithSelectStartEnd(selectedStartMonth, selectedEndMonth);
                }
                resetUIWithSelectedMonth();
            }
        });
        this.rvListItem.setAdapter(monthAdapter);

        this.currentSelectYear = selectYear;
        this.tvYear.setText(String.valueOf(this.currentSelectYear));

        if (currentSelectYear <= availableStartYear) {
            currentSelectYear = availableStartYear;
            this.btnCalendarBack.setImageResource(R.drawable.ic_arrow_left_fill_disable);
            this.btnCalendarBack.setClickable(false);
        } else {
            this.btnCalendarBack.setImageResource(R.drawable.ic_arrow_left_fill);
            this.btnCalendarBack.setClickable(true);
        }
        if (currentSelectYear >= availableEndYear) {
            currentSelectYear = availableEndYear;
            this.btnCalendarNext.setImageResource(R.drawable.ic_arrow_right_fill_disable);
            this.btnCalendarNext.setClickable(false);
        } else {
            this.btnCalendarNext.setImageResource(R.drawable.ic_arrow_right_fill);
            this.btnCalendarNext.setClickable(true);
        }
    }

    private void initAvailableMonthDefault() {
        Calendar calendar = Calendar.getInstance();
        this.currentMonth = calendar.get(Calendar.MONTH);
        this.currentYear = calendar.get(Calendar.YEAR);

        if (availableStartMonth == null || availableEndMonth == null) {
            this.availableStartMonth = new Pair<>(this.currentMonth, currentYear);
            calendar.add(Calendar.YEAR, 1);
            this.availableEndMonth = new Pair<>(Calendar.DECEMBER, calendar.get(Calendar.YEAR));
        }
        this.availableStartYear = availableStartMonth.second;
        this.availableEndYear = availableEndMonth.second;
    }

    public void updateAvailableRange(Pair<Integer, Integer> startMonth, Pair<Integer, Integer> endMonth) {
        if (startMonth == null || endMonth == null || startMonth.first == null || startMonth.second == null || endMonth.first == null || endMonth.second == null) {
            throw new NullPointerException("Invalid parameters");
        }
        if (startMonth.second.compareTo(endMonth.second) > 0 || (startMonth.second.compareTo(endMonth.second) == 0 && startMonth.first.compareTo(endMonth.first) > 0)) {
            throw new InvalidParameterException("Start month must be less than end month");
        }

        this.availableStartMonth = startMonth;
        this.availableEndMonth = endMonth;
        this.mapMonthData = new LinkedHashMap<>();
        this.selectedStartMonth = null;
        this.selectedEndMonth = null;
    }

    public void updateSelectedRange(Pair<Integer, Integer> startMonth, Pair<Integer, Integer> endMonth) {
        if (startMonth == null || endMonth == null || startMonth.first == null || startMonth.second == null || endMonth.first == null || endMonth.second == null) {
            throw new NullPointerException("Invalid parameters");
        }
        if (startMonth.second.compareTo(endMonth.second) > 0 || (startMonth.second.compareTo(endMonth.second) == 0 && startMonth.first.compareTo(endMonth.first) > 0)) {
            throw new InvalidParameterException("Start month must be less than end month");
        }

        this.selectedStartMonth = startMonth;
        this.selectedEndMonth = endMonth;
    }

    public void updateUIWithSelectStart(Pair<Integer, Integer> month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month.first);
        calendar.set(Calendar.YEAR, month.second);
        Date date = calendar.getTime();
        String title = String.format(TITLE_PLACEHOLDER_WITH_START, sdf.format(date));
        this.viewHeader.setTitle(title);
        this.setDisableBtnPositive();
    }

    private void setDisableBtnPositive(){
        this.btnPositive.disable();
        this.btnPositive.setTextColorButton(R.color.pri_2);
    }
    private void setEnableBtnPositive(){
        this.btnPositive.enable();
        this.btnPositive.setTextColorButton(R.color.pri_4);
    }

    public void updateUIWithSelectStartEnd(Pair<Integer, Integer> startMonth, Pair<Integer, Integer> endMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, startMonth.first);
        calendar.set(Calendar.YEAR, startMonth.second);
        Date startDate = calendar.getTime();

        calendar.set(Calendar.MONTH, endMonth.first);
        calendar.set(Calendar.YEAR, endMonth.second);
        Date endDate = calendar.getTime();
        String title = String.format(TITLE_PLACEHOLDER_WITH_START_END, sdf.format(startDate), sdf.format(endDate));
        viewHeader.setTitle(title);
        this.setEnableBtnPositive();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        this.currentSelectYear = 0;
        super.onDismiss(dialog);
    }

    public interface RangeSelectListener {
        void onSelect(int month, int year);

        void onSelectRange(Pair<Integer, Integer> start, Pair<Integer, Integer> end);

    }

    public static class Builder {
        private RangeSelectListener listener;
        private Pair<Integer, Integer> availableStartMonth, availableEndMonth;
        private Pair<Integer, Integer> selectedStartMonth, selectedEndMonth;
        private String dialogTitle, positiveButtonText, negativeButtonText;

        public Builder() {
        }

        public Builder setListener(RangeSelectListener listener) {
            this.listener = listener;
            return this;
        }

        public void setAvailableRange(Pair<Integer, Integer> startMonth, Pair<Integer, Integer> endMonth) {
            if (startMonth == null || endMonth == null || startMonth.first == null || startMonth.second == null || endMonth.first == null || endMonth.second == null) {
                throw new NullPointerException("Invalid parameters");
            }
            if (startMonth.second.compareTo(endMonth.second) > 0 || (startMonth.second.compareTo(endMonth.second) == 0 && startMonth.first.compareTo(endMonth.first) > 0)) {
                throw new InvalidParameterException("Start month must be less than end month");
            }

            this.availableStartMonth = startMonth;
            this.availableEndMonth = endMonth;
        }

        public void setSelectedRange(Pair<Integer, Integer> startMonth, Pair<Integer, Integer> endMonth) {
            if (startMonth == null || endMonth == null || startMonth.first == null || startMonth.second == null || endMonth.first == null || endMonth.second == null) {
                throw new NullPointerException("Invalid parameters");
            }
            if (startMonth.second.compareTo(endMonth.second) > 0 || (startMonth.second.compareTo(endMonth.second) == 0 && startMonth.first.compareTo(endMonth.first) > 0)) {
                throw new InvalidParameterException("Start month must be less than end month");
            }

            this.selectedStartMonth = startMonth;
            this.selectedEndMonth = endMonth;
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

        public DialogMonthPicker build() {
            return new DialogMonthPicker(this);
        }
    }
}
