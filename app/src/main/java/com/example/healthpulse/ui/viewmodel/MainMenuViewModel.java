package com.example.healthpulse.ui.viewmodel;

import android.view.View;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import com.example.healthpulse.R;

public class MainMenuViewModel extends ViewModel {

    public void navigateToViewProfile(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_viewProfileFragment);
    }

    public void navigateToViewHealthRecords(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_viewHealthRecordsFragment);
    }

    public void navigateToAddHealthRecord(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_addHealthRecordFragment);
    }

    public void navigateToNotificationSettings(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_notificationSettingsFragment);
    }

    public void navigateToEducationalContent(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_EducationalContentFragment);
    }

    public void navigateToChart(View view) {
        Navigation.findNavController(view).navigate(R.id.action_mainMenuFragment_to_ChartFragment);
    }
}
