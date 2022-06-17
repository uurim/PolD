package com.example.pold;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailFragment extends Fragment  {

    private static final String MOOD_CODE = "mcode";

    private int mcode;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(int mcode) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(MOOD_CODE, mcode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mcode = getArguments().getInt(MOOD_CODE);
        }
    }

    // 필요한 변수 모음
    View v;

    DiaryDBHelper dbHelper;
    SQLiteDatabase sqlDB;

    ImageView btnFrontFlip, btnBackFlip, btnBack, showDiaryImg;
    ImageView btnFrontFlip, btnBackFlip, btnBack, btnEdit, btnDelete;
    TextView detailTitle, detailDiary, detailDate;
    String year;
    int month;
    String day;
    String title;
    String contents;
    int mood, color;
    Uri uri;

    FrameLayout front, back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_detail, container, false);

        // DB 접근
        dbHelper = new DiaryDBHelper(getActivity().getApplicationContext());
        sqlDB = dbHelper.getWritableDatabase();

        // 텍스트뷰 가져오기
        detailTitle = v.findViewById(R.id.detailTitle);
        detailDiary = v.findViewById(R.id.detailDiary);
        detailDate = v.findViewById(R.id.detailDate);

        // 프레임 가져오기
        front = v.findViewById(R.id.front_card);
        back = v.findViewById(R.id.back_card);

        // 플립 아이콘 가져오기
        btnFrontFlip = v.findViewById(R.id.btnFrontFlip);
        btnBackFlip = v.findViewById(R.id.btnBackFlip);

        // 수정 삭제 버튼 가져오기
        btnEdit = v.findViewById(R.id.btnEdit);
        btnDelete = v.findViewById(R.id.btnDelete);

        //flip
        btnFrontFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                front.setVisibility(View.GONE);
                back.setVisibility(View.VISIBLE);
                btnFrontFlip.setVisibility(View.GONE);
                btnBackFlip.setVisibility(View.VISIBLE);
            }
        });

        btnBackFlip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.GONE);
                front.setVisibility(View.VISIBLE);
                btnBackFlip.setVisibility(View.GONE);
                btnFrontFlip.setVisibility(View.VISIBLE);
            }
        });

        btnBack = v.findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(CalFragment.newInstance());
            }
        });

        // select
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT year, month, day, title, contents, mood, code FROM diary WHERE code ='"+ mcode + "';", null);
        cursor = sqlDB.rawQuery("SELECT year, month, day, title, contents, mood, code, uri FROM diary WHERE code ='"+ pos + "';", null);

        cursor.moveToPosition(0);
        // 변수에 담기
        cursor.moveToPosition(0);
        year = cursor.getString(0);
        month = cursor.getInt(1) + 1;
        day = cursor.getString(2);
        title = cursor.getString(3);
        contents = cursor.getString(4);
        mood = cursor.getInt(5);
        uri = Uri.parse(cursor.getString(7));

        // 조회된 내용 적용
        detailDate.setText(year+"년 "+month+"월 "+day+"일");
        detailTitle.setText(title);
        detailDiary.setText(contents);
        showDiaryImg = v.findViewById(R.id.iconImg);
        showDiaryImg.setImageURI(uri);

        // 무드에 따라 프레임 색 변경
        color = ((MainActivity)getActivity()).changeMoodColor(mood);
        front.setBackgroundColor(color);
        back.setBackgroundColor(color);

        // 수정 버튼 클릭
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).replaceFragment(UpdateFragment.newInstance(mcode));
            }
        });

        // 삭제 버튼 클릭
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB.rawQuery("DELETE FROM diary WHERE code ='"+ mcode + "';", null);
            }
        });

        cursor.close();
        sqlDB.close();
        return v;
    }



}