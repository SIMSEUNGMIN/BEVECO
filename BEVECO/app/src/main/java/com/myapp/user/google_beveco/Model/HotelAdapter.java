package com.myapp.user.google_beveco.Model;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapp.user.google_beveco.R;

import java.util.ArrayList;



public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.ViewHolder> {

    private ArrayList<HotelList> _hotelLists;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.hotel_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {


        //이름
        viewHolder.hotel_title.setText(String.valueOf(this._hotelLists.get(position)));
        //주소
        viewHolder.hotel_adress.setText(String.valueOf(this._hotelLists.get(position)));
        //전화번호
        viewHolder.hotel_tel.setText(String.valueOf(this._hotelLists.get(position)));
    }

    @Override
    public int getItemCount() {
        return this._hotelLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView hotel_title, hotel_adress, hotel_tel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            hotel_title = (TextView) itemView.findViewById(R.id.hotel_title);
            hotel_adress = (TextView)itemView.findViewById(R.id.hotel_adress);
            hotel_tel = (TextView)itemView.findViewById(R.id.hotel_tel);
        }
    }

    //생성자
    public HotelAdapter(ArrayList<HotelList> list){
        this._hotelLists = list;
    }
}
