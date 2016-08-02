package com.gainstudy.demo;


public class RegistrationValues {
    //name and address string
    private String FirstName;
    private String LastName;
    private String email;
    private String phone;
    private String Uid;
    private String address;
    private String state;
    private String city;
    private String pincode;
    private String UserId;
    private int index;
    private  int level;


    public RegistrationValues() {
      /*Blank default constructor essential for Firebase*/
    }

    //Getters and setters
    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String FirstName) {
        this.FirstName = FirstName;
    }

    public String getLastName() {
        return LastName;
    }
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return Uid;
    }
    public void setUid(String Uid) {
        this.Uid = Uid;
    }



    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }
    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getUserId() {
        return UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public void setIndex(int index){
        this.index = index;
    }
    public String getIndex() {
        return String.valueOf(index);
    }

    public void setLevel(int level){
        this.level = level;
    }
    public String getLevel() {
        return String.valueOf(level);
    }
}


