package com.example.healthpulse.data.repository;

import android.app.Application;
import com.example.healthpulse.data.local.AppDatabase;
import com.example.healthpulse.data.local.dao.ProfileDao;
import com.example.healthpulse.data.local.model.Profile;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ProfileRepository {
    private final ProfileDao profileDao;
    private final LiveData<List<Profile>> allProfiles;
    private final ExecutorService executorService;

    public ProfileRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        profileDao = database.profileDao();
        allProfiles = profileDao.getAllProfiles();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<Profile>> getAllProfiles() {
        return allProfiles;
    }

    public void insert(Profile profile) {
        executorService.execute(() -> profileDao.insert(profile));
    }

    public void delete(Profile profile) {
        executorService.execute(() -> profileDao.delete(profile));
    }

    public void update(Profile profile) {
        executorService.execute(() -> profileDao.update(profile));
    }

    public Profile getProfileByEmail(String email) {
        // Using FutureTask to fetch the result in a synchronous manner
        FutureTask<Profile> futureTask = new FutureTask<>(() -> profileDao.getProfileByEmail(email));
        executorService.execute(futureTask);
        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Profile getCurrentProfile() {
        // Using FutureTask to fetch the result in a synchronous manner
        FutureTask<Profile> futureTask = new FutureTask<>(() -> profileDao.getCurrentProfile());
        executorService.execute(futureTask);
        try {
            return futureTask.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
