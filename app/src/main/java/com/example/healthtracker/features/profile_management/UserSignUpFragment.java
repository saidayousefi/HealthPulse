package com.example.healthtracker.features.profile_management;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.Profile;
import com.example.healthtracker.databinding.FragmentUserSignUpBinding;
import com.example.healthtracker.ui.viewmodel.ProfileViewModel;

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

        binding.saveButton.setOnClickListener(v -> {
            if (validateInputs()) {
                saveProfile();
            }
        });
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

        if (!binding.conditionsCheckBox.isChecked()) {
            Toast.makeText(getContext(), "You must accept the terms.", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        return valid;
    }

    private void saveProfile() {
        String firstName = binding.firstNameEditText.getText().toString();
        String lastName = binding.lastNameEditText.getText().toString();
        String email = binding.emailEditText.getText().toString();
        String password = binding.passwordEditText.getText().toString();
        String gender = binding.genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale ? "Male" : "Female";

        Profile profile = new Profile(firstName, lastName, email, password, gender, true);
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
