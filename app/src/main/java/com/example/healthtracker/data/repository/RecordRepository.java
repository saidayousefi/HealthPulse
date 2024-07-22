package com.example.healthtracker.data.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import com.example.healthtracker.data.local.AppDatabase;
import com.example.healthtracker.data.local.dao.RecordDao;
import com.example.healthtracker.data.local.model.RecordData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecordRepository {
    private RecordDao recordDao;
    private LiveData<List<RecordData>> allRecords;
    private ExecutorService executorService;

    public RecordRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        recordDao = database.recordDao();
        allRecords = recordDao.getAllRecords();
        executorService = Executors.newFixedThreadPool(2);
    }

    public LiveData<List<RecordData>> getAllRecords() {

        return allRecords;
    }

    public void insert(RecordData recordData) {
        executorService.execute(() -> recordDao.insert(recordData));
    }

    public void delete(RecordData recordData) {
        executorService.execute(() -> recordDao.delete(recordData));
    }

    public LiveData<RecordData> getRecordById(int id) {

        return recordDao.getRecordById(id);
    }

    public void deleteById(int id) {
        executorService.execute(() -> recordDao.deleteById(id));
    }

    public void update(RecordData recordData) {
        executorService.execute(() -> recordDao.update(recordData));
    }
}
