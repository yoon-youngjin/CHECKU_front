package com.example.nodeproject2.config;

import android.content.Context;
import androidx.room.Room;
import com.example.nodeproject2.API.Lecture.LectureDao;
import com.example.nodeproject2.API.Lecture.LectureDatabase;
import com.example.nodeproject2.datas.Lecture;

import java.util.ArrayList;

public class BuildLecturDatabase {
    private Context context;
    private String dbName;
    private LectureDatabase db;
    private LectureDao lectureDao;


    public BuildLecturDatabase(Context context, String dbName) {
        this.context = context;
        this.dbName = dbName;
        db = Room.databaseBuilder(this.context, LectureDatabase.class, this.dbName)
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries().build();
        lectureDao = db.lectureDao();
    }

    public ArrayList<Lecture> getLecturesFromDB() {

        return (ArrayList<Lecture>) lectureDao.getLectureAll();

    }

    public void updateLecture(Lecture lecture) {
        this.lectureDao.setUpdateLecture(lecture);
    }

    public void setInsertLecture(Lecture lecture) {
        this.lectureDao.setInsertLecture(lecture);
    }

    public void setDeleteLecture(Lecture lecture) {
        this.lectureDao.setDeleteLecture(lecture);
    }


}
