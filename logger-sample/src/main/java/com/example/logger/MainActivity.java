package com.example.logger;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.orhanobut.logger.Logger;

/**
 * Created by jcabotage on 6/28/2016.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Main Activity");

        findViewById(R.id.main_button_1).setOnClickListener(this);
        findViewById(R.id.main_button_2).setOnClickListener(this);
        findViewById(R.id.main_button_3).setOnClickListener(this);
        findViewById(R.id.main_button_4).setOnClickListener(this);
        findViewById(R.id.main_button_5).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_button_1:
                Logger.d("Main Button %s pressed", 1);
                break;
            case R.id.main_button_2:
                Logger.t("ExtraTag").d("Main Button 2 pressed");
                break;
            case R.id.main_button_3:
                Logger.json("{\"widget\":{\"debug\":\"on\",\"window\":{\"title\":\"Sample Konfabulator Widget\",\"name\":\"main_window\",\"width\":500,\"height\":500}}}");
                Logger.json("[{\"key\":3}]");
                break;
            case R.id.main_button_4:
                Logger.xml("<widget><debug>on</debug><window><title>Sample Konfabulator Widget</title><name>main_window</name><width>500</width><height>500</height></window></widget>");
                break;
            case R.id.main_button_5:
                Intent i = new Intent(MainActivity.this, ExampleLoggerActivity.class);
                startActivity(i);
                break;
        }
    }
}
