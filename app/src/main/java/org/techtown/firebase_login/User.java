package org.techtown.firebase_login;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {

    public String userId;
    public String userPwd;
    public String userName;
    public String userAge;
    public String petName;
    public String petAge;
    public String petSpecies;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String userPwd, String userName, String userAge, String petName, String petAge, String petSpecies) {
        this.userId = userId;
        this.userPwd = userPwd;
        this.userName =userName;
        this.userAge = userAge;
        this.petName = petName;
        this.petAge = petAge;
        this.petSpecies = petSpecies;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetAge() {
        return petAge;
    }

    public void setPetAge(String petAge) {
        this.petAge = petAge;
    }

    public String getPetSpecies() {
        return petSpecies;
    }

    public void setPetSpecies(String petSpecies) {
        this.petSpecies = petSpecies;
    }

    @Override
    public String toString() {
        return "User{" +
                "ID='" + userId + '\'' +
                ", password='" + userPwd + '\'' +
                "user name='" + userName + '\'' +
                "user age='" + userAge + '\'' +
                "pet name='" + petName + '\'' +
                "pet age='" + petAge + '\'' +
                "pet species='" + petSpecies + '\'' +
                '}';
    }
}