package com.example.myapplication.service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.api.WorldClockApi;
import com.example.myapplication.model.EasternStandardTimeModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WorldClockService {

    private WorldClockApi worldClockApi;

    private MutableLiveData<EasternStandardTimeModel> easternStandardTime = new MutableLiveData<>();
    private MutableLiveData<List<String>> cities = new MutableLiveData<>();


    private static final String BASE_URL = "https://worldtimeapi.org";

    public WorldClockService() {

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.addInterceptor(logging);

        OkHttpClient okHttpClient = clientBuilder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        worldClockApi = retrofit.create(WorldClockApi.class);
    }

    public LiveData<EasternStandardTimeModel> getEasternStandardTime() {
        return easternStandardTime;
    }

    public LiveData<List<String>> getCities() {
        return cities;
    }

    public void invokeBudapestService() {

        Call<EasternStandardTimeModel> call = worldClockApi.getEasternStandardTimeForBudapest();

        call.enqueue(new Callback<EasternStandardTimeModel>() {
            @Override
            public void onResponse(
                    Call<EasternStandardTimeModel> call,
                    Response<EasternStandardTimeModel> response) {

                if (response.isSuccessful()) {
                    easternStandardTime.setValue(response.body());
                }
            }

            @Override
            public void onFailure(
                    Call<EasternStandardTimeModel> call,
                    Throwable t) {

            }
        });
    }

    public void invokeCityService(String region, String city) {

        Call<EasternStandardTimeModel> call = worldClockApi.getEasternStandardTimeForRegionAndCity(region, city);

        call.enqueue(new Callback<EasternStandardTimeModel>() {
            @Override
            public void onResponse(
                    Call<EasternStandardTimeModel> call,
                    Response<EasternStandardTimeModel> response) {

                if (response.isSuccessful()) {
                    easternStandardTime.setValue(response.body());
                }
            }

            @Override
            public void onFailure(
                    Call<EasternStandardTimeModel> call,
                    Throwable t) {

            }
        });
    }

    public void showCities() {

        Call<ArrayList<String>> call = worldClockApi.getCities();

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(
                    Call<ArrayList<String>> call,
                    Response<ArrayList<String>> response) {

                if (response.isSuccessful()) {
                    cities.setValue(response.body());
                }
            }

            @Override
            public void onFailure(
                    Call<ArrayList<String>> call,
                    Throwable t) {

            }
        });
    }

    public void invokeCityService(String continent) {

        Call<ArrayList<String>> call = worldClockApi.getCitiesForContinent(continent);

        call.enqueue(new Callback<ArrayList<String>>() {
            @Override
            public void onResponse(
                    Call<ArrayList<String>> call,
                    Response<ArrayList<String>> response) {

                if (response.isSuccessful()) {
                    cities.setValue(response.body());
                }
            }

            @Override
            public void onFailure(
                    Call<ArrayList<String>> call,
                    Throwable t) {

            }
        });


    }
}
