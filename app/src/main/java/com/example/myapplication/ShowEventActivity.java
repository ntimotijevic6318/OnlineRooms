
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.database.MyRoomDatabase;
import com.example.myapplication.entity.Meeting;
import com.example.myapplication.recycler.adapter.ItemAdapter;
import com.example.myapplication.recycler.differ.ItemDiffItemCallback;


import java.util.List;

public class ShowEventActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    Button search ;
    MyRoomDatabase myRoomDatabase = null;
    EditText search_input ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);


        initAll();

    }

    private void initAll() {
        initView();
        initListeners();
        roomDatabase();
        initRecycler();
    }

    private void initListeners() {
        search.setOnClickListener(v->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Meeting> meetings =  myRoomDatabase.MeetingDao().search(search_input.getText().toString());
                    itemAdapter.submitList(meetings);
                }
            }).start();

        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.recycler) ;
        search = findViewById(R.id.search) ;
        search_input  = findViewById(R.id.search_input) ;

    }

    private void initRecycler() {
        itemAdapter = new ItemAdapter(new ItemDiffItemCallback() , itemToDelete->{
            new Thread(new Runnable() {
                @Override
                public void run() {
                    myRoomDatabase.MeetingDao().delete(itemToDelete.getId()) ;
                }

            }).start();
            return null;

        } ,  itemToClick-> {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = "https://zoom.us/j/" + itemToClick.getUrl()  ;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                }

            }).start();


            return null;

        }) ;


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
    }


    private void roomDatabase() {

        myRoomDatabase = MyRoomDatabase.getDatabase(this);
        myRoomDatabase.MeetingDao()
                        .getAll().observe(
                        ShowEventActivity.this, new Observer<List<Meeting>>() {
                            @Override
                            public void onChanged(List<Meeting> meetings) {

                                itemAdapter.submitList(meetings);
                                //String names = "";
                              //  for (int i=0; i<meetings.size(); i++) {
                                 ///   names += meetings.get(i).getName() + "\n";
                              //  }
                                //  textView.setText(names);
                            }
                        });
            }
}