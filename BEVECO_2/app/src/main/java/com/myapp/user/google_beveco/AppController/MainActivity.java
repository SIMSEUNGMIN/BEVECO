package com.myapp.user.google_beveco.AppController;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageButton;

import com.myapp.user.google_beveco.Model.SearchFood;
import com.myapp.user.google_beveco.Model.SearchHotel;
import com.myapp.user.google_beveco.Model.SearchTour;
import com.myapp.user.google_beveco.R;
import com.myapp.user.google_beveco.Model.ShortPath;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton shortPath = (ImageButton)findViewById(R.id.image_button1_shortPath);
        ImageButton hotel = (ImageButton)findViewById(R.id.image_button2_hotel);
        ImageButton tour = (ImageButton)findViewById(R.id.image_button3_tour);
        ImageButton food = (ImageButton)findViewById(R.id.image_button4_food);

        shortPath.setOnClickListener(this);
        hotel.setOnClickListener(this);
        tour.setOnClickListener(this);
        food.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.image_button1_shortPath:
                clickShortPath();
                break;
            case R.id.image_button2_hotel:
                clickHotel();
                break;
            case R.id.image_button3_tour:
                clickTour();
                break;
            case R.id.image_button4_food:
                clickFood();
                break;
        }
    }

    private void clickShortPath(){
        Intent intent = new Intent(this, ShortPath.class);
        startActivity(intent);
    }

    private void clickHotel(){
        Intent intent = new Intent(this, SearchHotel.class);
        startActivity(intent);
    }

    private void clickTour(){
        Intent intent = new Intent(this, SearchTour.class);
        startActivity(intent);
    }

    private void clickFood(){
        Intent intent = new Intent(this, SearchFood.class);
        startActivity(intent);
    }

}
