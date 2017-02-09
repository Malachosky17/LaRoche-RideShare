package com.products.laroche.larocherideshare.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Malac on 2/7/2017.
 *
 * @author: Malac
 */

public class MyPlace implements Serializable {

    private String mName;
    private double[] mLocation = new double[2];

    public MyPlace() {

    }

    public MyPlace(String name, double latitude, double longitude) {
        this.mName = name;
        this.mLocation[0] = latitude;
        this.mLocation[1] = longitude;
    }

    public String getName() {
        return mName;
    }

    public double[] getLocation() {
        return mLocation;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setLocation(double latitude, double longitude) {
        this.mLocation[0] = latitude;
        this.mLocation[1] = longitude;
    }

}
