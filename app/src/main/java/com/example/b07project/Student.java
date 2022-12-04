package com.example.b07project;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    public String name;
    public String email;
    public String id;
    public String program;
    public ArrayList<String> taken;



    public Student(){
        this.taken = new ArrayList<>();

    }

    public Student(String i,String p, String e, String n)
    {
            this();
            this.name = n;
            this.id = i;
            this.program =p;
            this.email = e;

    }


}
