package com.example.healthpulse.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "record_table")
public class RecordData {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int profileId;
    private int systolic;
    private int diastolic;
    private int bloodSugar;
    private String date;
    private String time;



    public RecordData(int profileId, int systolic, int diastolic, int bloodSugar, String date, String time) {
        this.profileId = profileId;
        this.systolic = systolic;
        this.diastolic = diastolic;
        this.bloodSugar = bloodSugar;
        this.date = date;
        this.time = time;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public int getBloodSugar() {
        return bloodSugar;
    }

    public void setBloodSugar(int bloodSugar) {
        this.bloodSugar = bloodSugar;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecordData that = (RecordData) o;

        if (id != that.id) return false;
        if (profileId != that.profileId) return false;
        if (systolic != that.systolic) return false;
        if (diastolic != that.diastolic) return false;
        if (bloodSugar != that.bloodSugar) return false;
        if (!date.equals(that.date)) return false;
        return time.equals(that.time);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + profileId;
        result = 31 * result + systolic;
        result = 31 * result + diastolic;
        result = 31 * result + bloodSugar;
        result = 31 * result + date.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }
}
