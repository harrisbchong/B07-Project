package com.example.b07project;

public enum UTSCSessions implements UniversitySession {
    Fall, Winter, Summer;

    @Override
    public String getSessionName() {
        return name();
    }

    @Override
    public int getSessionOrder() {
        return ordinal();
    }
}
