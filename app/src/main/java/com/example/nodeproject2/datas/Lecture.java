package com.example.nodeproject2.datas;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.example.nodeproject2.adapter.LiberalArtsAdapter;
import com.example.nodeproject2.adapter.MajorAdatper;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.util.Objects;


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

    public static Lecture createLectureFromMajor(MajorAdatper.ViewHolder holder) {
        Lecture lecture = Lecture.builder()
                .subject_num(Integer.parseInt(holder.sub_num.getText().toString()))
                .subject_title(holder.sub_title.getText().toString())
                .professor_name(holder.pro_name.getText().toString())
                .capacity_total(holder.capacity_total.getText().toString())
                .year(holder.grade.getText().toString().substring(0,1))
                .emptySize(Integer.parseInt(holder.empty.getText().toString()))
                .major_division(holder.type.getText().toString())
                .room(holder.room.getText().toString())
                .detail(holder.detail.getText().toString())
                .build();
        return lecture;
    }

    public static Lecture createLectureFromArts(LiberalArtsAdapter.ViewHolder holder) {

        String pro_name = holder.pro_name.getText().toString();
        String year = holder.year.getText().toString();

        Lecture lecture = Lecture.builder()
                .subject_num(Integer.parseInt(holder.sub_num.getText().toString()))
                .subject_title(holder.sub_title.getText().toString())
                .capacity_total(holder.capacity_total.getText().toString())
                .major_division(holder.type.getText().toString())
                .emptySize(Integer.parseInt(holder.empty.getText().toString()))
                .room(holder.room.getText().toString())
                .detail(holder.detail.getText().toString())
                .build();

        if (year.equals("전체")) {
            lecture.setYear("9");
        } else {
            lecture.setYear(year.substring(0, 1));
        }
        if (pro_name.equals("")) {
            lecture.setProfessor_name(pro_name);
        } else {
            lecture.setProfessor_name(pro_name.trim());
        }
        return lecture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lecture)) return false;
        Lecture lecture = (Lecture) o;
        return subject_num == lecture.subject_num;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject_num);
    }
}
