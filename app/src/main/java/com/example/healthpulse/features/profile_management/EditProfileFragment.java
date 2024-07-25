package com.example.healthpulse.features.profile_management;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

        // Setup gender dropdown
        setupGenderDropdown();

        // Setup date of birth dropdowns
        setupDateOfBirthDropdowns();

        // Retrieve the profile from the arguments
        if (getArguments() != null) {
            currentProfile = (Profile) getArguments().getSerializable("profile");
            if (currentProfile != null) {
                populateProfileData(currentProfile);
            }
        }

        return binding.getRoot();
    }

    private void setupGenderDropdown() {
        String[] genders = getResources().getStringArray(R.array.gender_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, genders);
        binding.genderAutoCompleteTextView.setAdapter(adapter);
    }

    private void setupDateOfBirthDropdowns() {
        // Year dropdown
        List<String> years = new ArrayList<>();
        for (int year = 1950; year <= 2022; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, years);
        binding.yearAutoCompleteTextView.setAdapter(yearAdapter);

        // Month dropdown
        List<String> months = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            months.add(String.valueOf(month));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, months);
        binding.monthAutoCompleteTextView.setAdapter(monthAdapter);

        // Day dropdown
        List<String> days = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            days.add(String.valueOf(day));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, days);
        binding.dayAutoCompleteTextView.setAdapter(dayAdapter);
    }

    private void populateProfileData(Profile profile) {
        binding.firstNameEditText.setText(profile.getFirstName());
        binding.lastNameEditText.setText(profile.getLastName());
        binding.emailEditText.setText(profile.getEmail());
        binding.genderAutoCompleteTextView.setText(profile.getGender(), false);

        // Set date of birth
        String[] dateParts = profile.getDateOfBirth().split("-");
        if (dateParts.length == 3) {
            binding.yearAutoCompleteTextView.setText(dateParts[0], false);
            binding.monthAutoCompleteTextView.setText(dateParts[1], false);
            binding.dayAutoCompleteTextView.setText(dateParts[2], false);
        }

        binding.medicalHistoryEditText.setText(profile.getMedicalHistory());
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

        if (TextUtils.isEmpty(binding.genderAutoCompleteTextView.getText())) {
            binding.genderAutoCompleteTextView.setError("Please select a gender");
            valid = false;
        }

        if (TextUtils.isEmpty(binding.yearAutoCompleteTextView.getText()) ||
                TextUtils.isEmpty(binding.monthAutoCompleteTextView.getText()) ||
                TextUtils.isEmpty(binding.dayAutoCompleteTextView.getText())) {
            Toast.makeText(getContext(), "Please select a complete date of birth", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void updateProfile() {
        currentProfile.setFirstName(binding.firstNameEditText.getText().toString());
        currentProfile.setLastName(binding.lastNameEditText.getText().toString());
        currentProfile.setEmail(binding.emailEditText.getText().toString());
        currentProfile.setGender(binding.genderAutoCompleteTextView.getText().toString());

        String dateOfBirth = binding.yearAutoCompleteTextView.getText() + "-" +
                binding.monthAutoCompleteTextView.getText() + "-" +
                binding.dayAutoCompleteTextView.getText();
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