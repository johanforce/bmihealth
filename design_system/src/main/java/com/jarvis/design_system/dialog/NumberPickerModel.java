package com.jarvis.design_system.dialog;

import android.util.Pair;

import java.util.List;

public class NumberPickerModel {
    private int unit;
    private String unitText, splitText, unitTitle;
    private int totalPicker;
    private List<Pair<Integer, Integer>> valueRange;
    private Pair<Integer, Integer> minValue, maxValue;

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getUnitText() {
        return unitText;
    }

    public void setUnitText(String unitText) {
        this.unitText = unitText;
    }

    public int getTotalPicker() {
        return totalPicker;
    }

    public void setTotalPicker(int totalPicker) {
        this.totalPicker = totalPicker;
    }

    public String getSplitText() {
        return splitText;
    }

    public void setSplitText(String splitText) {
        this.splitText = splitText;
    }

    public String getUnitTitle() {
        return unitTitle;
    }

    public void setUnitTitle(String unitTitle) {
        this.unitTitle = unitTitle;
    }

    public List<Pair<Integer, Integer>> getValueRange() {
        return valueRange;
    }

    public void setValueRange(List<Pair<Integer, Integer>> valueRange) {
        this.valueRange = valueRange;
    }

    public Pair<Integer, Integer> getMinValue() {
        return minValue;
    }

    public void setMinValue(Pair<Integer, Integer> minValue) {
        this.minValue = minValue;
    }

    public Pair<Integer, Integer> getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Pair<Integer, Integer> maxValue) {
        this.maxValue = maxValue;
    }

}
