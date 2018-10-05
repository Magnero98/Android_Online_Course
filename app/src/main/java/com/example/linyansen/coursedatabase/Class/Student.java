package com.example.linyansen.coursedatabase.Class;

/**
 * Created by Lin Yansen on 11/27/2017.
 */

public class Student
{
    private int id;
    private String fullname;
    private String username;
    private String gender;
    private String email;
    private String phone;
    private String image;

    public int getId(){ return (this.id); }
    public String getFullname(){ return (this.fullname); }
    public String getUsername(){ return (this.username); }
    public String getGender(){ return (this.gender); }
    public String getEmail(){ return (this.email); }
    public String getPhone(){ return (this.phone); }
    public String getImage(){ return (this.image); }

    public void setId(int id){ this.id = id; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public void setUsername(String username) { this.fullname = username; }
    public void setGender(String gender) { this.fullname = gender; }
    public void setEmail(String email) { this.fullname = email; }
    public void setPhone(String phone) { this.fullname = phone; }
    public void setImage(String image) { this.fullname = image; }

    public Student(){}

    public Student(int id, String fullname, String username, String gender, String email, String phone, String image)
    {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.image = image;
    }
}
