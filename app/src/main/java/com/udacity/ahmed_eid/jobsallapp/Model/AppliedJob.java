package com.udacity.ahmed_eid.jobsallapp.Model;

public class AppliedJob {
    private String jobId;
    private String empId;
    private String empName;
    private String empImage;
    private String empJobTitle;

    public AppliedJob() {
    }

    public AppliedJob(String jobId, String empId, String empName, String empImage, String empJobTitle) {
        this.jobId = jobId;
        this.empId = empId;
        this.empName = empName;
        this.empImage = empImage;
        this.empJobTitle = empJobTitle;
    }

    public String getJobId() {
        return jobId;
    }

    public String getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

    public String getEmpImage() {
        return empImage;
    }

}
