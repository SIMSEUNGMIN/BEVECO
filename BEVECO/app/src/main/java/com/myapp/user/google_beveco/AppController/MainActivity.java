package com.myapp.user.google_beveco.AppController;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.myapp.user.google_beveco.View.SearchHotel;
import com.myapp.user.google_beveco.R;
import com.myapp.user.google_beveco.Model.ShortPath;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shortPath = (Button)findViewById(R.id.button1_shortPath);
        Button hotel = (Button)findViewById(R.id.button2_hotel);
        Button sight = (Button)findViewById(R.id.button3_sight);
        Button food = (Button)findViewById(R.id.button4_food);

        shortPath.setOnClickListener(this);
        hotel.setOnClickListener(this);
        sight.setOnClickListener(this);
        food.setOnClickListener(this);

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button1_shortPath:
                clickShortPath();
                break;
            case R.id.button2_hotel:
                clickHotel();
                break;
            case R.id.button3_sight:
                clickSight();
                break;
            case R.id.button4_food:
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

    private void clickSight(){}

    private void clickFood(){}

}
