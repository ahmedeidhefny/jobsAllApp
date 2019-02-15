package com.udacity.ahmed_eid.jobsallapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Job implements Parcelable {
    private String jobId, title;
    private String category;
    private String vacanciesNum;
    private String yearMin;
    private String yearMax;
    private String age, salary, expireDate;
    private String gender ;
    private String degreeLevel;
    private String jobType;
    private String nationality;
    private String country;
    private String city;
    private String jobDescription;
    private String jobRequirement;
    private String companyId;

    public Job() {
    }

    public Job(String jobId, String companyId, String title, String category,String gender, String age, String salary, String expireDate, String vacanciesNum, String yearMin, String yearMax, String degreeLevel, String jobType, String nationality, String country, String city, String jobDescription, String jobRequirement) {
        this.companyId = companyId;
        this.gender = gender ;
        this.title = title;
        this.category = category;
        this.age = age;
        this.salary = salary;
        this.expireDate = expireDate;
        this.vacanciesNum = vacanciesNum;
        this.yearMin = yearMin;
        this.yearMax = yearMax;
        this.degreeLevel = degreeLevel;
        this.jobType = jobType;
        this.nationality = nationality;
        this.country = country;
        this.city = city;
        this.jobDescription = jobDescription;
        this.jobRequirement = jobRequirement;
        this.jobId = jobId;
    }

    protected Job(Parcel in) {
        jobId = in.readString();
        title = in.readString();
        category = in.readString();
        vacanciesNum = in.readString();
        yearMin = in.readString();
        yearMax = in.readString();
        age = in.readString();
        salary = in.readString();
        expireDate = in.readString();
        degreeLevel = in.readString();
        jobType = in.readString();
        nationality = in.readString();
        country = in.readString();
        city = in.readString();
        jobDescription = in.readString();
        jobRequirement = in.readString();
        companyId = in.readString();
        gender = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jobId);
        dest.writeString(title);
        dest.writeString(category);
        dest.writeString(vacanciesNum);
        dest.writeString(yearMin);
        dest.writeString(yearMax);
        dest.writeString(age);
        dest.writeString(salary);
        dest.writeString(expireDate);
        dest.writeString(degreeLevel);
        dest.writeString(jobType);
        dest.writeString(nationality);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(jobDescription);
        dest.writeString(jobRequirement);
        dest.writeString(companyId);
        dest.writeString(gender);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Job> CREATOR = new Creator<Job>() {
        @Override
        public Job createFromParcel(Parcel in) {
            return new Job(in);
        }

        @Override
        public Job[] newArray(int size) {
            return new Job[size];
        }
    };

    public String getGender() {
        return gender;
    }

    public String getAge() {
        return age;
    }

    public String getSalary() {
        return salary;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public String getJobId() {
        return jobId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getVacanciesNum() {
        return vacanciesNum;
    }

    public String getYearMin() {
        return yearMin;
    }

    public String getYearMax() {
        return yearMax;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public String getJobType() {
        return jobType;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public String getJobRequirement() {
        return jobRequirement;
    }
}
