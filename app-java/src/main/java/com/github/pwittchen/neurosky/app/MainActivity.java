package com.github.pwittchen.neurosky.app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listeners.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainWave;
import com.github.pwittchen.neurosky.library.message.Signal;
import com.github.pwittchen.neurosky.library.message.State;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

  private final static String LOG_TAG = "NeuroSky";
  private NeuroSky neuroSky;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    neuroSky = createNeuroSky();

    //TODO: handle this onClick
    try {
      neuroSky.connect();
    } catch (BluetoothNotEnabledException e) {
      Log.d(LOG_TAG, e.getMessage());
    }

    //TODO: handle this onClick
    neuroSky.stopMonitoring();

    //TODO: handle this onClick
    neuroSky.disconnect();
  }

  @NonNull private NeuroSky createNeuroSky() {
    return new NeuroSky(new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
          neuroSky.startMonitoring();
        }

        Log.d(LOG_TAG, state.toString());
        //TODO: log value in the text view
      }

      @Override public void onSignalChange(Signal signal) {
        Log.d(LOG_TAG, String.format("%s: %d", signal.toString(), signal.getValue()));
        //TODO: log value in the text view
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
        for (BrainWave brainWave : brainWaves) {
          Log.d(LOG_TAG, String.format("%s: %d", brainWave.toString(), brainWave.getValue()));
          //TODO: log value in the text view
        }
      }
    });
  }
}
