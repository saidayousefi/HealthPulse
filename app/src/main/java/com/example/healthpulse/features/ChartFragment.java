package com.example.healthpulse.features;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.healthpulse.R;
import com.example.healthpulse.data.local.model.RecordData;
import com.example.healthpulse.databinding.FragmentChartBinding;
import com.example.healthpulse.ui.viewmodel.HealthRecordsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChartFragment extends Fragment {

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

        // Initialize the charts
        barChartBloodPressure = binding.barChartBloodPressure;
        barChartBloodSugar = binding.barChartBloodSugar;
        setupChart(barChartBloodPressure);
        setupChart(barChartBloodSugar);

        healthRecordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(records, Comparator.comparing(RecordData::getDate).thenComparing(RecordData::getTime).reversed());
            }

            // Update the charts with data
            loadBloodPressureChartData(records);
            loadBloodSugarChartData(records);
        });
    }

    private void setupChart(BarChart barChart) {
        // Customize chart appearance
        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(1000); // Animation for chart loading

        // Customize X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);

        // Customize Y-Axis
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawGridLines(true);
        leftAxis.setGridColor(Color.LTGRAY);
        leftAxis.setTextColor(Color.BLACK);
        leftAxis.setTextSize(12f);
        barChart.getAxisRight().setEnabled(false);

        // Customize Legend
        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(14f);
        legend.setTextColor(Color.BLACK);
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
        systolicDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.systolic_color));
        systolicDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.systolic_color));

        BarDataSet diastolicDataSet = new BarDataSet(diastolicEntries, "Diastolic");
        diastolicDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.diastolic_color));
        diastolicDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.diastolic_color));

        BarData barData = new BarData(systolicDataSet, diastolicDataSet);
        barData.setValueTextSize(12f);
        barData.setBarWidth(0.45f); // Set bar width

        barChartBloodPressure.setData(barData);

        // Customize the x-axis
        XAxis xAxis = barChartBloodPressure.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setLabelRotationAngle(-45); // Rotate labels to avoid overlap

        barChartBloodPressure.groupBars(0f, 0.04f, 0.02f); // Group the bars together
        barChartBloodPressure.invalidate(); // Refresh the chart
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
        bloodSugarDataSet.setColor(ContextCompat.getColor(requireContext(), R.color.blood_sugar_color));
        bloodSugarDataSet.setValueTextColor(ContextCompat.getColor(requireContext(), R.color.blood_sugar_color));

        BarData barData = new BarData(bloodSugarDataSet);
        barData.setValueTextSize(12f);
        barData.setBarWidth(0.9f); // Set bar width

        barChartBloodSugar.setData(barData);

        // Customize the x-axis
        XAxis xAxis = barChartBloodSugar.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setLabelRotationAngle(-45); // Rotate labels to avoid overlap

        barChartBloodSugar.invalidate(); // Refresh the chart
    }

    private String formatDate(String dateString) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Return original string if parsing fails
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
