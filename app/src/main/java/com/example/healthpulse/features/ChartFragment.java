package com.example.healthpulse.features;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.healthpulse.R;
import com.example.healthpulse.data.local.model.RecordData;
import com.example.healthpulse.databinding.FragmentChartBinding;
import com.example.healthpulse.ui.viewmodel.HealthRecordsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartFragment extends Fragment {

    private static final int SYSTOLIC_COLOR = Color.rgb(255, 87, 34);  // Deep Orange
    private static final int DIASTOLIC_COLOR = Color.rgb(33, 150, 243);  // Blue
    private static final int BLOOD_SUGAR_COLOR = Color.rgb(76, 175, 80);  // Green
    private static final int GRID_COLOR = Color.rgb(200, 200, 200);  // Light Gray
    private static final int TEXT_COLOR = Color.rgb(33, 33, 33);  // Dark Gray

    private HealthRecordsViewModel healthRecordsViewModel;
    private FragmentChartBinding binding;
    private BarChart barChartBloodPressure;
    private BarChart barChartBloodSugar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        healthRecordsViewModel = new ViewModelProvider(this).get(HealthRecordsViewModel.class);

        barChartBloodPressure = binding.barChartBloodPressure;
        barChartBloodSugar = binding.barChartBloodSugar;
        setupChart(barChartBloodPressure);
        setupChart(barChartBloodSugar);

        healthRecordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(records, Comparator.comparing(RecordData::getDate).thenComparing(RecordData::getTime).reversed());
            }

            loadBloodPressureChartData(records);
            loadBloodSugarChartData(records);
        });
    }

    private void setupChart(BarChart barChart) {
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        barChart.setDrawValueAboveBar(true);
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(1500);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);
        xAxis.setTextColor(TEXT_COLOR);
        xAxis.setTextSize(10f);
        xAxis.setLabelRotationAngle(0); // Set rotation angle to 0 for better readability

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(GRID_COLOR);
        leftAxis.setTextColor(TEXT_COLOR);
        leftAxis.setTextSize(12f);
        leftAxis.setAxisMinimum(0f);

        barChart.getAxisRight().setEnabled(false);

        Legend legend = barChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(true);
        legend.setTextSize(12f);
        legend.setTextColor(TEXT_COLOR);
    }

    private void loadBloodPressureChartData(List<RecordData> recordDataList) {
        List<BarEntry> systolicEntries = new ArrayList<>();
        List<BarEntry> diastolicEntries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();

        for (int i = 0; i < recordDataList.size(); i++) {
            RecordData record = recordDataList.get(i);
            systolicEntries.add(new BarEntry(i, record.getSystolic()));
            diastolicEntries.add(new BarEntry(i, record.getDiastolic()));
            xAxisLabels.add(formatDate(record.getDate()));
        }

        BarDataSet systolicDataSet = new BarDataSet(systolicEntries, "Systolic");
        systolicDataSet.setColor(SYSTOLIC_COLOR);
        systolicDataSet.setValueTextColor(TEXT_COLOR);
        systolicDataSet.setValueTextSize(10f);

        BarDataSet diastolicDataSet = new BarDataSet(diastolicEntries, "Diastolic");
        diastolicDataSet.setColor(DIASTOLIC_COLOR);
        diastolicDataSet.setValueTextColor(TEXT_COLOR);
        diastolicDataSet.setValueTextSize(10f);

        BarData barData = new BarData(systolicDataSet, diastolicDataSet);
        barData.setBarWidth(0.35f);

        barChartBloodPressure.setData(barData);
        barChartBloodPressure.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        barChartBloodPressure.groupBars(-0.5f, 0.06f, 0.02f);
        barChartBloodPressure.setFitBars(true);
        barChartBloodPressure.invalidate();

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(barChartBloodPressure);
        barChartBloodPressure.setMarker(mv);
    }

    private void loadBloodSugarChartData(List<RecordData> recordDataList) {
        List<BarEntry> bloodSugarEntries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();

        for (int i = 0; i < recordDataList.size(); i++) {
            RecordData record = recordDataList.get(i);
            bloodSugarEntries.add(new BarEntry(i, record.getBloodSugar()));
            xAxisLabels.add(formatDate(record.getDate()));
        }

        BarDataSet bloodSugarDataSet = new BarDataSet(bloodSugarEntries, "Blood Sugar");
        bloodSugarDataSet.setColor(BLOOD_SUGAR_COLOR);
        bloodSugarDataSet.setValueTextColor(TEXT_COLOR);
        bloodSugarDataSet.setValueTextSize(10f);

        BarData barData = new BarData(bloodSugarDataSet);
        barData.setBarWidth(0.6f);

        barChartBloodSugar.setData(barData);
        barChartBloodSugar.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        barChartBloodSugar.getXAxis().setLabelRotationAngle(0); // Set rotation angle to 0 for better readability
        barChartBloodSugar.setFitBars(true);
        barChartBloodSugar.invalidate();

        MyMarkerView mv = new MyMarkerView(getContext(), R.layout.custom_marker_view);
        mv.setChartView(barChartBloodSugar);
        barChartBloodSugar.setMarker(mv);
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("MM/dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public class MyMarkerView extends MarkerView {

        private TextView tvContent;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            tvContent = findViewById(R.id.tvContent);
        }

        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            if (e instanceof BarEntry) {
                BarEntry be = (BarEntry) e;
                if (be.getYVals() != null) {
                    tvContent.setText(String.format("Systolic: %.1f, Diastolic: %.1f", be.getYVals()[0], be.getYVals()[1]));
                } else {
                    tvContent.setText(String.format("Value: %.1f", be.getY()));
                }
            } else {
                tvContent.setText(String.format("Value: %.1f", e.getY()));
            }
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            return new MPPointF(-(getWidth() / 2), -getHeight());
        }
    }
}
