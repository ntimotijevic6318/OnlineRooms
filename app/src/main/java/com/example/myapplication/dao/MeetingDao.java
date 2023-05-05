package com.example.myapplication.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.myapplication.entity.Meeting;

import java.util.List;


@Dao
public interface MeetingDao {

    @Insert
    public long insert(Meeting meeting);

    @Query("DELETE from meeting_table where id = :meeting_id")
    public int delete(long meeting_id) ;


    @Query("SELECT  *  from meeting_table where name like :meeting_name")
    public List<Meeting> search(String meeting_name) ;




    @Query("SELECT * FROM meeting_table")
    public LiveData<List<Meeting>> getAll();


    @Query("SELECT COUNT(*) " +
            "FROM meeting_table")
    public LiveData<Long> getMeetingCount();

}
