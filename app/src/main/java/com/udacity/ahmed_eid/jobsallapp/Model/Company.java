package com.udacity.ahmed_eid.jobsallapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Company implements Parcelable {
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

    public Company(String compLogo) {
        this.compLogo = compLogo;
    }

    public Company(String userId, String compName, String userType, String compWebsite, String compCategory, String compFounderDate, String compCity, String compCountry, String compProfile) {
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

    public Company(String userId,String compLogo, String compName, String userType, String compWebsite, String compCategory, String compFounderDate, String compCity, String compCountry, String compProfile) {
        this.userId = userId ;
        this.compName = compName;
        this.compWebsite = compWebsite;
        this.compCity = compCity;
        this.compCountry = compCountry;
        this.compProfile = compProfile;
        this.compFounderDate = compFounderDate;
        this.compCategory = compCategory;
        this.userType = userType;
        this.compLogo = compLogo ;
    }


    protected Company(Parcel in) {
        userId = in.readString();
        compName = in.readString();
        compLogo = in.readString();
        compWebsite = in.readString();
        compCity = in.readString();
        compCountry = in.readString();
        compProfile = in.readString();
        compFounderDate = in.readString();
        compCategory = in.readString();
        userType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(compName);
        dest.writeString(compLogo);
        dest.writeString(compWebsite);
        dest.writeString(compCity);
        dest.writeString(compCountry);
        dest.writeString(compProfile);
        dest.writeString(compFounderDate);
        dest.writeString(compCategory);
        dest.writeString(userType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public String getUserId() {
        return userId;
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
