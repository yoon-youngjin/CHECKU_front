package com.example.nodeproject2.view;


import android.os.*;
import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager2.widget.ViewPager2;
import com.example.nodeproject2.adapter.FragementAdapter;
import com.example.nodeproject2.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends AppCompatActivity {


    private ActivityMainBinding binding = null;
    private MainFragment mainFragment;
    private ListFragment listFragment;
    private LiberalArtsFragment liberalArtsFragment;
    private ViewPager2 viewPager;
    private FragementAdapter viewPagerAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
//        init();
        createFragment();
        createViewpager();
        settingTabLayout();
        new TabLayoutMediator(binding.tablayoutControl, viewPager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();
        setContentView(binding.getRoot());

    }

    //    //fragment 생성
    public void createFragment() {
        listFragment = new ListFragment();
        mainFragment = new MainFragment();
        liberalArtsFragment = new LiberalArtsFragment();
    }

    //
    //viewpager 및 어댑터 생성
    public void createViewpager() {
        viewPager = binding.viewPager;
        viewPagerAdapter = new FragementAdapter(getSupportFragmentManager(), getLifecycle());
        viewPagerAdapter.addFragment(listFragment);
        viewPagerAdapter.addFragment(mainFragment);
        viewPagerAdapter.addFragment(liberalArtsFragment);


        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setUserInputEnabled(false);//터치 스크롤 막음
    }
    //tablayout - viewpager 연결
    public void settingTabLayout() {
        binding.tablayoutControl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();

                switch (pos) {
                    case 0:
                        viewPager.setCurrentItem(0);
                        break;
                    case 1:
                        viewPager.setCurrentItem(1);
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


}