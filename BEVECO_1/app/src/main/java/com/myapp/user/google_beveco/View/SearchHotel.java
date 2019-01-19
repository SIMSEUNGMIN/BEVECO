package com.myapp.user.google_beveco.View;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.myapp.user.google_beveco.Model.Hotel;
import com.myapp.user.google_beveco.Model.HotelAdapter;
import com.myapp.user.google_beveco.Model.HotelList;
import com.myapp.user.google_beveco.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SearchHotel extends AppCompatActivity implements View.OnClickListener{

    private EditText searchHotel;
    private Button searchButton;

    private RecyclerView recyclerView;
    private ArrayList<HotelList> list = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel);

        searchHotel = (EditText)findViewById(R.id.search_hotel);
        searchButton = (Button)findViewById(R.id.search_button);

        recyclerView = (RecyclerView)findViewById(R.id.hotel_recycler_view);

        searchButton.setOnClickListener(this);
        
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.search_button:
                clickSearchButton();
                break;
        }
    }

    private void clickSearchButton(){

        String strData;
        strData = searchHotel.getText().toString();


        Hotel hotel = new Hotel();
        hotel.searchSetting();
        //AsynTask 작동시킴(파싱)
        new Description().execute();


//        new Thread(){
//            public void run(){
//                try {
//                    new Hotel.resultSearch();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//
    }

    private class Description extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            //진행다이얼로그 시작
            progressDialog = new ProgressDialog(SearchHotel.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시 기다려 주세요.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try{
                Document doc = Jsoup.connect("http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchStay").get();

                //필요한 기능만 꼬집어서 지정
                Elements hotelElements = doc.select("a.collapse");

                System.out.println("size : " + hotelElements.size());
                        //.select("collapsible");

                //목록이 몇개인지 확인
                int numHotelElements = hotelElements.size();

                for(Element hotel : hotelElements){

                    //item 안에서 다시 원하는 데이터를 추출해낸다.
                    //숙소 이름
                    String hotelTitle = hotel.select("title").text();
                    //숙소 주소
                    String hotelAddress = hotel.select("addr1").text();
                    //숙소 전화번호
                    String hotelTel = hotel.select("tel").text();

                    list.add(new HotelList(hotelTitle, hotelAddress, hotelTel));
                }

                //debug용
                for(int i = 0; i < numHotelElements; i++){
                    System.out.println("debug : " + "title : " + list.iterator().next().title());
                }
            }catch(IOException e){
                System.out.println("오류");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result){

            //ArrayList를 인자로 해서 어답터와 연결한다.
            HotelAdapter hotelAdapter = new HotelAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(hotelAdapter);

            progressDialog.dismiss();
        }
    }

}
