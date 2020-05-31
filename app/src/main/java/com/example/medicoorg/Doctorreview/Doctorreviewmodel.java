package com.example.medicoorg.Doctorreview;

public class Doctorreviewmodel {
    public Doctorreviewmodel(){

    }
    String patientimage,patientname,patientreview;

    public Doctorreviewmodel(String patientimage, String patientname, String patientreview) {
        this.patientimage = patientimage;
        this.patientname = patientname;
        this.patientreview = patientreview;
    }

    public String getPatientimage() {
        return patientimage;
    }

    public void setPatientimage(String patientimage) {
        this.patientimage = patientimage;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getPatientreview() {
        return patientreview;
    }

    public void setPatientreview(String patientreview) {
        this.patientreview = patientreview;
    }
}
