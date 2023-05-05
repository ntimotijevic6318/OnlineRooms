package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.myapplication.database.MyRoomDatabase;
import com.example.myapplication.entity.Meeting;
import com.example.myapplication.model.EasternStandardTimeModel;
import com.example.myapplication.service.WorldClockService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    Button checkTime , setDate , saveEvent ;
    Spinner spinner;
    AutoCompleteTextView textView;
    final WorldClockService worldClockService = new WorldClockService();
    private DatePickerDialog datePickerDialog;
    List<String> copy  = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initAll();
        roomDatabase(); 
    }

    private void roomDatabase() {



      EditText name = findViewById(R.id.name);
      EditText description  = findViewById(R.id.description);
      EditText url = findViewById(R.id.url) ;
      Button setTime = findViewById(R.id.setTime) ;
      String date = "06.07.21";
        final MyRoomDatabase myRoomDatabase = MyRoomDatabase.getDatabase(this);

        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long meetingId = myRoomDatabase.MeetingDao().insert(new Meeting(name.getText().toString() , description.getText().toString()  , new Date()   , setTime.getText().toString()  , url.getText().toString() ));
                    }

                }).start();

                myRoomDatabase.MeetingDao()
                        .getMeetingCount().observe(
                        AddEventActivity.this, new Observer<Long>() {
                            @Override
                            public void onChanged(Long number) {

                            }
                        });

                myRoomDatabase.MeetingDao()
                        .getAll().observe(
                        AddEventActivity.this, new Observer<List<Meeting>>() {
                            @Override
                            public void onChanged(List<Meeting> meetings) {
                                String names = "";
                                for (int i=0; i<meetings.size(); i++) {
                                    names += meetings.get(i).getName() + "\n";
                                }
                              //  textView.setText(names);
                            }
                        });
            }
        });
    }





    private void initAll() {
        initDatePicker();
        initView();
        initListeners();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datapicker , int year, int month, int dayOfMonth) {
                dayOfMonth =  dayOfMonth + 1  ;
              String date = makeDateString(dayOfMonth , month , year );
              setDate.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return    getMonthFormat(month) +  " " +  dayOfMonth +  " "  +  year ;  
    }

    private String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    private void initListeners() {


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.list, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                worldClockService.invokeCityService(spinner.getSelectedItem().toString());
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        textView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    worldClockService.invokeCityService(spinner.getSelectedItem().toString(), textView.getText().toString());
                    return true;
                }
                return false;
            }
        });


        worldClockService.getCities().observe(
                this,
                new Observer<List<String>>() {
                    @Override
                    public void onChanged(List<String> citiesValue) {
                       // String s = "";
                        copy.clear();
                        for (int i = 0; i < citiesValue.size(); i++) {
                          //  if (i < 10) {
                             //   s += citiesValue.get(i) + "\n";
                            //}


                            copy.add(citiesValue.get(i).substring(citiesValue.get(i).lastIndexOf("/") +  1) );
                        }



                        final ListView ListView = (ListView) findViewById(R.id.listView);
                        final List<String> cities_list = new ArrayList<String>(copy);
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                                (getApplicationContext(), android.R.layout.simple_list_item_1, cities_list);

                        // DataBind ListView with items from ArrayAdapter
                        ListView.setAdapter(arrayAdapter);
                        arrayAdapter.notifyDataSetChanged();

                    }
                });


        worldClockService.getEasternStandardTime().observe(
                this,
                new Observer<EasternStandardTimeModel>() {
                    @Override
                    public void onChanged(EasternStandardTimeModel easternStandardTimeModel) {

                        long unixSeconds = easternStandardTimeModel.getUnixtime();
                        Date date = new java.util.Date(unixSeconds * 1000L) ;
                        SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                        String formattedDate = sdf.format(date);
                        checkTime.setText(formattedDate);

                        //checkTime.setText(easternStandardTimeModel.getDatetime());
                    }
                });


        };


    private void initView() {
        spinner=  findViewById(R.id.spinner);
        textView = findViewById(R.id.cities) ;
        checkTime= findViewById(R.id.check) ;
        setDate = findViewById(R.id.setDate) ;
        saveEvent = findViewById(R.id.save);
    }

    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void openDatePicker(View view) {
        datePickerDialog.show() ;

    }
}