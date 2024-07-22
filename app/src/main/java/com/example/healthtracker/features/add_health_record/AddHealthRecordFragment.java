package com.example.healthtracker.features.add_health_record;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.healthtracker.databinding.FragmentAddHealthRecordBinding;
import com.example.healthtracker.data.local.model.RecordData;
import com.example.healthtracker.ui.viewmodel.HealthRecordsViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddHealthRecordFragment extends Fragment {
    private FragmentAddHealthRecordBinding binding;
    private HealthRecordsViewModel recordViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddHealthRecordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recordViewModel = new ViewModelProvider(this).get(HealthRecordsViewModel.class);

        binding.buttonSave.setOnClickListener(v -> saveRecord());
    }

    private void saveRecord() {
        String systolicStr = binding.editTextSystolic.getText().toString();
        String diastolicStr = binding.editTextDiastolic.getText().toString();
        String bloodSugarStr = binding.editTextBloodSugar.getText().toString();

        if (isValid(systolicStr, diastolicStr, bloodSugarStr)) {
            int systolic = Integer.parseInt(systolicStr);
            int diastolic = Integer.parseInt(diastolicStr);
            int bloodSugar = Integer.parseInt(bloodSugarStr);

            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

            RecordData record = new RecordData(0, systolic, diastolic, bloodSugar, currentDate, currentTime);
            recordViewModel.insert(record);
            Toast.makeText(getContext(), "Record saved successfully!", Toast.LENGTH_SHORT).show();
            NavHostFragment.findNavController(AddHealthRecordFragment.this).navigateUp();
        } else {
            Toast.makeText(getContext(), "Please enter valid health data.", Toast.LENGTH_SHORT).show();
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
