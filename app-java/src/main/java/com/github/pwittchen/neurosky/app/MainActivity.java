package com.github.pwittchen.neurosky.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.Preconditions;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

  private final static String LOG_TAG = "NeuroSky";
  private NeuroSky neuroSky;

  @BindView(R.id.tv_state) TextView tvState;
  @BindView(R.id.tv_attention) TextView tvAttention;
  @BindView(R.id.tv_meditation) TextView tvMeditation;
  @BindView(R.id.tv_blink) TextView tvBlink;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    neuroSky = createNeuroSky();
  }

  @Override protected void onResume() {
    super.onResume();
    if (neuroSky != null && Preconditions.isConnected(neuroSky.getDevice())) {
      neuroSky.startMonitoring();
    }
  }

  @Override protected void onPause() {
    super.onPause();
    if (neuroSky != null && Preconditions.isConnected(neuroSky.getDevice())) {
      neuroSky.stopMonitoring();
    }
  }

  @NonNull private NeuroSky createNeuroSky() {
    return new NeuroSky(new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
          neuroSky.startMonitoring();
        }

        tvState.setText(state.toString());
        Log.d(LOG_TAG, state.toString());
      }

      @Override public void onSignalChange(Signal signal) {

        switch (signal) {
          case ATTENTION:
            tvAttention.setText(getFormattedMessage("attention: %d", signal));
            break;
          case MEDITATION:
            tvMeditation.setText(getFormattedMessage("meditation: %d", signal));
            break;
          case BLINK:
            tvBlink.setText(getFormattedMessage("blink: %d", signal));
            break;
        }

        Log.d(LOG_TAG, String.format("%s: %d", signal.toString(), signal.getValue()));
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
        for (BrainWave brainWave : brainWaves) {
          Log.d(LOG_TAG, String.format("%s: %d", brainWave.toString(), brainWave.getValue()));
        }
      }
    });
  }

  private String getFormattedMessage(String messageFormat, Signal signal) {
    return String.format(Locale.getDefault(), messageFormat, signal.getValue());
  }

  @OnClick(R.id.btn_connect) void connect() {
    try {
      neuroSky.connect();
    } catch (BluetoothNotEnabledException e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
      Log.d(LOG_TAG, e.getMessage());
    }
  }

  @OnClick(R.id.btn_disconnect) void disconnect() {
    neuroSky.disconnect();
  }

  @OnClick(R.id.btn_start_monitoring) void startMonitoring() {
    neuroSky.startMonitoring();
  }

  @OnClick(R.id.btn_stop_monitoring) void stopMonitoring() {
    neuroSky.stopMonitoring();
  }
}
