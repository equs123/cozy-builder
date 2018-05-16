package com.cozy_builder.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownImageActivity extends AppCompatActivity {
    ImageView img1;
    Button btnLoad;
    String imgUrl = Common.SERVER_URL+"/mobile/images/eunji.jpg";
    Bitmap bm;

    //핸들러 선언
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //이미지뷰에 비트맵이 출력됨
            img1.setImageBitmap(bm);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_image);

        btnLoad = (Button) findViewById(R.id.btnLoad);
        img1 = (ImageView) findViewById(R.id.img1);
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downImg(imgUrl);
            }
        });
    }
    void downImg(final String file) {
        //네트워크 관련 작업은 반드시 백그라운드 스레드에서 실행해야 함
        //앞장에서처럼 조건체크
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                URL url=null;
                try {
                    url = new URL(file); //url 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //url에 접속
                    InputStream is  = conn.getInputStream(); //스트림생성

                    //스트림으로부터 이미지를 다운로드 받아 비트맵으로 저장
                    bm = BitmapFactory.decodeStream(is);
                    //핸들러에게 이미지 출력요청
                    handler.sendEmptyMessage(0);
                    conn.disconnect(); //url 접속 해제

                }catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });
        th.start();
    }
}
