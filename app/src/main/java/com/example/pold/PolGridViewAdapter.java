package com.example.pold;

import android.content.Context;
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

        // 이제 아이템이 존재하는 텍스트뷰 객체들을 view 객체에서 찾아 가져온다
        FrameLayout polItemFrame = (FrameLayout) view.findViewById(R.id.polItem_frame);
        ImageView polItemImg = (ImageView) view.findViewById(R.id.polItem_img);
        TextView polItemTitle = (TextView) view.findViewById(R.id.polItem_tvTitle);

        // 현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list 배열에서 객체를 가져온다
        Diary listData = polList.get(i);

        // 가져온 객체안에 있는 글자들을 각 뷰에 적용한다
        polItemTitle.setText(listData.getTitle());

        // 클릭시
        final int pos = i;
        polItemFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)context).replaceFragment(DetailFragment.newInstance());
                Toast.makeText(context.getApplicationContext(), pos + "클릭함~", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    //ArrayList로 선언된 list 변수에 목록을 채워주기 위함 다른방시으로 구현해도 됨
    public void addItemToPolGrid(String title, int mood){
        Diary listdata = new Diary();

        listdata.setTitle(title);
        listdata.setMood(mood);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        polList.add(listdata);
    }
}
