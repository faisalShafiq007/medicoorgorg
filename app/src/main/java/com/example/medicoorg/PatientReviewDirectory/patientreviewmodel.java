package com.example.medicoorg.PatientReviewDirectory;

public class patientreviewmodel {
    public patientreviewmodel(){

    }
    String doctorhospital,doctorimage,doctorname,doctornumber;

    public patientreviewmodel(String doctorhospital, String doctorimage, String doctorname, String doctornumber) {
        this.doctorhospital = doctorhospital;
        this.doctorimage = doctorimage;
        this.doctorname = doctorname;
        this.doctornumber = doctornumber;
    }

    public String getDoctorhospital() {
        return doctorhospital;
    }

    public void setDoctorhospital(String doctorhospital) {
        this.doctorhospital = doctorhospital;
    }

    public String getDoctorimage() {
        return doctorimage;
    }

    public void setDoctorimage(String doctorimage) {
        this.doctorimage = doctorimage;
    }

    public String getDoctorname() {
        return doctorname;
    }

    public void setDoctorname(String doctorname) {
        this.doctorname = doctorname;
    }

    public String getDoctornumber() {
        return doctornumber;
    }

    public void setDoctornumber(String doctornumber) {
        this.doctornumber = doctornumber;
    }
}
