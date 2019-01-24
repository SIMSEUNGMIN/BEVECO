package com.myapp.user.google_beveco.View;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.myapp.user.google_beveco.AppController.MainActivity;

public class StartView extends Activity {
    //어플 시작 화면

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        try{
            //앱의 시작 화면을 1.5초 동안 유지
            Thread.sleep(1500);
        }
        catch(InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
