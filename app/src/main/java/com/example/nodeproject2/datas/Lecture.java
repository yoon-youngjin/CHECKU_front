package com.example.nodeproject2.datas;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@Entity
public class Lecture {

    // 학수번호
    private String main_num;
    // 과목 번호
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int subject_num;
    // 교과목명
    @SerializedName("name")
    private String subject_title;
    // 학점
    @SerializedName("grade")
    private String credit;
    // 시간
    private String major_time;
    // 개설학년
    private String year;
    // 개설학과
    private String opening_major;
    // 교강사
    @SerializedName("professor")
    private String professor_name;
    // 이수구분
    private String major_division;

    // 정원(학년)
    private String capacity_year;
    // 정원(전체)
    @SerializedName("inwon")
    private String capacity_total;

    //    @SerializedName("room")
//    private String room;
//
//
//    @SerializedName("detail")


//    public void setRoom(String room) {
//        this.room = room;
//    }
//
//
////
//    public String getRoom() {
//        return room;
//    }
//
//
//    public String getDetail() {
//        return detail;
//    }
//

//    private String detail;

//    public void setDetail(String detail) {
//        this.detail = detail;
//    }
    public Lecture() {

    }


    public String getMain_num() {
        return main_num;
    }

    public String getMajor_division() {
        return major_division;
    }

    public int getSubject_num() {
        return subject_num;
    }

    public String getSubject_title() {
        return subject_title;
    }

    public String getCredit() {
        return credit;
    }

    public void setMain_num(String main_num) {
        this.main_num = main_num;
    }

    public void setMajor_division(String major_division) {
        this.major_division = major_division;
    }

    public void setSubject_num(int subject_num) {
        this.subject_num = subject_num;
    }

    public void setSubject_title(String subject_title) {
        this.subject_title = subject_title;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public void setMajor_time(String major_time) {
        this.major_time = major_time;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setOpening_major(String opening_major) {
        this.opening_major = opening_major;
    }

    public void setProfessor_name(String professor_name) {
        this.professor_name = professor_name;
    }

    public void setCapacity_year(String capacity_year) {
        this.capacity_year = capacity_year;
    }

    public void setCapacity_total(String capacity_total) {
        this.capacity_total = capacity_total;
    }

    public String getMajor_time() {
        return major_time;
    }

    public String getYear() {
        return year;
    }

    public String getOpening_major() {
        return opening_major;
    }

    public String getProfessor_name() {
        return professor_name;
    }

    public String getCapacity_year() {
        return capacity_year;
    }

    public String getCapacity_total() {
        return capacity_total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecture)) return false;
        Lecture lecture = (Lecture) o;
        return subject_num == lecture.subject_num;
    }




}
