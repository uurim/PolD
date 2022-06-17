package com.example.pold;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PolGridViewAdapter extends BaseAdapter {

    ArrayList<Diary> polList = new ArrayList<Diary>();

    @Override
    public int getCount() {
        return polList.size();
    }

    @Override
    public Object getItem(int i) {
        return polList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final Context context = viewGroup.getContext();

        // 그리드뷰에 아이템이 인플레이트 되어있는지 확인한 후
        // 아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트하고 view 객체에 담는다
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_polgrid, viewGroup, false);
        }

        // 이제 아이템이 존재하는 객체들을 view 객체에서 찾아 가져온다
        FrameLayout polItemFrame = (FrameLayout) view.findViewById(R.id.polItem_frame);
        ImageView polItemImg = (ImageView) view.findViewById(R.id.polItem_img);

        // 현재 포지션에 해당하는 아이템에 적용하기 위해 list 배열에서 객체를 가져온다
        Diary listData = polList.get(i);

        // 가져온 객체안에 있는 데이터들을 각 뷰에 적용한다
        try {
            if (listData.getImgName().equals("null")) {
                Drawable draw = context.getResources().getDrawable(R.drawable.ic_noimg);
                polItemImg.setImageDrawable(draw);
            } else {
                String imgpath = context.getCacheDir() + "/" + listData.getImgName();   // 내부 저장소에 저장되어 있는 이미지 경로
                Bitmap bm = BitmapFactory.decodeFile(imgpath);
                polItemImg.setImageBitmap(bm);   // 내부 저장소에 저장된 이미지를 이미지뷰에 셋
                // Toast.makeText(context.getApplicationContext(), "파일 로드 성공", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // Toast.makeText(context.getApplicationContext(), "파일 로드 실패", Toast.LENGTH_SHORT).show();
        }

        // 클릭 시 디테일프래그먼트로 이동
        final int pos = i;
        polItemFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).replaceFragment(DetailFragment.newInstance(listData.getCode()));
                // Toast.makeText(context.getApplicationContext(), pos + " 클릭함", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // ArrayList로 선언된 list 변수에 목록을 채워주기 위함 다른방식으로 구현해도 됨
    public void addItemToPolGrid(int code, String imgName){
        Diary listdata = new Diary();

        listdata.setCode(code);
        listdata.setImgName(imgName);

        //값들의 조립이 완성된 listdata 객체 한개를 list 배열에 추가
        polList.add(listdata);
    }
}
