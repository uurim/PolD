package com.example.pold;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.lukedeighton.wheelview.adapter.WheelArrayAdapter;

import android.widget.EditText;

import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    CalFragment calFragment;
    PolFragment polFragment;
    DetailFragment detailFragment;
    EditFragment editFragment;
    ChartFragment chartFragment;
    SettingFragment settingFragment;

    WheelView wheelView;
    int[] images = {R.drawable.mood1, R.drawable.mood2, R.drawable.mood3, R.drawable.mood4, R.drawable.mood5,
            R.drawable.mood6, R.drawable.mood7, R.drawable.mood8, R.drawable.mood9, R.drawable.mood10};
    int size = 10;

    NavigationBarView navigationBarView;
    Menu menu;

    EditText editTitle;
    EditText editDiary;
    FrameLayout front;
    FrameLayout back;


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

        // EditText
        editTitle = findViewById(R.id.editTitle);
        editDiary = findViewById(R.id.editDiary);

        // FrameLayout
        front = findViewById(R.id.front_card);
        back = findViewById(R.id.back_card);

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
                        if(wheelView.getVisibility()==View.GONE){
                            wheelView.setVisibility(View.VISIBLE);
                        } else {
                            wheelView.setVisibility(View.GONE);
                        }
                        menu.findItem(R.id.polMenu).setIcon(R.drawable.pol_icon);
                        menu.findItem(R.id.calMenu).setIcon(R.drawable.calendar_icon);
                        menu.findItem(R.id.chartMenu).setIcon(R.drawable.chart_icon);
                        menu.findItem(R.id.setMenu).setIcon(R.drawable.setting_icon);
                        //getSupportFragmentManager().beginTransaction().replace(R.id.containers, editFragment.newInstance()).commitNow();
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

        // 써클 메뉴
        wheelView = (WheelView) findViewById(R.id.wheel_view);
        wheelView.setWheelItemCount(size);

        Drawable[] drawables = new Drawable[size];

        for(int i = 0 ; i < size ; i++){
            drawables[i] = getDrawable(images[i]);
        }

        // 메뉴에 아이콘 부착
        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                Drawable drawable = drawables[position];
                return drawable;
            }

            @Override
            public int getCount() {
                return size;
            }
        });

        // 아이콘 클릭 시
        wheelView.setOnWheelItemClickListener(new WheelView.OnWheelItemClickListener() {
            @Override
            public void onWheelItemClick(WheelView parent, int position, boolean isSelected) {
                int color = 0;
                switch(position){
                    case 0 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_red);
                        break;
                    case 1 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_orange);
                        break;
                    case 2 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_realorange);
                        break;
                    case 3 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_lemon);
                        break;
                    case 4 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_green);
                        break;
                    case 5 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_blue);
                        break;
                    case 6 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_purple);
                        break;
                    case 7 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_pink);
                        break;
                    case 8 :
                        color = getApplicationContext().getResources().getColor(R.color.white);
                        break;
                    case 9 :
                        color = getApplicationContext().getResources().getColor(R.color.pol_brown);
                        break;
                }
                wheelView.setVisibility(View.GONE);
                getSupportFragmentManager().beginTransaction().replace(R.id.containers, editFragment.newInstance(color, position)).commitNow();
            }
        });
    }

    // 뒤로가기
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
        // menu.findItem(R.id.polMenu).setIcon(R.drawable.selected_pol_icon);
    }

}