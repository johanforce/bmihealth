package com.jarvis.bmihealth.presentation.utilx;

import android.content.Context;

import com.jarvis.bmihealth.R;
import com.well.unitlibrary.UnitConverter;

public class ListStringTimeDisplay {
    public String hoursValue;
    public String unitHours;
    public String minValue;
    public String minUnit;

    public ListStringTimeDisplay(int hoursValue, int minValue, Context context) {
        if (hoursValue <= 0) {
            this.hoursValue = "";
            this.unitHours = "";
            this.minUnit = context.getString(R.string.all_min);
        } else {
            this.hoursValue = String.valueOf(hoursValue);
            this.unitHours = "h";
            this.minUnit = "m";
        }
        this.minValue = UnitConverter.formatDoubleToString(minValue);
    }
}

