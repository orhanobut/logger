package com.example.logger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.LoggerActivity;

/**
 * Created by jcabotage on 6/28/2016.
 */
public class ExampleLoggerActivity extends LoggerActivity implements View.OnClickListener {

    ExampleLoggerFragment fragment;
    Button addFragmentButton;
    Button lifecycleLoggingButton;
    static final String FRAGMENT_TAG = "fragment_tag";
    static final String LIFECYCLE_LOGGING_KEY = "lifecycle_logging_key";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_example_logger);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Example LoggerActivity");

        findViewById(R.id.logger_activity_button_1).setOnClickListener(this);

        lifecycleLoggingButton = (Button) findViewById(R.id.logger_activity_button_2);
        lifecycleLoggingButton.setOnClickListener(this);

        addFragmentButton = (Button) findViewById(R.id.logger_activity_button_3);
        addFragmentButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            fragment = (ExampleLoggerFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
            setLogLifecycle(savedInstanceState.getBoolean(LIFECYCLE_LOGGING_KEY));
        }

        if (fragment == null) {
            fragment = new ExampleLoggerFragment();
        }

        if (fragment.isAdded()) {
            addFragmentButton.setText("Remove Example LoggerFragment");
        } else {
            addFragmentButton.setText("Add Example LoggerFragment");
        }

        if (isLogLifecycle()) {
            lifecycleLoggingButton.setText("Disable Lifecycle Logging");
        } else {
            lifecycleLoggingButton.setText("Enable Lifecycle Logging");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logger_activity_button_1:
                this.d("Logger Activity Button 1 pressed");
                break;
            case R.id.logger_activity_button_2:
                if (isLogLifecycle()) {
                    setLogLifecycle(false);
                    lifecycleLoggingButton.setText("Enable Lifecycle Logging");
                } else {
                    setLogLifecycle(true);
                    lifecycleLoggingButton.setText("Disable Lifecycle Logging");
                    Toast.makeText(ExampleLoggerActivity.this,
                            "Try changing the device orientation to see the activity's lifecycle callbacks",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.logger_activity_button_3:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                if (fragment.isAdded()) {
                    transaction.remove(fragment);
                    addFragmentButton.setText("Add Example LoggerFragment");
                } else {
                    transaction.add(R.id.logger_activity_fragment_container, fragment, FRAGMENT_TAG);
                    addFragmentButton.setText("Remove Example LoggerFragment");
                }
                transaction.commit();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //maintain lifecycle logging enabled/disabled state through orientation change
        outState.putBoolean(LIFECYCLE_LOGGING_KEY, isLogLifecycle());
    }
}
