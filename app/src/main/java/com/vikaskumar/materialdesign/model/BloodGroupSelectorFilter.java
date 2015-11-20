package com.vikaskumar.materialdesign.model;

/**
 * Created by vikas kumar on 11/13/2015.
 */
public class BloodGroupSelectorFilter {
    public static String bloodgroup;
    public static String getBloodgroup(){
        return bloodgroup;
    }
    public static void setBloodgroup(String bloodGroupToBeFilter){

        if (bloodGroupToBeFilter.contains("A+")){
            bloodgroup="A+";
        }
        else if (bloodGroupToBeFilter.contains("B+")){
            bloodgroup="B+";
        }
        else if (bloodGroupToBeFilter.contains("AB+")){
            bloodgroup="AB+";
        }
        else if (bloodGroupToBeFilter.contains("O+")){
            bloodgroup="O+";
        }
        else if (bloodGroupToBeFilter.contains("O-")){
            bloodgroup="O-";
        }
        else if (bloodGroupToBeFilter.contains("AB-")){
            bloodgroup="AB-";
        }
        else if (bloodGroupToBeFilter.contains("B-")){
            bloodgroup="B-";
        }
        else if (bloodGroupToBeFilter.contains("A-")){
            bloodgroup="A-";
        }
    }

}
