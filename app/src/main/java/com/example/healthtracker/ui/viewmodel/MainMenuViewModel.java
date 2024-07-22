package com.example.healthtracker.ui.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.healthtracker.MainMenuFragmentDirections;

public class MainMenuViewModel extends ViewModel {

    public void navigateToViewProfile(View view) {
        NavDirections action = MainMenuFragmentDirections.actionMainMenuFragmentToViewProfilesFragment();
        Navigation.findNavController(view).navigate(action);
    }

    public void navigateToViewHealthRecords(View view) {
        NavDirections action = MainMenuFragmentDirections.actionMainMenuFragmentToViewHealthRecordsFragment();
        Navigation.findNavController(view).navigate(action);
    }

    public void navigateToAddHealthRecord(View view) {
        NavDirections action = MainMenuFragmentDirections.actionMainMenuFragmentToAddHealthRecordFragment();
        Navigation.findNavController(view).navigate(action);
    }

    public void navigateToNotificationSettings(View view) {
        NavDirections action = MainMenuFragmentDirections.actionMainMenuFragmentToNotificationSettingsFragment();
        Navigation.findNavController(view).navigate(action);
    }
}
