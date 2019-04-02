package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CustomerFinal extends User implements Serializable {

    private ArrayList<ServicesFinal> historyServices, currentServices, incomingServices;
    private Double destinationLatitude, destinationLongitude;
    private HashMap<String, Marker> labourersLocation;
    private String notPaidService, notReviewedService;

    public String getNotPaidService() {
        return notPaidService;
    }

    public void setNotPaidService(String notPaidService) {
        this.notPaidService = notPaidService;
    }

    public String getNotReviewedService() {
        return notReviewedService;
    }

    public void setNotReviewedService(String notReviewedService) {
        this.notReviewedService = notReviewedService;
    }

    @Override
    public String toString() {
        return "CustomerFinal{" +
                "historyServices=" + historyServices +
                ", currentServices=" + currentServices +
                ", incomingServices=" + incomingServices +
                ", destinationLatitude=" + destinationLatitude +
                ", destinationLongitude=" + destinationLongitude +
                ", labourersLocation=" + labourersLocation +
                ", notPaidService='" + notPaidService + '\'' +
                ", notReviewedService='" + notReviewedService + '\'' +
                ", user ='" + super.toString() + '\'' +
                '}';
    }

    public CustomerFinal() {

    }



    public CustomerFinal(String id, String name, String image, String dob, String city, String state, String addressLine1, String addressLine2, String addressLine3,
                         Long phone, Long wallet, ArrayList<String> services, String email, String password) {
        super(id, name, image, dob, city, state, addressLine1, addressLine2, addressLine3, phone, wallet, services, email, password);
        this.historyServices = new ArrayList<ServicesFinal>();
        this.currentServices = new ArrayList<ServicesFinal>();
        this.incomingServices = new ArrayList<ServicesFinal>();

    }

    public Double getDestinationLatitude() {
        return destinationLatitude;
    }

    public void setDestinationLatitude(Double destinationLatitude) {
        this.destinationLatitude = destinationLatitude;
    }

    public Double getDestinationLongitude() {
        return destinationLongitude;
    }

    public void setDestinationLongitude(Double destinationLongitude) {
        this.destinationLongitude = destinationLongitude;
    }

    public ArrayList<ServicesFinal> getHistoryServices() {
        return historyServices;
    }

    public void setHistoryServices(ArrayList<ServicesFinal> historyServices) {
        this.historyServices = historyServices;
    }

    public ArrayList<ServicesFinal> getCurrentServices() {
        return currentServices;
    }

    public void setCurrentServices(ArrayList<ServicesFinal> currentServices) {
        this.currentServices = currentServices;
    }

    public ArrayList<ServicesFinal> getIncomingServices() {
        return incomingServices;
    }

    public void setIncomingServices(ArrayList<ServicesFinal> incomingServices) {
        this.incomingServices = incomingServices;
    }

   /* public GeoPoint getDestination() {
        return destination;
    }

    public void setDestination(GeoPoint destination) {
        this.destination = destination;
    }*/

    public HashMap<String, Marker> getLabourersLocation() {
        return labourersLocation;
    }

    public void setLabourersLocation(HashMap<String, Marker> labourersLocation) {
        this.labourersLocation = labourersLocation;
    }



}
