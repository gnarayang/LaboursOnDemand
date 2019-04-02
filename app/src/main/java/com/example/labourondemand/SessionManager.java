package com.example.labourondemand;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;

public class SessionManager {

    Gson gson;

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "LabourOnDemand";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        gson = new Gson();
    }

    public void saveServices(ServicesFinal servicesFinal){

        String json = gson.toJson(servicesFinal); // myObject - instance of MyObject
        editor.putString(servicesFinal.getServiceId(),json);
        editor.commit();

    }

    public ServicesFinal getService(String serviceId){
        String json = pref.getString(serviceId, "");
        ServicesFinal servicesFinal = gson.fromJson(json, ServicesFinal.class);
        return servicesFinal;
    }

    public void saveLabourer(LabourerFinal labourerFinal){

        String json = gson.toJson(labourerFinal);
        Log.d("jsonsaveLabourer",json+"!");
        editor.putString(labourerFinal.getId(),json);
        editor.commit();
    }

    public LabourerFinal getLabourer(String labourerUID){
        String json = pref.getString(labourerUID, "");
        Log.d("jsongetLabourer",json+"!");
        LabourerFinal labourerFinal = gson.fromJson(json, LabourerFinal.class);
        return labourerFinal;
    }

    public void saveCustomer(CustomerFinal customerFinal){

        String json = gson.toJson(customerFinal); // myObject - instance of MyObject
        Log.d("jsonsaveCustomer",json+"!");
        editor.putString(customerFinal.getId(),json);
        editor.commit();
    }

    public CustomerFinal getCustomer(String customerUID){
        String json = pref.getString(customerUID, "");
        Log.d("jsongetCustomer",json+"!");
        CustomerFinal customerFinal = gson.fromJson(json, CustomerFinal.class);
        return customerFinal;
    }

    /**
     * Create login session
     * */
    /*public void createLoginSession(String password, String email){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // commit changes
        editor.commit();
    }*/

    public void putEmailAndPassword(String email, String password){


        editor.putString("email",email);
        editor.putString("password",password);
        editor.commit();
    }
    public String getEmail(){
        return  pref.getString("email","");
    }

    public String getPassword(){
        return  pref.getString("password","");
    }
    /*
    *
    * */
    public void setType(String type){
        editor.putString("type",type);
        // commit changes
        editor.commit();
    }

    public Boolean isSetupCustomer(String s){
        String json = pref.getString(s, "");
        Log.d("json",json+"!");
        CustomerFinal customerFinal = gson.fromJson(json, CustomerFinal.class);
        if(customerFinal != null && customerFinal.getName()!=null){
            return true;
        }
        return false;
    }

    public Boolean isSetupLabourer(String s){
        String json = pref.getString(s, "");
        Log.d("json",json+"!");
        LabourerFinal labourerFinal = gson.fromJson(json, LabourerFinal.class);
        if(labourerFinal != null && labourerFinal.getName()!=null){
            return true;
        }
        return false;
    }

    public Boolean isSetup(String s){
        String name = pref.getString(s,null);
        Log.d("name",name+"!");
        if(name == null){
            return false;
        }
        return true;
    }

    /**
     * Setup Activity
     */

    public void createProfileLabourer(String name, String image, String dob, String city, String state, Long phone,
                              String addressLine1, String addressLine2, String addressLine3, ArrayList<String> skills, Long workExperience){

        editor.putString("name",name);
        editor.putString("image",image);
        editor.putString("dob",dob);
        editor.putString("city",city);
        editor.putString("state",state);
        editor.putLong("phone",phone);
        editor.putLong("workExperience",workExperience);
        editor.putString("addressLine1",addressLine1);
        editor.putString("addressLine2",addressLine2);
        editor.putString("addressLine3",addressLine3);
        editor.putStringSet("skill",new HashSet<String>(skills));

        editor.commit();
    }

    public void createProfileCustomer(String name, String image, String dob, String city, String state, Long phone,
                                      String addressLine1, String addressLine2, String addressLine3){

        editor.putString("name",name);
        editor.putString("image",image);
        editor.putString("dob",dob);
        editor.putString("city",city);
        editor.putString("state",state);
        editor.putLong("phone",phone);
        editor.putString("addressLine1",addressLine1);
        editor.putString("addressLine2",addressLine2);
        editor.putString("addressLine3",addressLine3);

        editor.commit();

    }

//    public void addServiceId()

    public Customer getCustomer(){
        Customer customer = new Customer();
        customer.setName(pref.getString("name",null));

        //TODO: set all values to customer and return
        return customer;
    }

    public Labourer getLabourer(){
        Labourer labourer = new Labourer();
        labourer.setName(pref.getString("name",null));

        //TODO: set all values to labourer and return
        return labourer;
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
   /* public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }*/

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}