package com.myapp.user.google_beveco.Model;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myapp.user.google_beveco.R;
import com.myapp.user.google_beveco.View.MapFood;
import com.myapp.user.google_beveco.View.MapHotel;
import com.myapp.user.google_beveco.View.MapTour;

import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener{

    GoogleMap GMap;
    public EditText searchText;
    public Button searchButton;
    Button ListButton;
    Button MapButton;

    public static Place[] selectPlace = new Place[5];
    public static int inputNumber = 0;

    public static Place[] ShortestPlace;

    public static String markerAddress = null;


    public static Place SelectPlaceWithIndex(int index) {
        return selectPlace[index];
    }

    public static void setShortestPlace(Place[] shortestPlace) {
        ShortestPlace = shortestPlace;
    }

    public static Place[] getShortestPlace() {
        return ShortestPlace;
    }

    public static void setSelectPlaceWithIndex(int index, Place place) {
        selectPlace[index] = place;
    }

    public static int inputNumber() {
        return inputNumber;
    }

    public static void setInputNumber(int number) {
        inputNumber = number;
    }

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchText = findViewById(R.id.searchText);
        searchButton = findViewById(R.id.searchButton);
        ListButton = findViewById(R.id.ListButton);
        MapButton = findViewById(R.id.MapButton);

        searchText.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        ListButton.setOnClickListener(this);
        MapButton.setOnClickListener(this);

    }

    //버튼을 눌렀을경우 실행할 함수 지정
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchText:
                clickSearchButton();
                break;
            case R.id.searchButton:
                clickSearchButton();
                break;
            case R.id.ListButton:
                clickListButton();
                break;
            case R.id.MapButton:
                break;//MapButton을 눌렀을경우 MapsActivity자신을 호출하기때문에 아무일도 설정해주지않았음.
        }
    }

    //SearchButton을 눌렀을때 실행 할 함수
    public void clickSearchButton() {
        Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
        startActivityForResult(intent, 0);
    }
    //SearchActivity와 ListActivity가 종료되었을경우 실행하는 함수
    //SearchActivity의 requestCode는 0, 입력된 값들을 Place객체(Google)로 저장하고 marker를 띄움
    //ListActivity의 requestCode는 0, ShortestPath를 기반으로 한 마커를 띄움
    //이때 SearchActivity는 listView에서 선택할때 정상종료되므로 resultCode==RESULT_OK가 필수적이나, ListActivity는 requestCode가 굳이 필요하지는 않음
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK) {
            if (inputNumber >= 5) {
                Toast.makeText(this, "5개까지만 입력할 수 있습니다.", Toast.LENGTH_LONG).show();
            } else {
                final double longitude = data.getDoubleExtra("longitude", 0);
                final double latitude = data.getDoubleExtra("latitude", 0);
                final String address = data.getStringExtra("address");
                final String POIName = data.getStringExtra("POI NAME");
                Place selected = new Place() {
                    @Override
                    public String getId() {
                        return null;
                    }

                    @Override
                    public List<Integer> getPlaceTypes() {
                        return null;
                    }

                    @Override
                    public CharSequence getAddress() {
                        return (CharSequence) address;
                    }

                    @Override
                    public Locale getLocale() {
                        return null;
                    }

                    @Override
                    public CharSequence getName() {
                        return (CharSequence) POIName;
                    }

                    @Override
                    public LatLng getLatLng() {
                        return new LatLng(latitude, longitude);
                    }


                    @Override
                    public LatLngBounds getViewport() {
                        return null;
                    }


                    @Override
                    public Uri getWebsiteUri() {
                        return null;
                    }


                    @Override
                    public CharSequence getPhoneNumber() {
                        return null;
                    }

                    @Override
                    public float getRating() {
                        return 0;
                    }

                    @Override
                    public int getPriceLevel() {
                        return 0;
                    }


                    @Override
                    public CharSequence getAttributions() {
                        return null;
                    }

                    @Override
                    public Place freeze() {
                        return null;
                    }

                    @Override
                    public boolean isDataValid() {
                        return false;
                    }
                };
                selectPlace[inputNumber] = selected;
                inputNumber++;
                this.setMarkerWithSelectPlace();
            }
        }
        if (requestCode == 1) {
            this.setMarkerWithShortestPlace();

        }

    }
    //선택한 장소에대한 marker를 띄움 (최단 경로 찾기 전)
    public void setMarkerWithSelectPlace() {
        GMap.clear();

        int i = 0;

        for (i = 0; i < this.inputNumber(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(SelectPlaceWithIndex(i).getLatLng());
            markerOptions.title((String) SelectPlaceWithIndex(i).getName());
            markerOptions.snippet((String)SelectPlaceWithIndex(i).getAddress());
            GMap.addMarker(markerOptions);
        }

        if(inputNumber() != 0){
            //가장 최근에 선택된 곳으로 카메라를 옮김
            CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(SelectPlaceWithIndex(i-1).getLatLng(), 10);
            GMap.animateCamera(zoom);
        }

    }
    //최단경로를 기반으로 마커를 띄움
    public void setMarkerWithShortestPlace() {
        GMap.clear();
        if (ShortestPlace != null) {
            //ListActivity를 실행하였지만 최단경로 버튼을 안눌렀을 경우에는 SelectPlace로 마커를 띄울 것임.
            for (int i = 0; i < this.inputNumber(); i++) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(ShortestPlace[i].getLatLng());
                markerOptions.title((String) ShortestPlace[i].getName());

                switch (i) {//무지개색으로 경로 표시
                    case 0:
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));//첫번째방문지
                        break;
                    case 1:
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));//두번째방문지
                        break;
                    case 2:
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));//세번째방문지
                        break;
                    case 3:
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));//네번째방문지
                        break;
                    case 4:
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));//다섯번째방문지
                        break;

                }
                markerOptions.title((String) getShortestPlace()[i].getName());
                markerOptions.snippet(i+1 + "번째 방문지 " + "\n" + getShortestPlace()[i].getAddress());
                GMap.addMarker(markerOptions);

                //중심에 대한 위치 설정
                LatLng center = new LatLng(36.52487, 127.92723);
                CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(center, 7);
                GMap.animateCamera(zoom);

                GMap.setOnInfoWindowClickListener(infoWindowClickListener);
            }
        }else{
            this.setMarkerWithSelectPlace();
        }
    }


    //ListButton을 눌렀을 경우 ListActivity실행
    public void clickListButton() {
        Intent intent = new Intent(getApplicationContext(), ListActivity.class);
        startActivityForResult(intent, 1);
    }

    //googleMap띄우기
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //구글 맵 객체를 불러온다.
        GMap = googleMap;

        inputNumber = 0;

        //중심에 대한 위치 설정
        LatLng center = new LatLng(36.52487, 127.92723);

        //구글 지도에서의 줌 레벨은 1~23까지 가능하다.
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(center, 7);
        googleMap.animateCamera(zoom);

