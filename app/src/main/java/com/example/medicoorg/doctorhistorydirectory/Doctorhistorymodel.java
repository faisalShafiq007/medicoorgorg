package com.example.medicoorg.doctorhistorydirectory;

public class Doctorhistorymodel {
    public Doctorhistorymodel(){

    }
    String patientimage,patientname,patientnumber;

    public Doctorhistorymodel(String patientimage, String patientname, String patientnumber) {
        this.patientimage = patientimage;
        this.patientname = patientname;
        this.patientnumber = patientnumber;
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

    public String getPatientnumber() {
        return patientnumber;
    }

    public void setPatientnumber(String patientnumber) {
        this.patientnumber = patientnumber;
    }
}
