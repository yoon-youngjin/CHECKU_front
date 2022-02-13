package com.example.nodeproject2.view;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.nodeproject2.R;


public class SubjectCodeFragment extends Fragment {




    public SubjectCodeFragment() {
        // Required empty public constructor
    }


    public static SubjectCodeFragment newInstance(String param1, String param2) {
        SubjectCodeFragment fragment = new SubjectCodeFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subject_code, container, false);
    }
}