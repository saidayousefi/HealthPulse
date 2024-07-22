package com.example.healthtracker.features.view_health_records;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.healthtracker.databinding.FragmentEditHealthRecordsBinding;
import com.example.healthtracker.data.local.model.RecordData;
import com.example.healthtracker.ui.viewmodel.HealthRecordsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditHealthRecordsFragment extends Fragment {
    private FragmentEditHealthRecordsBinding binding;
    private HealthRecordsViewModel recordViewModel;
    private RecordData existingRecord;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditHealthRecordsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordViewModel = new ViewModelProvider(this).get(HealthRecordsViewModel.class);

        if (getArguments() != null && getArguments().containsKey("recordId")) {
            int recordId = getArguments().getInt("recordId");
            recordViewModel.getRecordById(recordId).observe(getViewLifecycleOwner(), record -> {
                if (record != null) {
                    existingRecord = record;
                    prefillData(record);
                }
            });
        }

        binding.buttonSave.setOnClickListener(v -> saveOrUpdateRecord());
        binding.buttonDelete.setOnClickListener(v -> deleteRecord());
    }

    private void prefillData(RecordData record) {
        binding.editTextSystolic.setText(String.valueOf(record.getSystolic()));
        binding.editTextDiastolic.setText(String.valueOf(record.getDiastolic()));
        binding.editTextBloodSugar.setText(String.valueOf(record.getBloodSugar()));
    }

    private void saveOrUpdateRecord() {
        String systolicStr = binding.editTextSystolic.getText().toString();
        String diastolicStr = binding.editTextDiastolic.getText().toString();
        String bloodSugarStr = binding.editTextBloodSugar.getText().toString();

        if (isValid(systolicStr, diastolicStr, bloodSugarStr)) {
            int systolic = Integer.parseInt(systolicStr);
            int diastolic = Integer.parseInt(diastolicStr);
            int bloodSugar = Integer.parseInt(bloodSugarStr);

            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            if (existingRecord == null) {
                RecordData record = new RecordData(0, systolic, diastolic, bloodSugar, currentDate, currentTime);
                recordViewModel.insert(record);
                Toast.makeText(getContext(), "Record saved successfully!", Toast.LENGTH_SHORT).show();
            } else {
                existingRecord.setSystolic(systolic);
                existingRecord.setDiastolic(diastolic);
                existingRecord.setBloodSugar(bloodSugar);
                existingRecord.setDate(currentDate);
                existingRecord.setTime(currentTime);
                recordViewModel.update(existingRecord);
                Toast.makeText(getContext(), "Record updated successfully!", Toast.LENGTH_SHORT).show();
            }

            NavHostFragment.findNavController(EditHealthRecordsFragment.this).navigateUp();
        } else {
            Toast.makeText(getContext(), "Please enter valid health data.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRecord() {
        if (existingRecord != null) {
            recordViewModel.delete(existingRecord);
            Toast.makeText(getContext(), "Record deleted successfully!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(EditHealthRecordsFragment.this).navigateUp();
        }
    }

    private boolean isValid(String systolicStr, String diastolicStr, String bloodSugarStr) {
        if (systolicStr.isEmpty() || diastolicStr.isEmpty() || bloodSugarStr.isEmpty()) {
            return false;
        }

        try {
            int systolic = Integer.parseInt(systolicStr);
            int diastolic = Integer.parseInt(diastolicStr);
            int bloodSugar = Integer.parseInt(bloodSugarStr);

            if (systolic < 70 || systolic > 190) {
                binding.editTextSystolic.setError("Systolic BP must be between 70 and 190");
                return false;
            }
            if (diastolic < 40 || diastolic > 120) {
                binding.editTextDiastolic.setError("Diastolic BP must be between 40 and 120");
                return false;
            }
            if (bloodSugar < 70 || bloodSugar > 200) {
                binding.editTextBloodSugar.setError("Blood sugar must be between 70 and 200");
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}
