package com.example.healthpulse.data.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.healthpulse.data.local.model.RecordData;

import java.util.List;

@Dao
public interface RecordDao {

    @Insert
    void insert(RecordData recordData);

    @Update
    void update(RecordData recordData);

    @Delete
    void delete(RecordData recordData);

    @Query("DELETE FROM record_table WHERE id = :id")
    void deleteById(int id);

    @Query("SELECT * FROM record_table ORDER BY date DESC, time DESC")
    LiveData<List<RecordData>> getAllRecords();

    @Query("SELECT * FROM record_table WHERE id = :id")
    LiveData<RecordData> getRecordById(int id);

}
