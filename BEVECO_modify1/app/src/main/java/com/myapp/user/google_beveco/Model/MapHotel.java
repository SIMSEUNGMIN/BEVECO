package com.myapp.user.google_beveco.Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.myapp.user.google_beveco.R;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MapHotel extends Activity {

    //초기 어댑터
    private ListViewAdapter mapHotelAdapter;

    private int rowTotal;

    //검색 결과용
    private ListView map_search_list_hotel;
    private ListViewAdapter mapSearchHotelAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_hotel);

        mapHotelAdapter = new ListViewAdapter();

        new MapHotelExcel().execute();
        new MapHotelSearch().execute();

    }

    private class MapHotelSearch extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(MapHotel.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려 주십시오.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            search();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            map_search_list_hotel.setAdapter(mapSearchHotelAdapter);
            progressDialog.dismiss();

        }

    }

    private class MapHotelExcel extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(MapHotel.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("데이터를 불러 오고 있습니다.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            getExcel();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            progressDialog.dismiss();
        }

    }

    private void search() {

        map_search_list_hotel = (ListView) findViewById(R.id.map_hotel);
        mapSearchHotelAdapter = new ListViewAdapter();

        String[] strData = filtering(MapsActivity.markerAddress);
        String[] address;

        for(int i = 0; i < rowTotal - 1; i++){
            address = addressFiltering(mapHotelAdapter.getAddress(i));

            switch(address.length){
                case 2:
                    break;
                case 3 :
                    if(address[2].contains(strData[0])){
                        mapSearchHotelAdapter.addItem(mapHotelAdapter.getTitle(i), mapHotelAdapter.getAddress(i), mapHotelAdapter.getTel(i));
                    }
                    break;
                case 4 :
                    if(address[2].contains(strData[0])
                            && address[3].contains(strData[1])){
                        mapSearchHotelAdapter.addItem(mapHotelAdapter.getTitle(i), mapHotelAdapter.getAddress(i), mapHotelAdapter.getTel(i));
                    }
                    break;
                default :
                    if(address[2].contains(strData[0])
                            && address[3].contains(strData[1])
                            && address[4].contains(strData[2])){
                        mapSearchHotelAdapter.addItem(mapHotelAdapter.getTitle(i), mapHotelAdapter.getAddress(i), mapHotelAdapter.getTel(i));
                    }
                    break;
            }
        }

        map_search_list_hotel.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=";
                String title = mapSearchHotelAdapter.getTitle(position);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + title));
                startActivity(intent);
            }
        });

    }

    private void getExcel() {

        Workbook wb = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("hotel.xls");
            wb = Workbook.getWorkbook(is);

            if (wb != null) {
                Sheet sheet = wb.getSheet(0);   // 시트 불러오기
                if (sheet != null) {
                    int colTotal = sheet.getColumns();// 전체 컬럼
                    //디버그용
                    //System.out.println("colTotal : " + colTotal);
                    int rowIndexStart = 1;                  // row 인덱스 시작
                    rowTotal = sheet.getRows();
                    //디버그용
                    //System.out.println("rowTotal : " + rowTotal);

                    for (int row = rowIndexStart; row < rowTotal; row++) {

                        String title = null;
                        String address = null;
                        String tel = null;

                        for (int col = 0; col < colTotal; col++) {
                            switch(col){
                                case 0:
                                    title = sheet.getCell(col, row).getContents();
                                    break;
                                case 1:
                                    address = "주소 : " + sheet.getCell(col, row).getContents();
                                    break;
                                case 2:
                                    tel = "Tel : " + sheet.getCell(col, row).getContents();
                                    break;
                            }
                        }
                        mapHotelAdapter.addItem(title, address, tel);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        }finally{
            wb.close();
        }
    }

    //글자를 필터링 하여 도, 구, 시를 추출
    private String[] filtering(String str){
        String[] strings = str.split("\n");
        String[] filitering = strings[1].split(" ");
        return filitering;
    }

    private String[] addressFiltering(String str){
        String[] strings = str.split(" ");
        return strings;
    }

}
