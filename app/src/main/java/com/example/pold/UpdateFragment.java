package com.example.pold;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UpdateFragment extends Fragment {

    private static final String MOOD_CODE = "mcode";

    private int mcode;

    public UpdateFragment() {
        // Required empty public constructor
    }

    public static UpdateFragment newInstance(int mcode) {
        UpdateFragment fragment = new UpdateFragment();
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

    ImageView btnFrontFlip, btnBackFlip, btnBack, btnCheck, showDiaryImg;
    EditText updateTitle, updateDiary;
    TextView updateDate;
    int year;
    int month;
    int day;
    String title;
    String contents;
    int mood, color, code;
    String imgName;

    FrameLayout front, back;

    private static final int PICK_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_update, container, false);

        // DB 접근
        dbHelper = new DiaryDBHelper(getActivity().getApplicationContext());

        // 이미지뷰 가져오기
        showDiaryImg = v.findViewById(R.id.imgView);

        // 에디트텍스트 가져오기
        updateTitle = v.findViewById(R.id.updateTitle);
        updateDiary = v.findViewById(R.id.updateDiary);
        updateDate = v.findViewById(R.id.updateDate);

        // 프레임 가져오기
        front = v.findViewById(R.id.front_card);
        back = v.findViewById(R.id.back_card);

        // 플립 아이콘 가져오기
        btnFrontFlip = v.findViewById(R.id.btnFrontFlip);
        btnBackFlip = v.findViewById(R.id.btnBackFlip);

        // 취소, 저장 버튼 가져오기
        btnBack = v.findViewById(R.id.iconCancel);
        btnCheck = v.findViewById(R.id.iconCheck);

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


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragment.newInstance(mcode);
            }
        });

        // select
        sqlDB = dbHelper.getWritableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT year, month, day, title, contents, mood, code, imgName FROM diary WHERE code ='"+ mcode + "';", null);

        // 변수에 담기
        cursor.moveToPosition(0);
        year = cursor.getInt(0);
        month = cursor.getInt(1);
        day = cursor.getInt(2);
        title = cursor.getString(3);
        contents = cursor.getString(4);
        mood = cursor.getInt(5);
        code = cursor.getInt(6);
        imgName = cursor.getString(7);


        // 조회된 내용 적용
        updateDate.setText(year + "년 "+ (month + 1) +"월 "+ day + "일");
        updateTitle.setText(title);
        updateDiary.setText(contents);

        // 사진 적용
        try {
            String imgpath = getContext().getCacheDir() + "/" + imgName;
            // 내부 저장소에 저장되어 있는 이미지 경로
            Bitmap bm = BitmapFactory.decodeFile(imgpath);
            showDiaryImg.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
            // Toast.makeText(getContext().getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Toast.makeText(getContext().getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
            // Log.e("PDerror", e + "");
        }


        // 무드에 따라 프레임 색 변경
        color = ((MainActivity)getActivity()).changeMoodColor(mood);
        front.setBackgroundColor(color);
        back.setBackgroundColor(color);

        // 캘린더 객체 생성
        Calendar cal = Calendar.getInstance();

        // 데이트피커다이얼로그 생성
        DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                updateDate.setText(String.format("%d년 %d월 %d일", y, m + 1, d));
                year = y;
                month = m;
                day = d;
            }
        };

        updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), myDatePicker, year, month, day).show();
            }
        });

        showDiaryImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery,PICK_IMAGE);
            }
        });


        cursor.close();
        sqlDB.close();

        // 저장 버튼 클릭
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = dbHelper.getWritableDatabase();
                sqlDB.execSQL("UPDATE diary SET title='" + updateTitle.getText()
                        + "', contents='" + updateDiary.getText()
                        + "', year=" + year
                        + ", month=" + month
                        + ", day=" + day
                        + ", imgName='" + imgName
                        + "' WHERE code = " + mcode);
                sqlDB.close();

                ((MainActivity)getActivity()).replaceFragment(DetailFragment.newInstance(mcode));
            }
        });

        return v;
    }

}