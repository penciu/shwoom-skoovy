package com.skoovy.android;


import android.util.Log;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String birthday;
    public String email;

    public String firstname;
    private String lastname;
    private String nexmoPhoneNumber;
    private String password;
    private String phonePrefix;
    public String phoneCountryCode;
    private String phoneNumber;
    public String uid;
    private String username;

    public User() {
        //Default constructor
    }

    public String getUid() {
        return this.uid;
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

    public String getPhonePrefixCode() {
        return this.phonePrefix;
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

    public void setUid(String userUID) {
        this.uid = userUID;
        Log.d("User", "UID WAS SET");
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

    public void setPhoneCountryCode(String userCountry){
        this.phoneCountryCode = userCountry;
        Log.d("User", "PHONECOUNTRY WAS SET");
    }

    public void setPhonePrefixCode(String userCountryCode) {
        this.phonePrefix = userCountryCode;
        Log.d("User", "COUNTRY-CODE WAS SET");
    }

    public void setPhoneNumber(String userPhoneNumber) {
        this.phoneNumber = userPhoneNumber;
        Log.d("User", "PHONE NUMBER WAS SET");
        setNexmoPhoneNumber();
    }

    public void setPassword(String userPassword) {
        this.password = userPassword;
        Log.d("User", "PASSWORD WAS SET");
    }

    private void setNexmoPhoneNumber(){
        String phoneNumberData = getPhonePrefixCode().substring(2,getPhonePrefixCode().length()-1) + getPhoneNumber();
        nexmoPhoneNumber = phoneNumberData.replaceAll(" ", ""); //remove any remaining spaces
        Log.d("User", "NEXMO_PHONE_NUBMER (string):" + nexmoPhoneNumber);
    }

    @Override
    public String toString() {
        return "user [firstname=" + firstname + ", lastname=" + lastname + ", birthday=" + birthday + ", username=" + username + ", email=" + email + ", countrycode=" + phoneCountryCode + ", prefix=" + phonePrefix+ ", phonenumber=" + phoneNumber + ", nexmoPhoneNumber=" + nexmoPhoneNumber + ", password=" + password + ", uid=" + uid + "]";
    }

}
