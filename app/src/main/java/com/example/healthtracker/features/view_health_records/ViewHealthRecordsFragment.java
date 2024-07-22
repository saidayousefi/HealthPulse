package com.example.healthtracker.features.view_health_records;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.healthtracker.ui.Navigator;
import com.example.healthtracker.R;
import com.example.healthtracker.ui.adapter.ViewHealthRecordsAdapter;
import com.example.healthtracker.MyDividerItemDecoration;
import com.example.healthtracker.databinding.FragmentViewHealthRecordsBinding;
import com.example.healthtracker.data.local.model.RecordData;
import com.example.healthtracker.ui.viewmodel.HealthRecordsViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewHealthRecordsFragment extends Fragment implements Navigator {

    private HealthRecordsViewModel healthRecordsViewModel;
    private FragmentViewHealthRecordsBinding binding;
    private ViewHealthRecordsAdapter adapter;
    private BarChart barChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_health_records, container, false);
        healthRecordsViewModel = new ViewModelProvider(this).get(HealthRecordsViewModel.class);
        healthRecordsViewModel.setNavigator(this);
        binding.setViewModel(healthRecordsViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Initialize the chart
        barChart = binding.barChart;

        // Initializing the adapter
        adapter = new ViewHealthRecordsAdapter(new ViewHealthRecordsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecordData recordData) {
                // Navigate to EditRecordFragment
                Bundle bundle = new Bundle();
                bundle.putInt("recordId", recordData.getId());
                Navigation.findNavController(getView()).navigate(R.id.action_viewHealthRecordsFragment_to_editHealthRecordsFragment, bundle);
            }

            @Override
            public void onDeleteClick(RecordData recordData) {
                healthRecordsViewModel.delete(recordData);
                Toast.makeText(getContext(), "Record deleted", Toast.LENGTH_SHORT).show();
            }
        });

        binding.recyclerViewRecords.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewRecords.setAdapter(adapter);

        // Item decoration for dividers
        MyDividerItemDecoration dividerItemDecoration = new MyDividerItemDecoration(getContext());
        binding.recyclerViewRecords.addItemDecoration(dividerItemDecoration);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        healthRecordsViewModel.getAllRecords().observe(getViewLifecycleOwner(), records -> {
            if (records.isEmpty()) {
                binding.recyclerViewRecords.setVisibility(View.GONE);
                binding.emptyStateView.setVisibility(View.VISIBLE);
            } else {
                binding.recyclerViewRecords.setVisibility(View.VISIBLE);
                binding.emptyStateView.setVisibility(View.GONE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(records, Comparator.comparing(RecordData::getDate).thenComparing(RecordData::getTime).reversed());
            }
            adapter.submitList(records);

            // Update the chart with data
            loadBarChartData(records);
        });
    }

    private void loadBarChartData(List<RecordData> recordDataList) {
        List<BarEntry> systolicEntries = new ArrayList<>();
        List<BarEntry> diastolicEntries = new ArrayList<>();
        List<BarEntry> bloodSugarEntries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();

        for (int i = 0; i < recordDataList.size(); i++) {
            RecordData record = recordDataList.get(i);
            systolicEntries.add(new BarEntry(i, record.getSystolic()));
            diastolicEntries.add(new BarEntry(i, record.getDiastolic()));
            bloodSugarEntries.add(new BarEntry(i, record.getBloodSugar()));
            xAxisLabels.add(record.getDate()); // Format date as needed
        }

        BarDataSet systolicDataSet = new BarDataSet(systolicEntries, "Systolic");
        systolicDataSet.setColor(getResources().getColor(R.color.systolic_color)); // Customize as needed

        BarDataSet diastolicDataSet = new BarDataSet(diastolicEntries, "Diastolic");
        diastolicDataSet.setColor(getResources().getColor(R.color.diastolic_color)); // Customize as needed

        BarDataSet bloodSugarDataSet = new BarDataSet(bloodSugarEntries, "Blood Sugar");
        bloodSugarDataSet.setColor(getResources().getColor(R.color.blood_sugar_color)); // Customize as needed

        BarData barData = new BarData(systolicDataSet, diastolicDataSet, bloodSugarDataSet);
        barData.setBarWidth(0.3f); // Set the width of the bars

        barChart.setData(barData);

        // Customize the x-axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setDrawGridLines(false);
        xAxis.setLabelRotationAngle(-45); // Rotate labels to avoid overlap

        // Customize the y-axis
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);

        // Customize the legend
        barChart.getLegend().setEnabled(true);
        barChart.getLegend().setWordWrapEnabled(true);

        barChart.setFitBars(true); // Make the x-axis fit exactly all bars
        barChart.invalidate(); // Refresh the chart
    }


    @Override
    public void onBackClicked() {
        Navigation.findNavController(requireView()).navigateUp();
    }
}
