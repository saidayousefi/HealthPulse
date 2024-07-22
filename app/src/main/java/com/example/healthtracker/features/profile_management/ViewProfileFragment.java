package com.example.healthtracker.features.profile_management;

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
import com.example.healthtracker.R;
import com.example.healthtracker.data.local.model.Profile;
import com.example.healthtracker.databinding.FragmentViewProfileBinding;
import com.example.healthtracker.ui.viewmodel.ProfileViewModel;
import com.example.healthtracker.features.profile_management.SharedPreferencesManager;

public class ViewProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentViewProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentViewProfileBinding.inflate(inflater, container, false);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding.setProfileViewModel(profileViewModel); // Ensure the method name matches your binding class
        binding.setLifecycleOwner(getViewLifecycleOwner());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observing the LiveData
        profileViewModel.getCurrentProfile().observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                binding.profileName.setText(profile.getFirstName() + " " + profile.getLastName());
                binding.profileEmail.setText(profile.getEmail());
                // Set other profile fields here...
            }
        });

        // Handle edit profile button click
        binding.editProfileButton.setOnClickListener(v -> {
            Profile profile = profileViewModel.getCurrentProfile().getValue();
            if (profile != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("profile", profile);
                Navigation.findNavController(view).navigate(R.id.action_viewProfileFragment_to_editProfileFragment, bundle);
            }
        });

        // Handle logout button click
        binding.logoutButton.setOnClickListener(v -> {
            SharedPreferencesManager.getInstance(getContext()).setLoggedIn(false);
            Navigation.findNavController(view).navigate(R.id.action_viewProfileFragment_to_loginFragment);
            Toast.makeText(getContext(), "Logged out", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
