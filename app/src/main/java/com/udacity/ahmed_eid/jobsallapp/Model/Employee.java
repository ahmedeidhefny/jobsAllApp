package com.udacity.ahmed_eid.jobsallapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee {
    private String userId;
    private String userType;
    private String employeeName;
    private String jobTitle;
    private String employeeImage,employeeResumeFile;
    private String phone;
    private String gender,nationality;
    private String empCountry;
    private String empCity;
    private String birthOfDate;
    private String aboutMeSummery;
    private String empCategory;
    private String militaryStatus;
    private String maritalStatus;

    public Employee() {
    }

    public Employee(String userId, String employeeName, String userType, String jobTitle, String phone, String gender,String nationality, String empCountry, String empCity, String birthOfDate, String aboutMeSummery, String empCategory,
                    String militaryStatus,String maritalStatus ) {
        this.userId = userId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.maritalStatus = maritalStatus ;
        this.militaryStatus = militaryStatus;
        this.phone = phone;
        this.gender = gender;
        this.nationality = nationality ;
        this.empCountry = empCountry;
        this.empCity = empCity;
        this.birthOfDate = birthOfDate;
        this.aboutMeSummery = aboutMeSummery;
        this.empCategory = empCategory;
        this.userType = userType;
    }


    public String getEmployeeResumeFile() {
        return employeeResumeFile;
    }

    public String getMilitaryStatus() {
        return militaryStatus;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getEmployeeImage() {
        return employeeImage;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getEmpCountry() {
        return empCountry;
    }

    public String getEmpCity() {
        return empCity;
    }

    public String getBirthOfDate() {
        return birthOfDate;
    }

    public String getAboutMeSummery() {
        return aboutMeSummery;
    }

    public String getEmpCategory() {
        return empCategory;
    }
}

