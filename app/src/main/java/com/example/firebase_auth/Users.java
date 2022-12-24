package com.example.firebase_auth;

public class Users {
    String Uid;
    String name;
    String email;
    String imageURI;
    String Usn;

    public Users() {
    }


    public Users(String uid, String name, String email, String imageURI, String usn) {
        Uid = uid;
        this.name = name;
        this.email = email;
        this.imageURI = imageURI;
        Usn = usn;
    }

    public String getUsn() {
        return Usn;
    }

    public void setUsn(String usn) {
        Usn = usn;
    }


    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
