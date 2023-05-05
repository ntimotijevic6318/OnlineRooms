package com.example.myapplication.api;

import com.example.myapplication.model.EasternStandardTimeModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WorldClockApi {

    @GET("/api/timezone/Europe/Budapest")
    Call<EasternStandardTimeModel> getEasternStandardTimeForBudapest();

    @GET("/api/timezone/{region}/{city}")
    Call<EasternStandardTimeModel> getEasternStandardTimeForRegionAndCity(@Path(value = "region") String myRegion, @Path(value = "city") String myCity);

    @GET("/api/timezone/Europe")
    Call<ArrayList<String>> getCities();


    @GET("/api/timezone/{continent}/")
    Call<ArrayList<String>> getCitiesForContinent(@Path(value = "continent") String continent);
}
