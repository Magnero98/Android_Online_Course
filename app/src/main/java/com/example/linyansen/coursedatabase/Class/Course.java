package com.example.linyansen.coursedatabase.Class;

/**
 * Created by Lin Yansen on 11/27/2017.
 */

public class Course
{
    private int id;
    private String title;
    private int credit;
    private int quota;

    public int getId(){ return(this.id); }
    public String getTitle(){ return(this.title); }
    public int getCredit(){ return(this.credit); }
    public int getQuota(){ return(this.quota); }

    public void setId(int id){ this.id = id; }
    public void setTitle(String title){ this.title = title; }
    public void setCredit(int credit){ this.credit = credit; }
    public void setQuota(int quota){ this.quota = quota; }

    public Course(){}

    public Course(int id, String title, int credit, int quota)
    {
        this.id = id;
        this.title = title;
        this.credit = credit;
        this.quota = quota;
    }
}
