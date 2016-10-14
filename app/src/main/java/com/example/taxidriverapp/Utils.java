package com.example.taxidriverapp;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static String getAddress(double lat, double lon, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> address = null;
        try {
            address = geocoder.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (address != null && address.size() > 0) {
            String street = address.get(0).getThoroughfare();
            String houseNumber = address.get(0).getSubThoroughfare();
            return street + ", " + houseNumber;
        }
        return "";
    }

}
