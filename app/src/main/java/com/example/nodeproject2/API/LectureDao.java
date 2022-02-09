package com.example.nodeproject2.API;

import androidx.room.*;
import com.example.nodeproject2.datas.Lecture;

import java.util.List;

@Dao
public interface LectureDao {

    @Insert
    void setInsertLecture(Lecture lecture);

    @Update
    void setUpdateLecture(Lecture lecture);

    @Delete
    void setDeleteLecture(Lecture lecture);

    //조회 쿼리
    @Query("SELECT * FROM Lecture")
    List<Lecture> getLectureAll();

}
