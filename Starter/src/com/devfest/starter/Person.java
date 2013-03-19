package com.devfest.starter;

public class Person {

    private String mFirstName;
    
    private String mLastName;

    private String mGender;


    /*
     * Default constructor for general object creation
     */
    public Person() {

    }


    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        this.mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        this.mLastName = lastName;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }
}
