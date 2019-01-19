package com.myapp.user.google_beveco.Model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.myapp.user.google_beveco.R;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SearchTour extends Activity implements View.OnClickListener {

    private EditText searchTour;
    private Button searchButton;

    private ListView list_tour;
    private ListViewAdapter tourAdapter;

    private int rowTotal;

    private ListView search_list_tour;
    private ListViewAdapter searchTourAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tour);

        searchTour = (EditText) findViewById(R.id.search_tour);
        searchButton = (Button) findViewById(R.id.search_button);

        list_tour = (ListView) findViewById(R.id.list_tour);
        tourAdapter = new ListViewAdapter();

        new SearchTour.TourExcel().execute();

        search_list_tour = (ListView) findViewById(R.id.list_tour);

        searchButton.setOnClickListener(this);

        searchTour.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    new TourSearch().execute();
                    return true;
                }
                return false;
            }
        });

        search_list_tour.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query=";
                String title = searchTourAdapter.getTitle(position);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url + title));
                startActivity(intent);
            }
        });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                new TourSearch().execute();
                break;
        }
    }

    private class TourSearch extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(SearchTour.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("잠시만 기다려 주십시오.");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            clickSearchButton();
            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            search_list_tour.setAdapter(searchTourAdapter);
            progressDialog.dismiss();
        }

    }

    private class TourExcel extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            progressDialog = new ProgressDialog(SearchTour.this);
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
            list_tour.setAdapter(tourAdapter);
            progressDialog.dismiss();
        }
    }

    private void clickSearchButton() {

        searchTourAdapter = new ListViewAdapter();

        String strData;
        strData = searchTour.getText().toString();

        for(int i = 0; i < rowTotal - 1; i++){
            if(tourAdapter.getTitle(i).contains(strData) || tourAdapter.getAddress(i).contains(strData)){
                searchTourAdapter.addItem(tourAdapter.getTitle(i), tourAdapter.getAddress(i), tourAdapter.getTel(i));
            }
        }

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
                        tourAdapter.addItem(title, address, tel);
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

}
