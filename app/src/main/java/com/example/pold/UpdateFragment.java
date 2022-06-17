package com.example.pold;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;

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
    Uri uri;

    FrameLayout front, back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_update, container, false);

        // DB 접근
        dbHelper = new DiaryDBHelper(getActivity().getApplicationContext());

        // 이미지뷰 가져오기
        showDiaryImg = v.findViewById(R.id.iconImg);

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
                AlertDialog.Builder backDlg = new AlertDialog.Builder(getContext());
                backDlg.setMessage("수정된 내용이 저장되지 않습니다.\n뒤로 가시겠습니까?");
                backDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((MainActivity)getActivity()).replaceFragment(DetailFragment.newInstance(mcode));
                    }
                });
                backDlg.setNegativeButton("취소", null);
                backDlg.show();
            }
        });

        // select
        sqlDB = dbHelper.getReadableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT year, month, day, title, contents, mood, code, uri FROM diary WHERE code ='"+ mcode + "';", null);

        // 변수에 담기
        cursor.moveToPosition(0);
        year = cursor.getInt(0);
        month = cursor.getInt(1);
        day = cursor.getInt(2);
        title = cursor.getString(3);
        contents = cursor.getString(4);
        mood = cursor.getInt(5);
        code = cursor.getInt(6);
        uri = Uri.parse(cursor.getString(7));

        cursor.close();
        sqlDB.close();

        // 조회된 내용 적용
        updateDate.setText(year + "년 "+ (month + 1) +"월 "+ day + "일");
        updateTitle.setText(title);
        updateDiary.setText(contents);
        showDiaryImg.setImageURI(uri);

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
                year = y;
                month = m;
                day = d;

                updateDate.setText(String.format("%d년 %d월 %d일", y, m + 1, d));
            }
        };

        updateDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), myDatePicker, year, month, day).show();
            }
        });

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
                        + " WHERE code = " + mcode);
                sqlDB.close();

                ((MainActivity)getActivity()).replaceFragment(DetailFragment.newInstance(mcode));
            }
        });

        return v;
    }


}