package com.jarvis.design_system.dialog.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.jarvis.design_system.R;
import com.jarvis.design_system.databinding.ItemMonthYearPickerBinding;
import com.jarvis.design_system.dialog.model.MonthDataItem;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MonthAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Calendar calendar;
    private SimpleDateFormat sdf;
    private List<MonthDataItem> listMonth;
    private Context context;
    private OnItemSelectListener listener;

    @SuppressLint("SimpleDateFormat")
    public MonthAdapter(Context context, List<MonthDataItem> listMonth) {
        this.context = context;
        this.listMonth = listMonth;
        this.calendar = Calendar.getInstance(Locale.getDefault());
        this.sdf = new SimpleDateFormat("MMM");
    }

    public void setOnItemSelectListener(OnItemSelectListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMonthYearPickerBinding binding = ItemMonthYearPickerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MonthViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MonthDataItem monthDataItem = listMonth.get(position);
        this.calendar.set(Calendar.YEAR, monthDataItem.getYear());
        this.calendar.set(Calendar.MONTH, monthDataItem.getMonth());
        this.calendar.set(Calendar.DATE, 1);
        String monthStr = this.sdf.format(calendar.getTime());
        MonthViewHolder viewHolder = (MonthViewHolder) holder;
        viewHolder.bindView(position, monthStr, monthDataItem);
    }

    @Override
    public int getItemCount() {
        return this.listMonth != null ? this.listMonth.size() : 0;
    }

    public interface OnItemSelectListener {
        void onSelect(int position, MonthDataItem monthDataItem);
    }

    class MonthViewHolder extends RecyclerView.ViewHolder {
        private ItemMonthYearPickerBinding binding;

        public MonthViewHolder(@NonNull ItemMonthYearPickerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bindView(int position, String monthStr, MonthDataItem monthDataItem) {
            this.binding.tvContent.setText(monthStr);
            switch (monthDataItem.getState()) {
                case NORMAL:
                    this.binding.viewRoot.setSelected(false);
                    this.binding.tvContent.setTextColor(ContextCompat.getColor(context, R.color.ink_5));
                    this.binding.viewRoot.setClickable(true);
                    break;
                case SELECTED:
                    this.binding.viewRoot.setSelected(true);
                    this.binding.tvContent.setTextColor(ContextCompat.getColor(context, R.color.white_5));
                    this.binding.viewRoot.setClickable(true);
                    break;
                case DISABLE:
                    this.binding.viewRoot.setSelected(false);
                    this.binding.tvContent.setTextColor(ContextCompat.getColor(context, R.color.ink_2));
                    this.binding.viewRoot.setClickable(false);
                    break;
            }

            this.binding.viewRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (monthDataItem.getState() == MonthDataItem.State.DISABLE) {
                        return;
                    }
                    if (monthDataItem.getState() == MonthDataItem.State.NORMAL) {
                        monthDataItem.setState(MonthDataItem.State.SELECTED);
                        notifyItemChanged(position);
                    }
                    if (listener != null) {
                        listener.onSelect(position, monthDataItem);
                    }
                }
            });
        }
    }
}
