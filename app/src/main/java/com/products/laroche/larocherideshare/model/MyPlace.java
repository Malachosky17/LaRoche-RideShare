package com.products.laroche.larocherideshare.model;

import java.io.Serializable;

// TODO Investigate why we implement Serializable
public class MyPlace implements Serializable {

    private final String mName;
    private final double[] mLocation = new double[2];

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

}
