package com.example.medicoorg.doctorappointmentdirectory;

public class patientmodel {

    public patientmodel() {
    }
    String patientprofileimage,patientusername,patientnumber;

    public patientmodel(String patientprofileimage, String patientusername, String patientnumber) {
        this.patientprofileimage = patientprofileimage;
        this.patientusername = patientusername;
        this.patientnumber = patientnumber;
    }

    public String getPatientprofileimage() {
        return patientprofileimage;
    }

    public void setPatientprofileimage(String patientprofileimage) {
        this.patientprofileimage = patientprofileimage;
    }

    public String getPatientusername() {
        return patientusername;
    }

    public void setPatientusername(String patientusername) {
        this.patientusername = patientusername;
    }

    public String getPatientnumber() {
        return patientnumber;
    }

    public void setPatientnumber(String patientnumber) {
        this.patientnumber = patientnumber;
    }
}
