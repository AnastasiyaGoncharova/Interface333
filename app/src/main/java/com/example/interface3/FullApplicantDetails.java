package com.example.interface3;

import java.util.List;

public class FullApplicantDetails {
    private String firstName;
    private String lastName;
    private String middleName;
    private int eGE;
    private int priority;
    private String profile;
    private List<String> checkbox;
    private String phoneNumber;
    private String comment;
    private String id;

    public FullApplicantDetails(String firstName, String lastName,
                                String middleName, String phoneNumber, int eGE, int priority,
                                String profile, List<String> checkbox,
                                 String comment) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.phoneNumber = phoneNumber;
        this.eGE = eGE;
        this.priority = priority;
        this.profile = profile;
        this.checkbox = checkbox;
        this.comment = comment;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public List<String> getCheckbox() {
        return checkbox;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getComment() {
        return comment;
    }

    public int geteGE() {
        return eGE;
    }

    public int getPriority() {
        return priority;
    }

    public String getProfile() {
        return profile;
    }
}
