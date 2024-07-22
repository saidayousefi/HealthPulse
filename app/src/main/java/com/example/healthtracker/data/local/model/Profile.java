package com.example.healthtracker.data.local.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "profiles")
public class Profile implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private boolean conditionsAccepted;

    public Profile(String firstName, String lastName, String email, String password, String gender, boolean conditionsAccepted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.conditionsAccepted = conditionsAccepted;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isConditionsAccepted() {
        return conditionsAccepted;
    }

    public void setConditionsAccepted(boolean conditionsAccepted) {
        this.conditionsAccepted = conditionsAccepted;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Profile profile = (Profile) obj;

        if (id != profile.id) return false;
        if (conditionsAccepted != profile.conditionsAccepted) return false;
        if (!firstName.equals(profile.firstName)) return false;
        if (!lastName.equals(profile.lastName)) return false;
        if (!email.equals(profile.email)) return false;
        if (!password.equals(profile.password)) return false;
        return gender.equals(profile.gender);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + (conditionsAccepted ? 1 : 0);
        return result;
    }
}
