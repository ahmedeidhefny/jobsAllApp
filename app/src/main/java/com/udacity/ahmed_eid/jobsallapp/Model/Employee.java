package com.udacity.ahmed_eid.jobsallapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Employee implements Parcelable {
    private String userId;
    private String userType;
    private String employeeName;
    private String jobTitle;
    private String employeeImage, employeeResumeFile;
    private String phone;
    private String gender, nationality;
    private String empCountry;
    private String empCity;
    private String birthOfDate;
    private String aboutMeSummery;
    private String empCategory;
    private String militaryStatus;
    private String maritalStatus;

    public Employee() {
    }

    public Employee(String userId, String employeeName, String userType, String jobTitle, String phone, String gender, String nationality, String empCountry, String empCity, String birthOfDate, String aboutMeSummery, String empCategory,
                    String militaryStatus, String maritalStatus) {
        this.userId = userId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.maritalStatus = maritalStatus;
        this.militaryStatus = militaryStatus;
        this.phone = phone;
        this.gender = gender;
        this.nationality = nationality;
        this.empCountry = empCountry;
        this.empCity = empCity;
        this.birthOfDate = birthOfDate;
        this.aboutMeSummery = aboutMeSummery;
        this.empCategory = empCategory;
        this.userType = userType;
    }

    public Employee(String userId, String employeeImage, String employeeResumeFile, String employeeName, String userType, String jobTitle, String phone, String gender, String nationality, String empCountry, String empCity, String birthOfDate, String aboutMeSummery, String empCategory,
                    String militaryStatus, String maritalStatus) {
        this.userId = userId;
        this.employeeName = employeeName;
        this.jobTitle = jobTitle;
        this.maritalStatus = maritalStatus;
        this.militaryStatus = militaryStatus;
        this.phone = phone;
        this.gender = gender;
        this.nationality = nationality;
        this.empCountry = empCountry;
        this.empCity = empCity;
        this.birthOfDate = birthOfDate;
        this.aboutMeSummery = aboutMeSummery;
        this.empCategory = empCategory;
        this.userType = userType;
        this.employeeImage = employeeImage;
        this.employeeResumeFile = employeeResumeFile ;
    }


    protected Employee(Parcel in) {
        userId = in.readString();
        userType = in.readString();
        employeeName = in.readString();
        jobTitle = in.readString();
        employeeImage = in.readString();
        employeeResumeFile = in.readString();
        phone = in.readString();
        gender = in.readString();
        nationality = in.readString();
        empCountry = in.readString();
        empCity = in.readString();
        birthOfDate = in.readString();
        aboutMeSummery = in.readString();
        empCategory = in.readString();
        militaryStatus = in.readString();
        maritalStatus = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userType);
        dest.writeString(employeeName);
        dest.writeString(jobTitle);
        dest.writeString(employeeImage);
        dest.writeString(employeeResumeFile);
        dest.writeString(phone);
        dest.writeString(gender);
        dest.writeString(nationality);
        dest.writeString(empCountry);
        dest.writeString(empCity);
        dest.writeString(birthOfDate);
        dest.writeString(aboutMeSummery);
        dest.writeString(empCategory);
        dest.writeString(militaryStatus);
        dest.writeString(maritalStatus);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Employee> CREATOR = new Creator<Employee>() {
        @Override
        public Employee createFromParcel(Parcel in) {
            return new Employee(in);
        }

        @Override
        public Employee[] newArray(int size) {
            return new Employee[size];
        }
    };

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

