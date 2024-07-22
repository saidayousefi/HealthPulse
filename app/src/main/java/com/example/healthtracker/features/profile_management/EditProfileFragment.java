package com.example.healthtracker.features.profile_management;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.example.healthtracker.databinding.FragmentEditProfileBinding;
import com.example.healthtracker.ui.viewmodel.ProfileViewModel;

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

        // Retrieve the profile from the arguments
        if (getArguments() != null) {
            currentProfile = (Profile) getArguments().getSerializable("profile");
            if (currentProfile != null) {
                populateProfileData(currentProfile);
            }
        }

        return binding.getRoot();
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

    private void populateProfileData(Profile profile) {
        binding.firstNameEditText.setText(profile.getFirstName());
        binding.lastNameEditText.setText(profile.getLastName());
        binding.emailEditText.setText(profile.getEmail());
        // Assuming gender is a radio button group
        if ("Male".equals(profile.getGender())) {
            binding.radioButtonMale.setChecked(true);
        } else {
            binding.radioButtonFemale.setChecked(true);
        }
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

        return valid;
    }

    private void updateProfile() {
        currentProfile.setFirstName(binding.firstNameEditText.getText().toString());
        currentProfile.setLastName(binding.lastNameEditText.getText().toString());
        currentProfile.setEmail(binding.emailEditText.getText().toString());
        currentProfile.setGender(binding.genderRadioGroup.getCheckedRadioButtonId() == R.id.radioButtonMale ? "Male" : "Female");

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
