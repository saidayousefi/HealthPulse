package com.example.healthpulse.features.profile_management;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthpulse.R;
import com.example.healthpulse.data.local.model.Profile;
import com.example.healthpulse.databinding.FragmentUserSignUpBinding;
import com.example.healthpulse.ui.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class UserSignUpFragment extends Fragment {

    private FragmentUserSignUpBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserSignUpBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        // Setup gender dropdown
        setupGenderDropdown();

        // Setup date of birth dropdowns
        setupDateOfBirthDropdowns();

        binding.saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveProfile();
            }
        });
    }

    private void setupGenderDropdown() {
        String[] genders = getResources().getStringArray(R.array.gender_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, genders);
        binding.genderAutoCompleteTextView.setAdapter(adapter);
    }

    private void setupDateOfBirthDropdowns() {
        // Year dropdown
        List<String> years = new ArrayList<>();
        for (int year = 1950; year <= 2022; year++) {
            years.add(String.valueOf(year));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, years);
        binding.yearAutoCompleteTextView.setAdapter(yearAdapter);

        // Month dropdown
        List<String> months = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            months.add(String.format("%02d", month));
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, months);
        binding.monthAutoCompleteTextView.setAdapter(monthAdapter);

        // Day dropdown
        List<String> days = new ArrayList<>();
        for (int day = 1; day <= 31; day++) {
            days.add(String.format("%02d", day));
        }
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_item, days);
        binding.dayAutoCompleteTextView.setAdapter(dayAdapter);
    }

    private boolean validateInputs() {
        boolean valid = true;

        if (TextUtils.isEmpty(binding.firstNameEditText.getText())) {
            binding.firstNameInputLayout.setError("First name is required.");
            valid = false;
        } else {
            binding.firstNameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(binding.lastNameEditText.getText())) {
            binding.lastNameInputLayout.setError("Last name is required.");
            valid = false;
        } else {
            binding.lastNameInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(binding.emailEditText.getText())) {
            binding.emailInputLayout.setError("Email is required.");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.emailEditText.getText()).matches()) {
            binding.emailInputLayout.setError("Enter a valid Email.");
            valid = false;
        } else {
            binding.emailInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(binding.passwordEditText.getText())) {
            binding.passwordInputLayout.setError("Password is required.");
            valid = false;
        } else {
            binding.passwordInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(binding.genderAutoCompleteTextView.getText())) {
            binding.genderInputLayout.setError("Please select a gender");
            valid = false;
        } else {
            binding.genderInputLayout.setError(null);
        }

        if (TextUtils.isEmpty(binding.yearAutoCompleteTextView.getText()) ||
                TextUtils.isEmpty(binding.monthAutoCompleteTextView.getText()) ||
                TextUtils.isEmpty(binding.dayAutoCompleteTextView.getText())) {
            Toast.makeText(getContext(), "Date of Birth is required.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (TextUtils.isEmpty(binding.medicalHistoryEditText.getText())) {
            binding.medicalHistoryInputLayout.setError("Medical History is required.");
            valid = false;
        } else {
            binding.medicalHistoryInputLayout.setError(null);
        }

        return valid;
    }

    private void saveProfile() {
        String firstName = binding.firstNameEditText.getText().toString();
        String lastName = binding.lastNameEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        String gender = binding.genderAutoCompleteTextView.getText().toString();
        String dateOfBirth = binding.yearAutoCompleteTextView.getText() + "-" +
                binding.monthAutoCompleteTextView.getText() + "-" +
                binding.dayAutoCompleteTextView.getText();
        String medicalHistory = binding.medicalHistoryEditText.getText().toString();

        Profile profile = new Profile(firstName, lastName, email, password, gender, dateOfBirth, medicalHistory);
        profileViewModel.insert(profile);

        Toast.makeText(getContext(), "Profile created", Toast.LENGTH_SHORT).show();

        NavHostFragment.findNavController(UserSignUpFragment.this).navigate(R.id.action_userSignUpFragment_to_loginFragment);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}