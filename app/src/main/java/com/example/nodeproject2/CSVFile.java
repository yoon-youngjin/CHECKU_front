package com.example.nodeproject2;


import com.example.nodeproject2.datas.Subject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class CSVFile {
    InputStream inputStream;

    public CSVFile(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public ArrayList<Subject> read(){
        ArrayList<Subject> resultList = new ArrayList();
        Subject sub;
        try( BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(",");
                sub = Subject.builder().main_num(row[0]).major_division(row[1]).subject_num(row[2]).subject_title(row[3]).credit(row[4])
                        .major_time(row[5]).year(row[6]).opening_major(row[7]).professor_name(row[8]).capacity_year(row[9]).capacity_total(row[10])
                        .build();
                resultList.add(sub);

            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }

        return resultList;
    }
}
