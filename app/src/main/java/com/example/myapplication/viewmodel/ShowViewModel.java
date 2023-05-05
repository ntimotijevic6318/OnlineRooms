package com.example.myapplication.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.model.Event;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ShowViewModel extends ViewModel {


    MutableLiveData<List<Event>> events = new MutableLiveData<List<Event>>();
    List<Event> eventList = new ArrayList<Event>();

    public ShowViewModel() {

        List<Event> listToSubmit;

        listToSubmit = new ArrayList<>(eventList);
        events.setValue(listToSubmit);
    }

    public LiveData<List<Event>> getEvents() {
        return events;
    }

   // public void filter(String filter) {
     //   List<Car> filterd = carList.stream().filter(car -> car.getModel().toLowerCase().startsWith(filter.toLowerCase())).collect(Collectors.toList());
      //  cars.setValue(filterd);



    public void addEvent(Integer id , String name , String description , String date , String time , String url){
        //Event event = new Event(counterIncome++ , title , amount , description , file);

        //eventList.add(event);

        ArrayList<Event> listToSubmit = new ArrayList<>(eventList);
        events.setValue(listToSubmit);
    }


    }



