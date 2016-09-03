package com.example.logger;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.orhanobut.logger.LoggerFragment;

/**
 * Created by jcabotage on 6/28/2016.
 */
public class ExampleLoggerFragment extends LoggerFragment implements View.OnClickListener {

    Button lifecycleLoggingButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //maintain lifecycle logging enabled/disabled state through orientation change
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_example_logger, container, false);

        fragmentView.findViewById(R.id.logger_fragment_button_1).setOnClickListener(this);
        lifecycleLoggingButton = (Button) fragmentView.findViewById(R.id.logger_fragment_button_2);
        lifecycleLoggingButton.setOnClickListener(this);

        if (isLogLifecycle()) {
            lifecycleLoggingButton.setText("Disable Lifecycle Logging");
        } else {
            lifecycleLoggingButton.setText("Enable Lifecycle Logging");
        }

        return fragmentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logger_fragment_button_1:
                this.d("Logger Fragment Button 1 pressed");
                break;
            case R.id.logger_fragment_button_2:
                if (isLogLifecycle()) {
                    setLogLifecycle(false);
                    lifecycleLoggingButton.setText("Enable Lifecycle Logging");
                } else {
                    setLogLifecycle(true);
                    lifecycleLoggingButton.setText("Disable Lifecycle Logging");
                    Toast.makeText(getActivity(),
                            "Try changing the device orientation to see the fragment's lifecycle callbacks",
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
