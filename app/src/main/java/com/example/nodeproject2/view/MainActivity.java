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


public class  MainActivity extends AppCompatActivity {


    ActivityMainBinding binding = null;
    MainFragment mainFragment;
//    ListFragment listFragment = new ListFragment();
    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.return_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_item:
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_frame, mainFragment).commit();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        super.onCreate(savedInstanceState);
//        setSupportActionBar(binding.toolbar);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowTitleEnabled(false);

        setContentView(binding.getRoot());
        init();
    }
    private void init() {
        mainFragment = new MainFragment();
        ft.replace(R.id.main_frame, mainFragment).commit();
    }





}