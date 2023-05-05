package com.example.myapplication.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.myapplication.converters.DateConverter;
import com.example.myapplication.dao.MeetingDao;
import com.example.myapplication.entity.Meeting;





@Database(version = 2,
        entities = {
                Meeting.class,
        },
        exportSchema = false
)
@TypeConverters({DateConverter.class})
public abstract class MyRoomDatabase extends RoomDatabase {

    private static MyRoomDatabase singletonInstance;
    public abstract MeetingDao MeetingDao();

    public static MyRoomDatabase getDatabase(final Context context) {
        if (singletonInstance == null) {
            synchronized (MyRoomDatabase.class) {
                if (singletonInstance == null) {
                    singletonInstance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            MyRoomDatabase.class,
                            "my_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(callback)
                            .build();
                }
            }
        }
        return singletonInstance;
    }

    private static RoomDatabase.Callback callback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDatabaseAsync(singletonInstance).execute();
                }
            };

    private static class PopulateDatabaseAsync extends AsyncTask<Void, Void, Void> {

        private final MeetingDao meetingDao;

        PopulateDatabaseAsync(MyRoomDatabase myRoomDatabase) {
            meetingDao = myRoomDatabase.MeetingDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {

            return null;
        }
    }

}