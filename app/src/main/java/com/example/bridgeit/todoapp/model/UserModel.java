package com.example.bridgeit.todoapp.model;

public class UserModel {

    private   String fullname;
    private   String password;
     private String mobileNo;
    private String email;
    private String id;

   /* public UserModel( String fullname, String password, String mobileNo, String email) {
        id=1;
        this.fullname = fullname;
        this.password = password;
        this.mobileNo = mobileNo;
        this.email = email;
    }*/

    public UserModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname()
    {
        return fullname;
    }


    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}