package com.example.medicoorg.patientappointmentdirectory;

public class doctormodel{
    public doctormodel() {
    }

    String profileimage,hospital,specialized,username;


    public doctormodel(String profileimage, String hospital, String specialized, String username) {
        this.hospital = hospital;

        this.profileimage = profileimage;
        this.specialized = specialized;
        this.username = username;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }





    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getSpecialized() {
        return specialized;
    }

    public void setSpecialized(String specialized) {
        this.specialized = specialized;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}