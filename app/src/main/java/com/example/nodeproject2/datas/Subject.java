package com.example.nodeproject2.datas;

public class Subject {
    private String subject_title;
    private String professor_name;
    private String total_num;
    private String current_num;
    private  String subject_num;

    public String getSubject_title() {
        return subject_title;
    }

    public String getProfessor_name() {
        return professor_name;
    }

    public String getSubject_num() {
        return subject_num;
    }

    public Subject(String subject_title, String professor_name, String total_num, String current_num, String subject_num) {
        this.subject_title = subject_title;
        this.professor_name = professor_name;
        this.total_num = total_num;
        this.current_num = current_num;
        this.subject_num = subject_num;
    }
}
