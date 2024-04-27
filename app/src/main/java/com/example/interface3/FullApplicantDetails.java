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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public List<String> getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(List<String> checkbox) {
        this.checkbox = checkbox;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int geteGE() {
        return eGE;
    }

    public void seteGE(int eGE) {
        this.eGE = eGE;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
