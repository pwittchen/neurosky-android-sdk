package com.github.pwittchen.neurosky.app.rxjava;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.github.pwittchen.neurosky.library.rx.RxNeuroSky;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

  private final static String LOG_TAG = "NeuroSky";
  private RxNeuroSky rxNeuroSky;

  @BindView(R.id.tv_state) TextView tvState;
  @BindView(R.id.tv_attention) TextView tvAttention;
  @BindView(R.id.tv_meditation) TextView tvMeditation;
  @BindView(R.id.tv_blink) TextView tvBlink;
  private Disposable disposable;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    rxNeuroSky = new RxNeuroSky();
  }

  @Override protected void onResume() {
    super.onResume();
    handleBrainMessages();
  }

  private void handleBrainMessages() {
    disposable = rxNeuroSky
        .stream()
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnCancel(() -> rxNeuroSky.stopMonitoring())
        .subscribe(brainMessage -> {
          if (!brainMessage.getState().equals(State.UNKNOWN)) {
            tvState.setText(brainMessage.getState().toString());
            Log.d(LOG_TAG, brainMessage.getState().toString());
          }

          switch (brainMessage.getSignal()) {
            case ATTENTION:
              tvAttention.setText(getFormattedMessage("attention: %d", brainMessage.getSignal()));
              break;
            case MEDITATION:
              tvMeditation.setText(getFormattedMessage("meditation: %d", brainMessage.getSignal()));
              break;
            case BLINK:
              tvBlink.setText(getFormattedMessage("blink: %d", brainMessage.getSignal()));
              break;
          }

          if (!brainMessage.getBrainWaves().isEmpty()) {
            for (BrainWave brainWave : brainMessage.getBrainWaves()) {
              Log.d(LOG_TAG, String.format("%s: %d", brainWave.toString(), brainWave.getValue()));
            }
          }
        }, throwable -> Log.d(LOG_TAG, throwable.getMessage()));
  }

  private String getFormattedMessage(String messageFormat, Signal signal) {
    return String.format(Locale.getDefault(), messageFormat, signal.getValue());
  }

  @OnClick(R.id.btn_connect) void connect() {
    try {
      rxNeuroSky.connect();
    } catch (BluetoothNotEnabledException e) {
      Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
      Log.d(LOG_TAG, e.getMessage());
    }
  }

  @OnClick(R.id.btn_disconnect) void disconnect() {
    rxNeuroSky.disconnect();
  }

  @OnClick(R.id.btn_start_monitoring) void startMonitoring() {
    rxNeuroSky.startMonitoring();
  }

  @OnClick(R.id.btn_stop_monitoring) void stopMonitoring() {
    rxNeuroSky.startMonitoring();
  }

  @Override protected void onPause() {
    super.onPause();
    if (disposable != null && !disposable.isDisposed()) {
      disposable.dispose();
    }
  }
}
