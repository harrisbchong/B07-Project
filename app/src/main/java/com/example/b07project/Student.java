package com.example.b07project;

import java.util.ArrayList;

public class Student {
    public String name;
    public  String email;
    public String id;
    public String program;

    public ArrayList<String> tcourse;



    public Student(){}

    public Student(String i,String p, String e, String n)
    {
            this.name = n;
            this.id = i;
            this.program =p;
            this.email = e;
    }


}
