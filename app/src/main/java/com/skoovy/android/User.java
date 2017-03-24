package com.skoovy.android;


import android.util.Log;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public String firstname;
    private String lastname;
    private String birthday;
    private String username;
    public String email;
    private String phoneCountryCode;
    private String phoneNumber;
    private String password;

    private String nexmoPhoneNumber;

    public User() {
        //Default constructor
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPhoneCountryCode() {
        return this.phoneCountryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword(){
        return this.password;
    }

    public String getNexmoPhoneNumber() {
        return nexmoPhoneNumber;
    }

    public void setFirstname(String userFirstName) {
        this.firstname = userFirstName;
        Log.d("User", "FIRST NAME WAS SET");
    }

    public void setLastname(String userLastName) {
        this.lastname = userLastName;
        Log.d("User", "LAST NAME WAS SET");
    }

    public void setBirthday(String userBirthday) {
        this.birthday = userBirthday;
        Log.d("User", "BIRTHDAY WAS SET");
    }

    public void setUsername(String userUserName) {
        this.username = userUserName;
        Log.d("User", "USERNAME WAS SET");
    }

    public void setEmail(String userEmail) {
        this.email = userEmail;
        Log.d("User", "EMAIL WAS SET");
    }

    public void setPhoneCountryCode(String userCountryCode) {
        this.phoneCountryCode = userCountryCode;
        Log.d("User", "COUNTRY-CODE WAS SET");
    }

    public void setPhoneNumber(String userPhoneNumber) {
        this.phoneNumber = userPhoneNumber;
        setNexmoPhoneNumber();
        Log.d("User", "PHONE NUMBER WAS SET");
        setNexmoPhoneNumber();
    }

    public void setPassword(String userPassword) {
        this.password = userPassword;
        Log.d("User", "PASSWORD WAS SET");
    }

    private void setNexmoPhoneNumber(){
        String phoneNumberData = getPhoneCountryCode().substring(2,getPhoneCountryCode().length()-1) + getPhoneNumber().substring(1,4) + getPhoneNumber().substring(6,9) + getPhoneNumber().substring(10,14);
        nexmoPhoneNumber = phoneNumberData.replaceAll(" ", ""); //remove any remaining spaces
        Log.d("User", "NEXMO_PHONE_NUBMER (string):" + nexmoPhoneNumber);
    }

    @Override
    public String toString() {
        return "user [firstname=" + firstname + ", lastname=" + lastname + ", birthday=" + birthday + ", username=" + username + ", email=" + email + ", countrycode=" + phoneCountryCode + ", phonenumber=" + phoneNumber + ", password=" + password + "]";
    }

}
