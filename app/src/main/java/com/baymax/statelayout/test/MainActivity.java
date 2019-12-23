package com.baymax.statelayout.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.baymax.statelayout.StateLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, StateLayout.OnStateClickListener {
    private static final String STATE_CUSTOM1 = "custom1";
    private static final String STATE_CUSTOM2 = "custom2";
    private StateLayout stateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateLayout = findViewById(R.id.state_layout);

        findViewById(R.id.empty).setOnClickListener(this);
        findViewById(R.id.content).setOnClickListener(this);
        findViewById(R.id.error).setOnClickListener(this);
        findViewById(R.id.loading).setOnClickListener(this);
        findViewById(R.id.offline).setOnClickListener(this);
        findViewById(R.id.custom1).setOnClickListener(this);
        findViewById(R.id.custom2).setOnClickListener(this);

        stateLayout.setCustomViewSrc(STATE_CUSTOM1, R.layout.main_custom1_src);
        stateLayout.setCustomViewSrc(STATE_CUSTOM2, R.layout.main_custom2_src);

        stateLayout.setErrorClickListener(this);
        stateLayout.setEmptyClickListener(this);
        stateLayout.setCustomStateListener(STATE_CUSTOM1, this);
        stateLayout.setCustomStateListener(STATE_CUSTOM2, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.empty:
                stateLayout.showEmpty();
                break;
            case R.id.content:
                stateLayout.showContent();
                break;
            case R.id.error:
                stateLayout.showError();
                break;
            case R.id.loading:
                stateLayout.showLoading();
                break;
            case R.id.offline:
                stateLayout.showOffline();
                break;
            case R.id.custom1:
                stateLayout.show(STATE_CUSTOM1);
                break;
            case R.id.custom2:
                stateLayout.show(STATE_CUSTOM2);
                break;
            default:
        }
    }

    @Override
    public void onClick(View v, String state) {
        switch (state) {
            case StateLayout.EMPTY:
            case StateLayout.ERROR:
            case STATE_CUSTOM1:
            case STATE_CUSTOM2:
                stateLayout.showLoading();
                break;
            default:
        }
    }
}
