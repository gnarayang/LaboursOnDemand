package com.example.labourondemand;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;

public class    User implements Serializable/*implements Parcelable*/{

    private String name, image, dob, city, state, addressLine1, addressLine2, addressLine3, id;
    private GeoPoint currentLocation;
    private Long phone,wallet;
    private ArrayList<String> services;
    private ServicesFinal servicesFinal;


    public User() {
    }

    public User(String id, String name, String image, String dob, String city, String state, String addressLine1, String addressLine2,
                String addressLine3, Long phone, Long wallet, ArrayList<String> services) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.dob = dob;
        this.city = city;
        this.state = state;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.phone = phone;
        this.wallet = wallet;
        this.services = services;
    }


    /*protected User(Parcel in) {
        name = in.readString();
        image = in.readString();
        dob = in.readString();
        city = in.readString();
        state = in.readString();
        addressLine1 = in.readString();
        addressLine2 = in.readString();
        addressLine3 = in.readString();
        id = in.readString();
        if (in.readByte() == 0) {
            phone = null;
        } else {
            phone = in.readLong();
        }
        if (in.readByte() == 0) {
            wallet = null;
        } else {
            wallet = in.readLong();
        }
        services = in.createStringArrayList();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(image);
        dest.writeString(dob);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(addressLine1);
        dest.writeString(addressLine2);
        dest.writeString(addressLine3);
        dest.writeString(id);
        if (phone == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(phone);
        }
        if (wallet == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(wallet);
        }
        dest.writeStringList(services);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public GeoPoint getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(GeoPoint currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public Long getWallet() {
        return wallet;
    }

    public void setWallet(Long wallet) {
        this.wallet = wallet;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

}
