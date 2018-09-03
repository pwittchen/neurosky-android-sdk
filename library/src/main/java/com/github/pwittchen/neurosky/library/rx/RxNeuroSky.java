package com.github.pwittchen.neurosky.library.rx;

import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainMessage;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.util.HashSet;
import java.util.Set;

public class RxNeuroSky {

  private NeuroSky neuroSky;

  public Flowable<BrainMessage> stream() {
    return stream(BackpressureStrategy.BUFFER);
  }

  public Flowable<BrainMessage> stream(BackpressureStrategy backpressureStrategy) {
    return Flowable.create(emitter ->
        neuroSky = new NeuroSky(new ExtendedDeviceMessageListener() {
          @Override public void onStateChange(State state) {
            if (state.equals(State.CONNECTED)) {
              startMonitoring();
            }

            emitter.onNext(new BrainMessage(state, Signal.STATE_CHANGE, new HashSet<>()));
          }

          @Override public void onSignalChange(Signal signal) {
            emitter.onNext(new BrainMessage(State.CONNECTED, signal, new HashSet<>()));
          }

          @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
            emitter.onNext(new BrainMessage(State.CONNECTED, Signal.EEG_POWER, brainWaves));
          }
        }), backpressureStrategy);
  }

  public void connect() {
    neuroSky.connect();
  }

  public void disconnect() {
    neuroSky.disconnect();
  }

  public void startMonitoring() {
    neuroSky.startMonitoring();
  }

  public void stopMonitoring() {
    neuroSky.stopMonitoring();
  }

  public void enableRawSignal() {
    neuroSky.enableRawSignal();
  }

  public void disableRawSignal() {
    neuroSky.disableRawSignal();
  }
}
