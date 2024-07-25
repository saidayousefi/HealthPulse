package com.example.healthpulse.features.profile_management;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.example.healthpulse.R;
import com.example.healthpulse.data.local.model.Profile;
import com.example.healthpulse.databinding.FragmentEditProfileBinding;
import com.example.healthpulse.ui.viewmodel.ProfileViewModel;
import java.util.ArrayList;
import java.util.List;

public class EditProfileFragment extends Fragment {

    private FragmentEditProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private Profile currentProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setLifecycleOwner(getViewLifecycleOwner());

        // Setup gender spinner
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(requireContext(), R.array.gender_array, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.genderSpinner.setAdapter(genderAdapter);

        // Setup date of birth spinners
        setupDateOfBirthSpinners();

        // Retrieve the profile from the arguments
        if (getArguments() != null) {
            currentProfile = (Profile) getArguments().getSerializable("profile");
            if (currentProfile != null) {
                populateProfileData(currentProfile);
            }
        }

        return binding.getRoot();
    }

    private void setupDateOfBirthSpinners() {
        // Year spinner
        List<String> years = new ArrayList<>();
        for (int year = 1950; year <= 2022; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.yearSpinner.setAdapter(yearAdapter);

        // Month spinner
        List<String> months = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            months.add(String.valueOf(month));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.monthSpinner.setAdapter(monthAdapter);

        // Day spinner
        List<String> days = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            days.add(String.valueOf(day));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, days);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.daySpinner.setAdapter(dayAdapter);
    }

    private void populateProfileData(Profile profile) {
        binding.firstNameEditText.setText(profile.getFirstName());
        binding.lastNameEditText.setText(profile.getLastName());
        binding.emailEditText.setText(profile.getEmail());

        // Set gender spinner selection
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) binding.genderSpinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(profile.getGender());
            if (position != -1) {
                binding.genderSpinner.setSelection(position);
            }
        }

        // Set date of birth spinners
        String[] dateParts = profile.getDateOfBirth().split("-");
        if (dateParts.length == 3) {
            setSpinnerToValue(binding.yearSpinner, dateParts[0]);
            setSpinnerToValue(binding.monthSpinner, dateParts[1]);
            setSpinnerToValue(binding.daySpinner, dateParts[2]);
        }

        binding.medicalHistoryEditText.setText(profile.getMedicalHistory());
    }

    private void setSpinnerToValue(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(value);
            if (position != -1) {
                spinner.setSelection(position);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                updateProfile();
            }
        });
    }

    private boolean validateInputs() {
        boolean valid = true;

        if (TextUtils.isEmpty(binding.firstNameEditText.getText())) {
            binding.firstNameEditText.setError("First name is required.");
            valid = false;
        }

        if (TextUtils.isEmpty(binding.lastNameEditText.getText())) {
            binding.lastNameEditText.setError("Last name is required.");
            valid = false;
        }

        if (TextUtils.isEmpty(binding.emailEditText.getText())) {
            binding.emailEditText.setError("Email is required.");
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.getText()).matches()) {
            binding.emailEditText.setError("Enter a valid Email.");
            valid = false;
        }

        if (binding.genderSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (binding.yearSpinner.getSelectedItemPosition() == 0 ||
                binding.monthSpinner.getSelectedItemPosition() == 0 ||
                binding.daySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(getContext(), "Please select a complete date of birth", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void updateProfile() {
        currentProfile.setFirstName(binding.firstNameEditText.getText().toString());
        currentProfile.setLastName(binding.lastNameEditText.getText().toString());
        currentProfile.setEmail(binding.emailEditText.getText().toString());
        currentProfile.setGender(binding.genderSpinner.getSelectedItem().toString());

        String dateOfBirth = binding.yearSpinner.getSelectedItem().toString() + "-" +
                binding.monthSpinner.getSelectedItem().toString() + "-" +
                binding.daySpinner.getSelectedItem().toString();
        currentProfile.setDateOfBirth(dateOfBirth);

        currentProfile.setMedicalHistory(binding.medicalHistoryEditText.getText().toString());

        profileViewModel.update(currentProfile);

        Toast.makeText(getContext(), "Profile updated", Toast.LENGTH_SHORT).show();
        NavHostFragment.findNavController(EditProfileFragment.this).navigateUp();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
