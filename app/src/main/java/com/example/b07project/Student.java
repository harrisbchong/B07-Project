package com.example.b07project;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Student {
    public String name;
    public String email;
    public String id;
    public List<String> taken;


    public Student(){
        this.taken = new ArrayList<>();

    }

    public Student(String i, String e, String n)
    {
            this();
            this.name = n;
            this.id = i;
            this.email = e;

    }


}
