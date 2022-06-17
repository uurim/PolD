package com.example.pold;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalFragment newInstance() {
        CalFragment fragment = new CalFragment();
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

    // 필요한 변수 모음
    View v;

    GridView calGridView;

    DiaryDBHelper dbHelper;
    SQLiteDatabase sqlDB;

    TextView txtDateCal;

    // 캘린더 객체 생성
    Calendar cal = Calendar.getInstance();

    // 데이트피커다이얼로그 생성
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, month);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            txtDateCal = getView().findViewById(R.id.txtDateCal);
            txtDateCal.setText(String.format("%d년 %d월", year, month + 1));

            showCalGrid();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_cal, container, false);

        // 날짜를 출력하는 텍스트뷰에 오늘 날짜 설정
        txtDateCal = v.findViewById(R.id.txtDateCal);
        txtDateCal.setText(cal.get(Calendar.YEAR) +"년 "+ (cal.get(Calendar.MONTH) + 1) +"월");
        txtDateCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getContext(), myDatePicker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // 그리드 뷰
        calGridView = v.findViewById(R.id.calGridView);
        showCalGrid();

        return v;
    }

    void showCalGrid() {
        dbHelper = new DiaryDBHelper(getActivity().getApplicationContext());
        sqlDB = dbHelper.getWritableDatabase();

        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT mood, year, month, day FROM diary WHERE year ==" + cal.get(Calendar.YEAR) + " AND month ==" + cal.get(Calendar.MONTH) + " ORDER BY day;", null);

        CalGridViewAdapter adapter = new CalGridViewAdapter();
        calGridView.setAdapter(adapter);

        while (cursor.moveToNext()) {
            adapter.addItemToCalGrid(cursor.getInt(0));
        }

        cursor.close();
        sqlDB.close();
    }
}