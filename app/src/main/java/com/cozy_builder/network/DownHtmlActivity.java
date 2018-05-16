package com.cozy_builder.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownHtmlActivity extends AppCompatActivity {
    String html;
    TextView result;

    //핸들러 선언
    //백그라운드 스레드에서는 메인화면을 고칠수 없으므로 핸들러에게 대신 요청해야함
    Handler handler = new Handler(){
        //alt+insert , override method
        //핸들러에 전달된 메세지를 처리하는 코드
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            result.setText(html);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.down_html);

        //네트워크 관련 작업은 반드시 백그라운드 스레드에서 처리
        //백그라운드 스레드에서는 메인화면을 터치 할 수 없음
        result = (TextView)findViewById(R.id.result);

        Button btn = (Button) findViewById(R.id.down);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //result.setText(downloadHtml(Common.SERVER_URL+ "/mobile/main.jsp"));
                Thread th = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        html = downloadHtml(Common.SERVER_URL+ "/mobile/main.jsp");
                        //핸들러에게 메세지 요청
                        handler.sendEmptyMessage(0);
                    }
                });
                th.start();
            }
        });
    }
    String downloadHtml (String addr) {
        StringBuilder html = new StringBuilder();
        try {
            URL url = new URL(addr); //url 객체 생성

            //url 접속 처리 객체
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            if(conn != null) { // url 형식에 맞으면

                //타임아웃 시간 설정
                conn.setConnectTimeout(10000);

                //캐쉬 사용 여부
                conn.setUseCaches(false);

                //url에 접속 성공하면
                if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));

                    while(true) {
                        String line = br.readLine(); // 한라인을 읽음
                        if(line == null) break; //더이상 내용이 없으면 종료
                        html.append(line+"\n");
                    }
                    br.close();//버퍼닫기
                }
                conn.disconnect(); //웹서버와의 연결 정료
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return html.toString();
    }
}
