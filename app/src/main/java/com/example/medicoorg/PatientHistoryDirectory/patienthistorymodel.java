package com.example.medicoorg.PatientHistoryDirectory;

public class patienthistorymodel {
    public patienthistorymodel(){
    }
    String doctorimage,doctorname,doctornumber;

    public patienthistorymodel(String doctorimage, String doctorname, String doctornumber) {
        this.doctorimage = doctorimage;
        this.doctorname = doctorname;
        this.doctornumber = doctornumber;
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
