package com.example.pold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationBarView;

import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    CalFragment calFragment;
    PolFragment polFragment;
    DetailFragment detailFragment;
    EditFragment editFragment;
    SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("메인은 감정~");

        //프래그먼트 생성
        calFragment = new CalFragment();
        polFragment = new PolFragment();
        detailFragment = new DetailFragment();
        editFragment = new EditFragment();
        settingFragment = new SettingFragment();

        //초기 프래그먼트 설정
        getSupportFragmentManager().beginTransaction().replace(R.id.containers, calFragment).commit();

        //하단바 생성
        NavigationBarView navigationBarView = findViewById(R.id.bottom_nav_view);
        navigationBarView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //메뉴 선택 프래그먼트 연결
                switch (item.getItemId()){
                    case R.id.calMenu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, calFragment);
                        return true;
                    case R.id.polMenu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, polFragment);
                        return true;
                    case R.id.setMenu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, settingFragment);
                        return true;

                }

                return false;
            }
        });
    }
}