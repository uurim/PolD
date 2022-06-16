package com.example.pold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

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

        //프래그먼트 생성
        calFragment = new CalFragment();
        polFragment = new PolFragment();
        detailFragment = new DetailFragment();
        editFragment = new EditFragment();
        chartFragment = new ChartFragment();
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
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, calFragment.newInstance()).commitNow();
                        return true;
                    case R.id.polMenu:
                        item.setIcon(R.drawable.selected_pol_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.chartMenu).setIcon(R.drawable.chart_icon);
                        menu.findItem(R.id.setMenu).setIcon(R.drawable.setting_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, polFragment.newInstance()).commitNow();
                        return true;
                    case R.id.selMenu:
                        menu.findItem(R.id.polMenu).setIcon(R.drawable.pol_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.chartMenu).setIcon(R.drawable.chart_icon);
                        menu.findItem(R.id.setMenu).setIcon(R.drawable.setting_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, editFragment.newInstance()).commitNow();
                        return true;
                    case R.id.chartMenu:
                        item.setIcon(R.drawable.selected_chart_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.polMenu).setIcon(R.drawable.pol_icon);
                        menu.findItem(R.id.setMenu).setIcon(R.drawable.setting_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, chartFragment.newInstance()).commitNow();
                        return true;
                    case R.id.setMenu:
                        item.setIcon(R.drawable.selected_set_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.polMenu).setIcon(R.drawable.pol_icon);
                        menu.findItem(R.id.chartMenu).setIcon(R.drawable.chart_icon);
                        getSupportFragmentManager().beginTransaction().replace(R.id.containers, settingFragment.newInstance()).commitNow();
                        return true;

                }
                return false;
            }
        });
    }

    private long lastTimeBackPressed;

    @Override
    public void onBackPressed() {

        //프래그먼트 onBackPressedListener사용
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        for(Fragment fragment : fragmentList){
            if(fragment instanceof onBackPressedListener){
                ((onBackPressedListener)fragment).onBackPressed();
                return;
            }
        }

        //두 번 클릭시 어플 종료
        if(System.currentTimeMillis() - lastTimeBackPressed < 1500){
            finish();
            return;
        }
        lastTimeBackPressed = System.currentTimeMillis();
        Toast.makeText(this,"'뒤로' 버튼을 한 번 더 누르면 종료됩니다.",Toast.LENGTH_SHORT).show();

    }

    // 프래그먼트 이동
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.containers, fragment).commit();
        menu.findItem(R.id.polMenu).setIcon(R.drawable.selected_pol_icon);
    }
}