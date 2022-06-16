package com.example.pold;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditFragment extends Fragment  implements onBackPressedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment EditFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFragment newInstance() {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // DB 헬퍼
    DiaryDBHelper dbHelper;
    SQLiteDatabase sqlDB;

    // 필요한 변수 모음
    FrameLayout front, back;
    TextView txtDate;
    EditText editTitle, editDiary;
    ImageView btnFrontFlip, btnBackFlip, btnBack, btnCheck;

    // 캘린더 객체 생성
    Calendar cal = Calendar.getInstance();

    // 데이트피커다이얼로그 생성
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            txtDate = getView().findViewById(R.id.txtDate);
            txtDate.setText(String.format("%d년 %d월 %d일", year, month + 1, dayOfMonth));
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);

        // flip
        front = v.findViewById(R.id.front_card);
        back = v.findViewById(R.id.back_card);
        btnFrontFlip = v.findViewById(R.id.btnFrontFlip);
        btnBackFlip = v.findViewById(R.id.btnBackFlip);

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

        // 날짜를 출력하는 텍스트뷰에 오늘 날짜 설정
        txtDate = v.findViewById(R.id.txtDate);
        txtDate.setText(cal.get(Calendar.YEAR) +"년 "+ (cal.get(Calendar.MONTH) + 1) +"월 "+ cal.get(Calendar.DATE) + "일");
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), myDatePicker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 뒤로가기 버튼
        btnBack = v.findViewById(R.id.iconCancel);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder backDlg = new AlertDialog.Builder(getContext());
                backDlg.setMessage("작성된 내용이 저장되지 않습니다.\n뒤로 가시겠습니까?");
                backDlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onBackPressed();
                    }
                });
                backDlg.setNegativeButton("취소", null);
                backDlg.show();
            }
        });

        // DB 입력
        dbHelper = new DiaryDBHelper(getContext());

        editTitle = v.findViewById(R.id.editTitle);
        editDiary = v.findViewById(R.id.editDiary);

        btnCheck = v.findViewById(R.id.iconCheck);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = dbHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO diary VALUES (" +
                        "NULL, '" +
                        editTitle.getText().toString() + "', '" +
                        txtDate.getText().toString() + "', '" +
                        editDiary.getText().toString() + "', " +
                        "'uri', " + 1 + ");");
                sqlDB.close();
                Toast.makeText(getContext(), "입력됨", Toast.LENGTH_SHORT).show();

                // 폴라로이드 리스트로 이동
                onBackPressed();
            }
        });

        // 지울 거 : 개발중 DB 초기화 버튼
        ImageView justRemove = v.findViewById(R.id.iconImg);
        justRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = dbHelper.getWritableDatabase();
                dbHelper.onUpgrade(sqlDB, 1, 2);
                sqlDB.close();
                Toast.makeText(getContext(), "초기화됨", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    // 뒤로가기 : 폴라로이드 리스트로 이동
    @Override
    public void onBackPressed() {
        ((MainActivity)getActivity()).replaceFragment(PolFragment.newInstance());
    }

}