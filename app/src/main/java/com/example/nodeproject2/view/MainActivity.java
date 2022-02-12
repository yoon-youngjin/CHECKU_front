package com.example.nodeproject2.view;


import android.os.*;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import com.example.nodeproject2.R;
import com.example.nodeproject2.databinding.ActivityMainBinding;





public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding = null;
    private MainFragment mainFragment = new MainFragment();
    private ListFragment listFragment = new ListFragment();
    private boolean state_check = true;

    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frame, listFragment).commit();


        binding.fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (state_check) {
                    ft.replace(R.id.main_frame, mainFragment).commit();
                    state_check = false;
                } else {
                    ft.replace(R.id.main_frame, listFragment).commit();
                    state_check = true;
                }

            }
        });
    }

}