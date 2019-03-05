package com.udacity.ahmed_eid.jobsallapp.Model;

public class SocialMedia {

    private String empId, phoneNumber, whatsApp, mail, facebook, twitter, linkedIn;

    public SocialMedia() {
    }

    public SocialMedia(String empId, String phoneNumber, String whatsApp, String mail, String facebook, String twitter, String linkedIn) {
        this.phoneNumber = phoneNumber;
        this.whatsApp = whatsApp;
        this.mail = mail;
        this.facebook = facebook;
        this.twitter = twitter;
        this.linkedIn = linkedIn;
        this.empId = empId;
    }


    public String getMail() {
        return mail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWhatsApp() {
        return whatsApp;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getLinkedIn() {
        return linkedIn;
    }
}
