package com.example.healthtracker.ui.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.healthtracker.features.profile_management.SharedPreferencesManager;
import com.example.healthtracker.data.local.model.Profile;
import com.example.healthtracker.data.repository.ProfileRepository;

public class AuthViewModel extends AndroidViewModel {
    private final ProfileRepository profileRepository;
    private final SharedPreferencesManager sharedPreferencesManager;

    public AuthViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);
        sharedPreferencesManager = SharedPreferencesManager.getInstance(application);
    }

    public LiveData<Boolean> login(String email, String password) {
        MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
        Profile profile = profileRepository.getProfileByEmail(email);

        if (profile != null && profile.getPassword().equals(password)) {
            sharedPreferencesManager.setLoggedIn(true);
            loginResult.setValue(true);
        } else {
            loginResult.setValue(false);
        }
        return loginResult;
    }

    public void logout() {
        sharedPreferencesManager.setLoggedIn(false);
    }
}
