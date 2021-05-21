package com.anydone.desk.utils;

import android.annotation.SuppressLint;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class CustomValueFormatter extends ValueFormatter {
    private DecimalFormat mFormat;

    public CustomValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    public CustomValueFormatter(DecimalFormat format) {
        this.mFormat = format;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String getFormattedValue(float value) {
        if (value > 0)
            return String.format("%.0f", value);
        else return "";
    }
}