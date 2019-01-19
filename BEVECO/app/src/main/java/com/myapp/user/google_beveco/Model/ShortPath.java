package com.myapp.user.google_beveco.Model;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.myapp.user.google_beveco.R;

public class ShortPath extends FragmentActivity
                        implements OnMapReadyCallback {

    //구글 맵 참조 변수 생성
    GoogleMap gMap;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.short_path);

        //SupportMapFragment을 통해 레이아웃에 만든 fragment의 ID를 참조하고 구글맵을 호출.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //getMapAsync must be called on the main thread.
    }

    //onCreate()에서 getMapAsync()를 통해 onMapReady()가 자동호출 되면서 아래 작업을 수행.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //구글 맵 객체를 불러온다.
        gMap = googleMap;

        //서울 여의도에 대한 위치 설정
        LatLng seoul = new LatLng(37.52487, 126.92723);

        //구글 지도에서의 줌 레벨은 1~23까지 가능하다.
        CameraUpdate zoom = CameraUpdateFactory.newLatLngZoom(seoul, 15);
        googleMap.animateCamera(zoom);

        //구글 맵에 표시할 마커에 대한 옵션 설정
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(seoul).title("원하는 위치(위도, 경도)에 마커를 표시했습니다.");

        //카메라를 여의도 위치로 옮긴다.
        //gMap.moveCamera(CameraUpdateFactory.newLatLng(seoul));

        //마커를 생성한다.
        gMap.addMarker(markerOptions);

    }
}
