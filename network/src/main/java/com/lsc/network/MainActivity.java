package com.lsc.network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GetRequest<JSONObject> request = new GetRequest<JSONObject>("www.baidu.com");
        request.execute();
        request.execute(new JsonCallback<JSONObject>() {
            @Override
            public void onSuccess(ApiResponse<JSONObject> response) {
                super.onSuccess(response);
            }
        });

    }
}
