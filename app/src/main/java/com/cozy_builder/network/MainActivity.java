package com.cozy_builder.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }
    public void onClick (View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.button1:
                intent = new Intent(this, NetworkStatusActivity.class);
                break;
            case R.id.button2:
                intent = new Intent(this, DownHtmlActivity.class);
                break;
            case R.id.button3:
                intent = new Intent(this, DownImageActivity.class);
                break;
        }
        startActivity(intent);
    }
}
