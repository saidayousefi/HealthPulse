package com.example.healthpulse;

import android.content.Context;
import android.widget.TextView;

import com.example.healthpulse.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyMarkerView extends MarkerView {
    private TextView tvContent;
    private SimpleDateFormat dateFormat;

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = findViewById(R.id.tvContent);
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    }

    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof BarEntry) {
            BarEntry be = (BarEntry) e;
            if (be.getYVals() != null) {
                // This is for the Blood Pressure chart
                tvContent.setText(String.format(Locale.getDefault(),
                        "Date: %s\nSystolic: %.1f\nDiastolic: %.1f",
                        getFormattedDate(be.getX()),
                        be.getYVals()[0],
                        be.getYVals()[1]));
            } else {
                // This is for the Blood Sugar chart
                tvContent.setText(String.format(Locale.getDefault(),
                        "Date: %s\nBlood Sugar: %.1f",
                        getFormattedDate(be.getX()),
                        be.getY()));
            }
        } else {
            tvContent.setText(String.format(Locale.getDefault(),
                    "Date: %s\nValue: %.1f",
                    getFormattedDate(e.getX()),
                    e.getY()));
        }
        super.refreshContent(e, highlight);
    }

    private String getFormattedDate(float x) {
        // Assuming x is the number of days since epoch
        long milliseconds = (long) x * 24 * 60 * 60 * 1000;
        return dateFormat.format(new Date(milliseconds));
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}