//        //구글 맵에 표시할 마커에 대한 옵션 설정
//        MarkerOptions marker = new MarkerOptions();
//        marker.position(seoul).title("원하는 위치(위도, 경도)에 마커를 표시했습니다.");

        //카메라를 여의도 위치로 옮긴다.
        //gMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));
    }

    // 마커의 정보창을 클릭한 경우
    GoogleMap.OnInfoWindowClickListener infoWindowClickListener = new GoogleMap.OnInfoWindowClickListener() {
        @Override
        public void onInfoWindowClick(Marker marker) {
            markerAddress = marker.getSnippet();
            //Toast.makeText(MapsActivity.this, "정보창 클릭 Marker ID : "+markerId, Toast.LENGTH_SHORT).show();
            DialogSelectOption();
        }
    };


    private void DialogSelectOption(){
        final String item[] = {"숙박", "투어", "맛집"};
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("원하는 서비스를 클릭하세요.");
        ab.setSingleChoiceItems(item, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //각 리스트를 선택했을 때
                switch(which){
                    case 0 :
                        Intent intent1 = new Intent(MapsActivity.this, MapHotel.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(MapsActivity.this, MapTour.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        //시/구, 동 단위 선택 없이 그냥 한다
                        Intent intent3 = new Intent(MapsActivity.this, MapFood.class);
                        startActivity(intent3);
                        break;
                }
            }
        });

        //다이얼로그 생성
        ab.create();
        ab.show();

    }
}
