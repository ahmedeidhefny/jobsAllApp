package com.udacity.ahmed_eid.jobsallapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Company {
    private String userId;
    private String compName;
    private String compLogo ;
    private String compWebsite;
    private String compCity;
    private String compCountry;
    private String compProfile;
    private String compFounderDate;
    private String compCategory;
    private String userType;

    public Company(){}

    public Company(String userId,String compName,String userType, String compWebsite, String compCategory, String compFounderDate, String compCity, String compCountry, String compProfile) {
        this.userId = userId ;
        this.compName = compName;
        this.compWebsite = compWebsite;
        this.compCity = compCity;
        this.compCountry = compCountry;
        this.compProfile = compProfile;
        this.compFounderDate = compFounderDate;
        this.compCategory = compCategory;
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getCompLogo() {
        return compLogo;
    }

    public String getCompName() {
        return compName;
    }

    public String getCompWebsite() {
        return compWebsite;
    }

    public String getCompCity() {
        return compCity;
    }

    public String getCompCountry() {
        return compCountry;
    }

    public String getCompProfile() {
        return compProfile;
    }

    public String getCompFounderDate() {
        return compFounderDate;
    }

    public String getCompCategory() {
        return compCategory;
    }
}
