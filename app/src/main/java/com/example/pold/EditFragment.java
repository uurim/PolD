package com.example.pold;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
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

    private static final String FRAME_COLOR = "color";
    private static final String MOOD_CODE = "position";

    private int color;
    private int position;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(int color, int position) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putInt(FRAME_COLOR, color);
        args.putInt(MOOD_CODE, position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(FRAME_COLOR);
            position = getArguments().getInt(MOOD_CODE);
        }
    }

    View v;

    // DB 헬퍼
    DiaryDBHelper dbHelper;
    SQLiteDatabase sqlDB;

    // 필요한 변수 모음
    FrameLayout front, back;
    TextView txtDate;
    EditText editTitle, editDiary;
    ImageView btnFrontFlip, btnBackFlip, btnBack, btnCheck, imgDiary;

    // 캘린더 객체 생성
    Calendar cal = Calendar.getInstance();

    private static final int PICK_IMAGE = 100;
    Uri imageUri;

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
        v = inflater.inflate(R.layout.fragment_edit, container, false);

        // frame
        front = v.findViewById(R.id.front_card);
        back = v.findViewById(R.id.back_card);

        // color 설정
        front.setBackgroundColor(color);
        back.setBackgroundColor(color);

        // flip
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

        // 이미지뷰 갤러리에서 가져오기
        imgDiary = (ImageView) v.findViewById(R.id.iconImg);
        imgDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
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
                        editTitle.getText().toString() + "', " +
                        cal.get(Calendar.YEAR) + ", " +
                        cal.get(Calendar.MONTH) + ", " +
                        cal.get(Calendar.DAY_OF_MONTH) + ", '" +
                        editDiary.getText().toString() + "', '" +
                        imageUri + "', " + position + ");");
                sqlDB.close();
                Toast.makeText(getContext(), "입력됨", Toast.LENGTH_SHORT).show();

                // 폴라로이드 리스트로 이동
                onBackPressed();
            }
        });

//        // 지울 거 : 개발중 DB 초기화 버튼!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        ImageView justRemove = v.findViewById(R.id.iconImg);
//        justRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sqlDB = dbHelper.getWritableDatabase();
//                dbHelper.onUpgrade(sqlDB, 1, 2);
//                sqlDB.close();
//                Toast.makeText(getContext(), "초기화됨", Toast.LENGTH_SHORT).show();
//            }
//        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imgDiary.setImageURI(imageUri);
        }
    }

    // 뒤로가기 : 폴라로이드 리스트로 이동
    @Override
    public void onBackPressed() {
        ((MainActivity)getActivity()).replaceFragment(CalFragment.newInstance());
    }

}