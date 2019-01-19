package com.myapp.user.google_beveco.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myapp.user.google_beveco.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Item> itemList = new ArrayList<Item>();

    //생성자
    public ListViewAdapter(){

    }

    //Adapter에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴 : 필수 구현
    @Override
    public int getCount() {
        return itemList.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();

        //listview_item layout을 inflate하여 convertView 참조를 획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        //화면에 표시될 View(layout이 inflate된)으로부터 위젯에 대한 참조 획득
        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView address = (TextView)convertView.findViewById(R.id.address);
        TextView tel = (TextView)convertView.findViewById(R.id.tel);

        //Data set(ItemList)에서 position에 위치한 데이터 참조 획득
        Item listViewItem = itemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        title.setText(listViewItem.title());
        address.setText(listViewItem.address());
        tel.setText(listViewItem.tel());

        return convertView;
    }

    //지정한 위치에 있는 데이터 ID 리턴 : 필수구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    //지정된 위치에 있는 데이터 리턴 : 필수구현
    @Override
    public Object getItem(int position){
        return itemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(String givenTitle, String givenAddress, String givenTel) {
        Item item = new Item();

        item.setTitle(givenTitle);
        item.setAddress(givenAddress);
        item.setTel(givenTel);

        itemList.add(item);
    }

    //검색을 위한 함수
    public String getTitle(int position){
        return itemList.get(position).title();
    }

    public String getAddress(int position){
        return itemList.get(position).address();
    }

    public String getTel(int position){
        return itemList.get(position).tel();
    }

}
