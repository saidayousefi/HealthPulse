package com.example.healthtracker.ui.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.healthtracker.data.local.model.Profile;
import com.example.healthtracker.data.repository.ProfileRepository;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepository repository;
    private MutableLiveData<Profile> currentProfile;

    public ProfileViewModel(Application application) {
        super(application);
        repository = new ProfileRepository(application);
        currentProfile = new MutableLiveData<>();
        loadCurrentProfile();
    }

    public LiveData<Profile> getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(Profile profile) {
        currentProfile.setValue(profile);
    }

    public void insert(Profile profile) {
        repository.insert(profile);
        currentProfile.setValue(profile);
    }

    public void update(Profile profile) {
        repository.update(profile);
        currentProfile.setValue(profile);
    }

    private void loadCurrentProfile() {
        // Load the current profile from the repository
        Profile profile = repository.getCurrentProfile();
        if (profile != null) {
            currentProfile.setValue(profile);
        }
    }
}
