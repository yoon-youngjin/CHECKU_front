package com.example.nodeproject2.API;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.nodeproject2.datas.Lecture;

@Database(entities = {Lecture.class},version = 1)
public abstract class LectureDatabase extends RoomDatabase {
    public abstract LectureDao lectureDao();
}
