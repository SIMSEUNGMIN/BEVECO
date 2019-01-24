package com.myapp.user.google_beveco.View;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.myapp.user.google_beveco.Model.ListViewAdapter;
import com.myapp.user.google_beveco.Model.MapsActivity;
import com.myapp.user.google_beveco.R;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class MapTour extends Activity {

    //초기 어댑터
    private ListViewAdapter mapTourAdapter;

    private int rowTotal;

    //검색 결과용 리스트와 어댑터
    private ListView map_search_list_tour;
    private ListViewAdapter mapSearchTourAdapter;

    //검색 옵션용
    Spinner tourSpinner;
    private ArrayAdapter optionTourAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_tour);

        mapTourAdapter = new ListViewAdapter();

        tourSpinner = (Spinner)findViewById(R.id._tour);
        optionTourAdapter = ArrayAdapter.createFromResource(this, R.array.tour, android.R.layout.simple_spinner_dropdown_item);
        tourSpinner.setAdapter(optionTourAdapter);

        tourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 1 : //시/구 단위
                        new MapTourExcel().execute();
                        new WideMapTourSearch().execute();
                        break;
                    case 2 : //동 단위
                        new MapTourExcel().execute();
                        new MapTourSearch().execute();
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private class MapTourSearch extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(MapTour.this);
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
            map_search_list_tour.setAdapter(mapSearchTourAdapter);
            progressDialog.dismiss();
        }

    }

    private class WideMapTourSearch extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(MapTour.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려 주십시오.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wideSearch();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            map_search_list_tour.setAdapter(mapSearchTourAdapter);
            progressDialog.dismiss();
        }

    }

    private class MapTourExcel extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(MapTour.this);
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

        map_search_list_tour = (ListView) findViewById(R.id.map_tour);
        mapSearchTourAdapter = new ListViewAdapter();

        String[] strData = filtering(MapsActivity.markerAddress);
        String[] address;

        for(int i = 0; i < rowTotal - 1; i++){
            address = addressFiltering(mapTourAdapter.getAddress(i));

            switch(address.length){
                case 2:
                    break;
                case 3 :
                    if(address[2].contains(strData[0])){
                        mapSearchTourAdapter.addItem(mapTourAdapter.getTitle(i), mapTourAdapter.getAddress(i), mapTourAdapter.getTel(i));
                    }
                    break;
                case 4 :
                    if(address[2].contains(strData[0])
                            && address[3].contains(strData[1])){
                        mapSearchTourAdapter.addItem(mapTourAdapter.getTitle(i), mapTourAdapter.getAddress(i), mapTourAdapter.getTel(i));
                    }
                    break;
                default :
                    if(address[2].contains(strData[0])
                            && address[3].contains(strData[1])
                            && address[4].contains(strData[2])){
                        mapSearchTourAdapter.addItem(mapTourAdapter.getTitle(i), mapTourAdapter.getAddress(i), mapTourAdapter.getTel(i));
                    }
                    break;
            }
        }

        map_search_list_tour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=";
                String title = mapSearchTourAdapter.getTitle(position);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + title));
                startActivity(intent);
            }
        });

    }


    private void wideSearch() {

        map_search_list_tour = (ListView) findViewById(R.id.map_tour);
        mapSearchTourAdapter = new ListViewAdapter();

        String[] strData = filtering(MapsActivity.markerAddress);
        String[] address;

        for(int i = 0; i < rowTotal - 1; i++){
            address = addressFiltering(mapTourAdapter.getAddress(i));

            switch(address.length){
                case 2:
                    break;
                case 3 :
                    if(address[2].contains(strData[0])){
                        mapSearchTourAdapter.addItem(mapTourAdapter.getTitle(i), mapTourAdapter.getAddress(i), mapTourAdapter.getTel(i));
                    }
                    break;
                case 4 :
                    if(address[2].contains(strData[0])
                            && address[3].contains(strData[1])){
                        mapSearchTourAdapter.addItem(mapTourAdapter.getTitle(i), mapTourAdapter.getAddress(i), mapTourAdapter.getTel(i));
                    }
                    break;
                default :
                    if(address[2].contains(strData[0])
                            && address[3].contains(strData[1])){
                        mapSearchTourAdapter.addItem(mapTourAdapter.getTitle(i), mapTourAdapter.getAddress(i), mapTourAdapter.getTel(i));
                    }
                    break;
            }
        }

        map_search_list_tour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=";
                String title = mapSearchTourAdapter.getTitle(position);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + title));
                startActivity(intent);
            }
        });

    }


    private void getExcel() {

        Workbook wb = null;

        try {
            InputStream is = getBaseContext().getResources().getAssets().open("tour.xls");
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
                        mapTourAdapter.addItem(title, address, tel);
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
