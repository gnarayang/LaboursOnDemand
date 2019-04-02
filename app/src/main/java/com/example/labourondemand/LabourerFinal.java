package com.example.labourondemand;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class LabourerFinal extends User implements Serializable {

    private Services currentService;
    private Double averageRating;
    private boolean isBusy;
    private ArrayList<String> skill;
    private Long workExperience;
    private ArrayList<ServicesFinal> incomingServices, historyServices;

    public LabourerFinal() {

    }

    public LabourerFinal(Services currentService, Boolean isBusy, ArrayList<String> skill, Long workExperience,
                         ArrayList<ServicesFinal> incomingServices, ArrayList<ServicesFinal> historyServices) {
        this.currentService = currentService;
        this.isBusy = isBusy;
        this.skill = skill;
        this.workExperience = workExperience;
        this.incomingServices = incomingServices;
        this.historyServices = historyServices;
    }

    public LabourerFinal(String id, String name, String image, String dob, String city, String state, String addressLine1,
                         String addressLine2, String addressLine3, Long phone, Long wallet, ArrayList<String> services, String email, String password) {
        super(id, name, image, dob, city, state, addressLine1, addressLine2, addressLine3, phone, wallet, services, email, password);
    }

    public LabourerFinal(String id, String name, String image, String dob, String city, String state, String addressLine1, String addressLine2,
                         String addressLine3, Long phone, Long wallet, ArrayList<String> services, Boolean isBusy, ArrayList<String> skill,
                         Long workExperience , String email, String password) {
        super(id, name, image, dob, city, state, addressLine1, addressLine2, addressLine3, phone, wallet, services, email, password);
        this.isBusy = isBusy;
        this.skill = skill;
        this.workExperience = workExperience;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Services getCurrentService() {
        return currentService;
    }

    public void setCurrentService(Services currentService) {
        this.currentService = currentService;
    }

    public Boolean getBusy() {
        return isBusy;
    }

    public void setBusy(Boolean busy) {
        isBusy = busy;
    }

    public ArrayList<String> getSkill() {
        return skill;
    }

    public void setSkill(ArrayList<String> skill) {
        this.skill = skill;
    }

    public Long getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Long workExperience) {
        this.workExperience = workExperience;
    }

    public ArrayList<ServicesFinal> getIncomingServices() {
        return incomingServices;
    }

    public void setIncomingServices(ArrayList<ServicesFinal> incomingServices) {
        this.incomingServices = incomingServices;
    }

    public ArrayList<ServicesFinal> getHistoryServices() {
        return historyServices;
    }

    public void setHistoryServices(ArrayList<ServicesFinal> historyServices) {
        this.historyServices = historyServices;
    }

    @Override
    public String toString() {
        return "LabourerFinal{" +
                "currentService=" + currentService +
                ", isBusy=" + isBusy +
                ", skill=" + skill +
                ", workExperience=" + workExperience +
                ", incomingServices=" + incomingServices +
                ", historyServices=" + historyServices +
                '}';
    }
}

