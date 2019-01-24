package com.myapp.user.google_beveco.Model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.myapp.user.google_beveco.R;
import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapTapi;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity implements View.OnClickListener {

    private EditText searchText;
    private Button searchButton;
    private List<String> list = new ArrayList<>();
    //검색로그에서 포인트를 저장하는 리스트
    private List<TMapPOIItem> tMapPoints = new ArrayList<>();
    public TMapData tMapData;
    public ListView searchListView;
    public int input = 0;
    private ArrayAdapter<String> listAdapter;
    public TMapPOIItem selectPoints = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        searchText.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        searchListView = findViewById(R.id.searchListView);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);


        tMapData = new TMapData();
        searchText.setOnKeyListener(new View.OnKeyListener() {
            //searchText에서 엔터를 누를 시 검색 수행
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_ENTER)) {
                    clickSearchButton();
                    return true;
                }
                return false;
            }
        });
    }
    //검색버튼을 눌렀을 경우 searchText에 들어간 값으로 검색하여 리스트 띄움
    public void clickSearchButton() {
        String strData;
        strData = searchText.getText().toString();
        TMapTapi tmaptapi = new TMapTapi(this);
        tmaptapi.setSKTMapAuthentication("fbd882e5-aa38-48d4-a301-3e865738dd40");

        tMapData.findAllPOI(strData, new TMapData.FindAllPOIListenerCallback() {
            @Override
            public void onFindAllPOI(ArrayList poiItems) {
                //이전에 있던 값들을 전부 초기화
                tMapPoints.clear();
                list.clear();
                for (int i = 0; i < poiItems.size(); i++) {
                    TMapPOIItem item = (TMapPOIItem) poiItems.get(i);
                    Log.d("POI Name: ", item.getPOIName().toString() + ", " +
                            "Address: " + item.getPOIAddress().replace("null", "") + ", " +
                            "Point: " + item.getPOIPoint().toString());
                    list.add(item.getPOIName());
                    //포인트 저장
                    tMapPoints.add(item);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listAdapter.clear();
                        listAdapter.addAll(list);
                        searchListView.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        //listView에 띄워진 아이템을 클릭했을때, baseIntent로 값을 넘겨주고 finish함.
        searchListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent baseIntent = new Intent(getApplicationContext(), MapsActivity.class);

                baseIntent.putExtra("POI NAME", tMapPoints.get(position).getPOIName());
                baseIntent.putExtra("address", tMapPoints.get(position).getPOIAddress());
                baseIntent.putExtra("latitude", tMapPoints.get(position).getPOIPoint().getLatitude());
                baseIntent.putExtra("longitude", tMapPoints.get(position).getPOIPoint().getLongitude());
                setResult(RESULT_OK,baseIntent);
                finish();

            }
        });

    }
    //버튼을 눌렀을때 실행할 함수지정
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.searchButton:
                clickSearchButton();
                break;

        }
    }

}
