package com.uiotsoft.micro.oauth2.domain;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class User  {


    private int id;
    private String username;
    private String password;

  //  private String phone;
   // private String email;
    //Default user is initial when create database, do not delete
   // private boolean defaultUser = false;

    //private Date lastLoginTime;

    private List<Privilege> privileges = new ArrayList<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        //this.phone = phone;
        //this.email = email;
    }

/*    public boolean defaultUser() {
        return defaultUser;
    }*/

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Privilege> privileges() {
        return privileges;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{username='").append(username).append('\'');
        sb.append(", id='").append(id).append('\'');
        //sb.append(", guid='").append(guid).append('\'');
        sb.append('}');
        return sb.toString();
    }




    public User username(String username) {
        this.username = username;
        return this;
    }




    public User password(String password) {
        this.password = password;
        return this;
    }
}