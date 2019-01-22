package com.myapp.user.google_beveco.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.myapp.user.google_beveco.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends Activity implements View.OnClickListener {

    ListView SelectList;
    ListView ShortestListView;
    private List<String> list = new ArrayList<>();
    private List<String> shortestList = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private ArrayAdapter<String> shortestAdapter;
    private Place[] ShortestPlaces = new Place[5];

    Button shortestButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_list);
        Button ListButton, MapButton;
        SelectList = findViewById(R.id.selectList);
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice);
        shortestAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        shortestButton = findViewById(R.id.shortest);
        shortestButton.setOnClickListener(this);
        ShortestListView = findViewById(R.id.shortestListView);
        SelectList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        SelectList.setOnItemLongClickListener(longClickListener);
        ListButton = findViewById(R.id.ListButton);
        ListButton.setOnClickListener(this);
        MapButton= findViewById(R.id.MapButton);
        MapButton.setOnClickListener(this);
        MapsActivity.setShortestPlace(null);
        list.clear();
        //SelectPlace를 listView에 띄우기위한 작업
        for (int i = 0; i < MapsActivity.inputNumber(); i++) {
            list.add((String) MapsActivity.SelectPlaceWithIndex(i).getName());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listAdapter.clear();
                listAdapter.addAll(list);
                SelectList.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

            }
        });
        if(MapsActivity.inputNumber()>=1){
            SelectList.setItemChecked(0, true);
        }

    }
    //position위치에 있는 item을 longClick하면 삭제할 지 물어보는 함수
    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(ListActivity.this);
            dlg.setMessage("삭제하시겠습니까?");
            final int positiontoRemove = position;
            dlg.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    list.remove(positiontoRemove);
                    boolean isStart = false;
                    if(positiontoRemove==ListActivity.this.SelectList.getCheckedItemPosition()){
                        isStart = true;
                    }
                    //삭제할 경우, shortestPlace도 초기화
                    shortestAdapter.clear();
                    MapsActivity.setShortestPlace(null);
                    //삭제한 위치에 맨 끝 위치에 있는 item을 옮겨옴
                    if (positiontoRemove != MapsActivity.inputNumber() - 1) {
                        Place replace = MapsActivity.SelectPlaceWithIndex(MapsActivity.inputNumber - 1);
                        MapsActivity.setSelectPlaceWithIndex(positiontoRemove, replace);
                    }
                    MapsActivity.setInputNumber(MapsActivity.inputNumber()-1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter.clear();
                            listAdapter.addAll(list);
                            SelectList.setAdapter(listAdapter);
                            listAdapter.notifyDataSetChanged();
                            ShortestListView.setAdapter(shortestAdapter);
                            shortestAdapter.notifyDataSetChanged();

                        }
                    });
                    if(isStart){
                        ListActivity.this.SelectList.setItemChecked(0, true);
                    }
                }
            });
            dlg.setNegativeButton("아니오", null);
            dlg.show();

            return false;

        }
    };
    //최단경로 버튼을 눌렀을경우 최단경로를 찾아줌
    public void clickShortestButton() {
        if(MapsActivity.inputNumber()==0){
            Toast.makeText(this, "입력한 장소가 없습니다.", Toast.LENGTH_LONG).show();
            return;
        }

        int startPosition = SelectList.getCheckedItemPosition();
        ShortestPath shortestPath = new ShortestPath(MapsActivity.inputNumber(), MapsActivity.selectPlace, startPosition);
        shortestPath.findShortestPaths();
        this.ShortestPlaces = shortestPath.getShortestPoints();
        MapsActivity.setShortestPlace(ShortestPlaces);
        shortestList.clear();
        for (int i = 0; i < MapsActivity.inputNumber(); i++) {
            shortestList.add((String) ShortestPlaces[i].getName());
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                shortestAdapter.clear();
                shortestAdapter.addAll(shortestList);
                ShortestListView.setAdapter(shortestAdapter);
                shortestAdapter.notifyDataSetChanged();
            }
        });


    }
    //버튼을 눌렀을경우 실행할 함수 지정
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shortest:
                clickShortestButton();
                break;
            case R.id.MapButton:
                clickMapButton();
                break;
            case R.id.ListButton:
                break;
        }
    }
    //MapButton을 눌렀을 경우에는 mapActivity로 다시 돌아감.
    public void clickMapButton(){
        Intent baseIntent = new Intent(getApplicationContext(), MapsActivity.class);
        finish();
    }

}
