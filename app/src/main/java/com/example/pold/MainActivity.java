package com.example.pold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    CalFragment calFragment;
    PolFragment polFragment;
    DetailFragment detailFragment;
    EditFragment editFragment;
    ChartFragment chartFragment;
    SettingFragment settingFragment;

    NavigationBarView navigationBarView;
    Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("감정보기");

        //프래그먼트 생성
        calFragment = new CalFragment();
        polFragment = new PolFragment();
        detailFragment = new DetailFragment();
        editFragment = new EditFragment();
        settingFragment = new SettingFragment();

        //초기 프래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, calFragment).commit();

        //하단바 생성
        navigationBarView = findViewById(R.id.bottom_nav_view);
        menu = navigationBarView.getMenu();

        navigationBarView.setItemIconTintList(null);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //메뉴 선택 프래그먼트 연결
                switch (item.getItemId()){
                    case R.id.calMenu:
                        item.setIcon(R.drawable.selected_cal_icon);
                        menu.findItem(R.id.polMenu).setIcon(R.drawable.pol_icon);
                        menu.findItem(R.id.chartMenu).setIcon(R.drawable.chart_icon);
                        menu.findItem(R.id.setMenu).setIcon(R.drawable.setting_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, calFragment).commit();
                        return true;
                    case R.id.polMenu:
                        item.setIcon(R.drawable.selected_pol_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.chartMenu).setIcon(R.drawable.chart_icon);
                        menu.findItem(R.id.setMenu).setIcon(R.drawable.setting_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, polFragment).commit();
                        return true;
                    case R.id.chartMenu:
                        item.setIcon(R.drawable.selected_chart_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.polMenu).setIcon(R.drawable.pol_icon);
                        menu.findItem(R.id.setMenu).setIcon(R.drawable.setting_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, chartFragment).commit();
                        return true;
                    case R.id.setMenu:
                        item.setIcon(R.drawable.selected_set_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.polMenu).setIcon(R.drawable.pol_icon);
                        menu.findItem(R.id.chartMenu).setIcon(R.drawable.chart_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, settingFragment).commit();
                        return true;

                }
                return false;
            }
        });
    }
}