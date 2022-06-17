package com.example.pold;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    String imgName;

    // 캘린더 객체 생성
    Calendar cal = Calendar.getInstance();

    private static final int PICK_IMAGE = 100;

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
                        imgName + "', "+
                        position + ");");
                sqlDB.close();
                // Toast.makeText(getContext(), "입력됨", Toast.LENGTH_SHORT).show();

                // 폴라로이드 리스트로 이동
                onBackPressed();
            }
        });

//        // 지울 거 : 개발중 한 번에 DB 초기화 버튼!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                ContentResolver resolver = getContext().getContentResolver();
                try {
                    InputStream instream = resolver.openInputStream(uri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    imgDiary.setImageBitmap(imgBitmap);    // 선택한 이미지 이미지뷰에 셋
                    instream.close();   // 스트림 닫아주기
                    Toast.makeText(getContext(), "사진 불러오는 중", Toast.LENGTH_SHORT).show();
                    saveBitmapToJpeg(imgBitmap);    // 내부 저장소에 저장
                    // Toast.makeText(getContext(), "파일 불러오기 성공", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    // Toast.makeText(getContext(), "파일 불러오기 실패", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String dateName(long dateTaken){
        Date date = new Date(dateTaken);
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        return dateFormat.format(date);
    }

    public void saveBitmapToJpeg(Bitmap bitmap) {   // 선택한 이미지 내부 저장소에 저장
        long now = System.currentTimeMillis();
        imgName = dateName(now);
        File tempFile = new File(getContext().getCacheDir(), imgName);   // 파일 경로와 이름 넣기
        try {
            tempFile.createNewFile();   // 자동으로 빈 파일을 생성하기
            FileOutputStream out = new FileOutputStream(tempFile);  // 파일을 쓸 수 있는 스트림을 준비하기
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);   // compress 함수를 사용해 스트림에 비트맵을 저장하기
            out.close();    // 스트림 닫아주기
            // Toast.makeText(getContext().getApplicationContext(), "파일 저장 성공", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // Toast.makeText(getContext().getApplicationContext(), "파일 저장 실패", Toast.LENGTH_SHORT).show();
        }
    }

    // 뒤로가기 : 폴라로이드 리스트로 이동
    @Override
    public void onBackPressed() {
        ((MainActivity)getActivity()).replaceFragment(CalFragment.newInstance());
    }

}