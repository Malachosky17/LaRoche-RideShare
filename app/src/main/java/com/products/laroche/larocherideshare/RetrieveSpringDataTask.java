package com.products.laroche.larocherideshare;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.products.laroche.larocherideshare.model.Constants;
import com.products.laroche.larocherideshare.model.MyPlace;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Malac on 2/7/2017.
 *
 * @author: Malac
 */

/*
 * Grabs sample Data:
 * [
 *  {"name":"Sunoco Gas","location":[-80.023772,40.557819]},
 *  {"name":"Sunoco Gas 2","location":[-80.036587,40.572271]},
 *  {"name":"Passavant Hospital","location":[51.06,90]},
 *  {"name":"Sunoco Gas","location":[-80.023772,40.557819]},
 *  {"name":"Sunoco Gas 2","location":[-80.036587,40.572271]},
 *  {"name":"Passavant Hospital","location":[51.06,90]},
 *  {"name":"Sunoco Gas","location":[-80.023772,40.557819]},
 *  {"name":"Sunoco Gas 2","location":[-80.036587,40.572271]},
 *  {"name":"Passavant Hospital","location":[51.06,90]}
 * ]
*/
public class RetrieveSpringDataTask extends AsyncTask<String, Void, ArrayList<MyPlace>> {

    private static final String LOGTAG = RetrieveSpringDataTask.class.getSimpleName();

    @Override
    protected ArrayList<MyPlace> doInBackground(String... params) {
        Log.i(LOGTAG, "Extension passed in: " + params[0]);
        ArrayList<MyPlace> myPlaces = new ArrayList<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            JSONArray reader = new JSONArray(restTemplate.getForObject(Constants.HOST_URL + params[0], String.class, "Android"));
            for(int i = 0; i < reader.length(); i++) {
                JSONObject obj = reader.getJSONObject(i);
                String name = obj.getString("name");
                JSONArray location = obj.getJSONArray("location");
                MyPlace place = new MyPlace(name, (double)location.get(0), (double)location.get(1));
                myPlaces.add(place);
                Log.i(LOGTAG, String.format(Locale.ENGLISH, "Info: %s \t LatLong:(%f, %f)", name, (double)location.get(0), (double)location.get(1)));
            }
            return myPlaces;
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage(), e);
        }
        return null;
    }
}
