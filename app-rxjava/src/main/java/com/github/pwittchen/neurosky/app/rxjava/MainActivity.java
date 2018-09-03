package com.github.pwittchen.neurosky.app.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

  private final static String LOG_TAG = "NeuroSky";

  @BindView(R.id.tv_state) TextView tvState;
  @BindView(R.id.tv_attention) TextView tvAttention;
  @BindView(R.id.tv_meditation) TextView tvMeditation;
  @BindView(R.id.tv_blink) TextView tvBlink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

  }

  @Override protected void onResume() {
    super.onResume();
  }

  @OnClick(R.id.btn_connect) void connect() {
  }

  @OnClick(R.id.btn_disconnect) void disconnect() {
  }

  @OnClick(R.id.btn_start_monitoring) void startMonitoring() {
  }

  @OnClick(R.id.btn_stop_monitoring) void stopMonitoring() {
  }

  @Override protected void onPause() {
    super.onPause();
  }
}
