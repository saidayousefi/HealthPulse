package com.example.healthpulse.features.profile_management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.healthpulse.R;
import com.example.healthpulse.data.local.model.Profile;
import com.example.healthpulse.databinding.FragmentViewProfileBinding;
import com.example.healthpulse.ui.viewmodel.ProfileViewModel;

public class ViewProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentViewProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewProfileBinding.inflate(inflater, container, false);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setProfileViewModel(profileViewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profileViewModel.getCurrentProfile().observe(getViewLifecycleOwner(), this::updateUI);

        binding.editProfileButton.setOnClickListener(v -> navigateToEditProfile());
        binding.logoutButton.setOnClickListener(v -> logout());
    }

    private void updateUI(Profile profile) {
        if (profile != null) {
            binding.profileName.setText(getString(R.string.full_name, profile.getFirstName(), profile.getLastName()));
            binding.profileEmail.setText(profile.getEmail());
            binding.profileGender.setText(getString(R.string.gender_label, profile.getGender()));
            binding.profileDateOfBirth.setText(getString(R.string.dob_label, profile.getDateOfBirth()));
            binding.profileMedicalHistory.setText(profile.getMedicalHistory());
        }
    }

    private void navigateToEditProfile() {
        Profile profile = profileViewModel.getCurrentProfile().getValue();
        if (profile != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("profile", profile);
            Navigation.findNavController(requireView()).navigate(R.id.action_viewProfileFragment_to_editProfileFragment, bundle);
        }
    }

    private void logout() {
        SharedPreferencesManager.getInstance(requireContext()).setLoggedIn(false);
        Navigation.findNavController(requireView()).navigate(R.id.action_viewProfileFragment_to_loginFragment);
        Toast.makeText(requireContext(), R.string.logged_out, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
