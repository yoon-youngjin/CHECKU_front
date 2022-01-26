package com.example.nodeproject2.datas;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class Subject {
    // 학수번호
    private String main_num;
    // 이수구분
    private String major_division;
    // 과목 번호
    private String subject_num;
    // 교과목명
    private String subject_title;
    // 학점
    private String credit;
    // 시간
    private String major_time;
    // 개설학년
    private String year;
    // 개설학과
    private String opening_major;
    // 교강사
    private String professor_name;
    // 정원(학년)
    private String capacity_year;
    // 정원(전체)
    private String capacity_total;



}
