package com.udacity.ahmed_eid.jobsallapp.Model;

public class SavedJob {

    private String jobId;
    private String userId ;

    public SavedJob() {

    }

    public SavedJob(String jobId, String userId) {
        this.jobId = jobId;
        this.userId = userId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getUserId() {
        return userId;
    }
}
