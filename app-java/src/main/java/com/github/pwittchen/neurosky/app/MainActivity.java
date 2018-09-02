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
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainWave;
import com.github.pwittchen.neurosky.library.message.Signal;
import com.github.pwittchen.neurosky.library.message.State;
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
            tvAttention.setText(
                String.format(Locale.getDefault(), "attention: %d", signal.getValue())
            );
            break;
          case MEDITATION:
            tvMeditation.setText(
                String.format(Locale.getDefault(), "meditation: %d", signal.getValue())
            );
            break;
          case BLINK:
            tvBlink.setText(
                String.format(Locale.getDefault(), "blink: %d", signal.getValue())
            );
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
