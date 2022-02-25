package com.example.nodeproject2.datas;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.google.gson.annotations.SerializedName;
import lombok.*;



@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(tableName = "lecture_table")
@Getter @Setter
public class Lecture {

    // 과목 번호
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "sbj_num")
    private int subject_num;

    // 교과목명
    @SerializedName("name")
    @ColumnInfo(name = "sbj_title")
    private String subject_title;

    // 개설학년
    @SerializedName("grade")
    @ColumnInfo(name = "grade")
    private String year;

    // 교강사
    @SerializedName("professor")
    @ColumnInfo(name = "pro_name")
    private String professor_name;

    // 이수구분
    @SerializedName("type")
    @ColumnInfo(name = "type")
    private String major_division;

//    // 정원(학년)
//    private String capacity_year;

    // 정원(전체)
    @SerializedName("inwon")
    @ColumnInfo(name = "inwon")
    private String capacity_total;

    @ColumnInfo(name = "empty")
    private int emptySize;

    @SerializedName("room")
    @ColumnInfo(name = "time")
    private String room;

    @SerializedName("detail")
    @ColumnInfo(name = "detail")
    private String detail;


}
