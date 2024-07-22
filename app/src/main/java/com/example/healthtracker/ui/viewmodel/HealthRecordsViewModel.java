package com.example.healthtracker.ui.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.healthtracker.data.repository.RecordRepository;
import com.example.healthtracker.data.local.model.RecordData;
import com.example.healthtracker.ui.Navigator;
import java.util.List;

public class HealthRecordsViewModel extends AndroidViewModel {

    private RecordRepository repository;
    private LiveData<List<RecordData>> allRecords;
    private MutableLiveData<List<RecordData>> records;
    private Navigator navigator;

    public HealthRecordsViewModel(Application application) {
        super(application);
        repository = new RecordRepository(application);
        allRecords = repository.getAllRecords();
        records = new MutableLiveData<>();
    }

    public void setNavigator(Navigator navigator) {
        this.navigator = navigator;
    }

    public LiveData<List<RecordData>> getAllRecords() {
        return allRecords;
    }

    public void insert(RecordData recordData) {
        repository.insert(recordData);
    }

    public void update(RecordData recordData) {
        repository.update(recordData);
    }

    public void delete(RecordData recordData) {
        repository.delete(recordData);
    }

    public LiveData<RecordData> getRecordById(int id) {
        return repository.getRecordById(id);
    }

    public void onBackClicked() {
        if (navigator != null) {
            navigator.onBackClicked();
        }
    }
}
