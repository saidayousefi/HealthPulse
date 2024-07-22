package com.example.healthtracker.features.profile_management;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.example.healthtracker.R;
import com.example.healthtracker.features.profile_management.SharedPreferencesManager;

public class SplashFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Delay for splash screen (e.g., 2 seconds)
        new Handler(Looper.getMainLooper()).postDelayed(this::checkLoginStatus, 2000);
    }

    private void checkLoginStatus() {
        // Check if user is logged in
        boolean isLoggedIn = SharedPreferencesManager.getInstance(getContext()).isLoggedIn();

        if (isLoggedIn) {
            NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_mainMenuFragment);
        } else {
            NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment);
        }
    }
}
