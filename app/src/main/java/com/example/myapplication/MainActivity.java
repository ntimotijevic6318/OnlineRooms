package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.database.MyRoomDatabase;
import com.example.myapplication.entity.Meeting;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button addEvent;
    Button showEvent;
    Button export_to_json;
    Intent intent ;
    Intent intent1 ;
    MyRoomDatabase myRoomDatabase = null;
    FileOutputStream fos  =  null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initAll();
    }




    private void initAll() {
        initView();
        initListeners();
    }

    private void initListeners() {
         intent = new Intent(this , AddEventActivity.class);
         intent1 = new Intent(this , ShowEventActivity.class);


         addEvent.setOnClickListener(v->{
             startActivity(intent);
         });

         showEvent.setOnClickListener(v->{
             startActivity(intent1) ;
         });

         export_to_json.setOnClickListener(v->{

             myRoomDatabase = MyRoomDatabase.getDatabase(this);
             myRoomDatabase.MeetingDao()
                     .getAll().observe(
                     MainActivity.this,
                     new Observer<List<Meeting>>() {
                         @Override
                         public void onChanged(List<Meeting> meetings) {
                             String filename = "my_json_users.txt" ;

                                 String content =  new Gson().toJson(meetings);
                                 writeFileOnInternalStorage(getApplicationContext() ,  filename , content);
                                 //Toast.makeText(getApplicationContext() ,  "Saved to: "  + getFilesDir() + "/"  +  filename, Toast.LENGTH_LONG).show(); ;

                         };
                     }) ;

         });
    }


    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void writeFileOnInternalStorage(Context mcoContext, String sFileName, String sBody){
        File dir = new File(mcoContext.getFilesDir(), "mydir");
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initView() {
       addEvent = findViewById(R.id.add_event);
       showEvent = findViewById(R.id.show_event);
       export_to_json =  findViewById(R.id.export_to_json);
    }

}