package com.example.nodeproject2.view;


import android.content.Intent;
import android.os.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nodeproject2.API.viewmodel.LectureViewModel;
import com.example.nodeproject2.CSVFile;
import com.example.nodeproject2.R;
import com.example.nodeproject2.service.MyService;
import com.example.nodeproject2.adapter.MainAdatper;
import com.example.nodeproject2.databinding.ActivityMainBinding;
import com.example.nodeproject2.datas.Lecture;


import java.io.*;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding = null;
    private MainFragment mainFragment = new MainFragment();
    private ListFragment listFragment = new ListFragment();
    private boolean state_check = true;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.return_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if (state_check) {
            ft.replace(R.id.main_frame, listFragment).commit();
            state_check = false;
        } else {
            ft.replace(R.id.main_frame, mainFragment).commit();
            state_check = true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.main_frame, mainFragment).commit();
    }

}