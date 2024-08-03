package com.example.healthpulse.features.add_health_record;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthpulse.R;
import com.example.healthpulse.databinding.FragmentAddHealthRecordBinding;
import com.example.healthpulse.data.local.model.RecordData;
import com.example.healthpulse.ui.viewmodel.HealthRecordsViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddHealthRecordFragment extends Fragment {
    private FragmentAddHealthRecordBinding binding;
    private HealthRecordsViewModel recordViewModel;
    private final Calendar selectedDateTime = Calendar.getInstance();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddHealthRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordViewModel = new ViewModelProvider(this).get(HealthRecordsViewModel.class);

        binding.buttonSave.setOnClickListener(v -> saveRecord());
        binding.editTextDate.setOnClickListener(v -> showDatePicker());
        binding.editTextTime.setOnClickListener(v -> showTimePicker());
    }

    private void showDatePicker() {
        if (getContext() != null) {
            // Create a ContextThemeWrapper with your custom theme
            Context themedContext = new ContextThemeWrapper(getContext(), R.style.CustomDatePicker);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    themedContext,
                    (view, year, month, dayOfMonth) -> {
                        selectedDateTime.set(Calendar.YEAR, year);
                        selectedDateTime.set(Calendar.MONTH, month);
                        selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
                        binding.editTextDate.setText(dateFormat.format(selectedDateTime.getTime()));
                    },
                    selectedDateTime.get(Calendar.YEAR),
                    selectedDateTime.get(Calendar.MONTH),
                    selectedDateTime.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        }
    }

    private void showTimePicker() {
        if (getContext() != null) {
            // Create a ContextThemeWrapper with your custom theme
            Context themedContext = new ContextThemeWrapper(getContext(), R.style.CustomTimePicker);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    themedContext,
                    (view, hourOfDay, minute) -> {
                        selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedDateTime.set(Calendar.MINUTE, minute);
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
                        binding.editTextTime.setText(timeFormat.format(selectedDateTime.getTime()));
                    },
                    selectedDateTime.get(Calendar.HOUR_OF_DAY),
                    selectedDateTime.get(Calendar.MINUTE),
                    true
            );
            timePickerDialog.show();
        }
    }

    private void saveRecord() {
        String systolicStr = binding.editTextSystolic.getText() != null ? binding.editTextSystolic.getText().toString() : "";
        String diastolicStr = binding.editTextDiastolic.getText() != null ? binding.editTextDiastolic.getText().toString() : "";
        String bloodSugarStr = binding.editTextBloodSugar.getText() != null ? binding.editTextBloodSugar.getText().toString() : "";
        String dateStr = binding.editTextDate.getText() != null ? binding.editTextDate.getText().toString() : "";
        String timeStr = binding.editTextTime.getText() != null ? binding.editTextTime.getText().toString() : "";

        if (isValid(systolicStr, diastolicStr, bloodSugarStr, dateStr, timeStr)) {
            int systolic = Integer.parseInt(systolicStr);
            int diastolic = Integer.parseInt(diastolicStr);
            int bloodSugar = Integer.parseInt(bloodSugarStr);

            RecordData record = new RecordData(0, systolic, diastolic, bloodSugar, dateStr, timeStr);
            recordViewModel.insert(record);
            Toast.makeText(getContext(), "Record saved successfully!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(AddHealthRecordFragment.this).navigateUp();
        } else {
            Toast.makeText(getContext(), "Please enter valid health data.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValid(String systolicStr, String diastolicStr, String bloodSugarStr, String dateStr, String timeStr) {
        if (TextUtils.isEmpty(systolicStr) || TextUtils.isEmpty(diastolicStr) || TextUtils.isEmpty(bloodSugarStr)
                || TextUtils.isEmpty(dateStr) || TextUtils.isEmpty(timeStr)) {
            return false;
        }

        try {
            int systolic = Integer.parseInt(systolicStr);
            int diastolic = Integer.parseInt(diastolicStr);
            int bloodSugar = Integer.parseInt(bloodSugarStr);

            if (systolic < 70 || systolic > 210) {
                binding.editTextSystolic.setError("Systolic BP must be between 70 and 210");
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
