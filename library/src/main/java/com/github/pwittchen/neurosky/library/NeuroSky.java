package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainEvent;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.neurosky.thinkgear.TGDevice;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import java.util.HashSet;
import java.util.Set;

public class NeuroSky {

  private final static EventBus eventBus = EventBus.create();
  private TGDevice device;
  private boolean rawSignalEnabled = false;

  public NeuroSky(final DeviceMessageListener listener) {
    if (Preconditions.isBluetoothAdapterInitialized()) {
      DeviceMessageHandler handler = new DeviceMessageHandler(listener);
      device = new TGDevice(BluetoothAdapter.getDefaultAdapter(), handler);
    }
  }

  public NeuroSky() {
    this(new ExtendedDeviceMessageListener() {
      private State state = State.UNKNOWN;

      @Override public void onStateChange(State state) {
        this.state = state;
        eventBus.send(new BrainEvent(state, Signal.STATE_CHANGE, new HashSet<>()));
      }

      @Override public void onSignalChange(Signal signal) {
        eventBus.send(new BrainEvent(state, signal, new HashSet<>()));
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
        eventBus.send(new BrainEvent(state, Signal.EEG_POWER, brainWaves));
      }
    });
  }

  public Flowable<BrainEvent> stream(BackpressureStrategy backpressureStrategy) {
    return eventBus.receive(backpressureStrategy);
  }

  public Flowable<BrainEvent> stream() {
    return eventBus.receive();
  }

  public void connect() throws BluetoothNotEnabledException {
    if (!Preconditions.isBluetoothEnabled()) {
      throw new BluetoothNotEnabledException();
    }

    if (Preconditions.canConnect(device)) {
      device.connect(rawSignalEnabled);
    }
  }

  public void disconnect() {
    if (Preconditions.isConnected(device)) {
      device.close();
      device = null;
    }
  }

  public void enableRawSignal() {
    rawSignalEnabled = true;
  }

  public void disableRawSignal() {
    rawSignalEnabled = false;
  }

  public void startMonitoring() {
    if (Preconditions.isConnected(device)) {
      device.start();
    }
  }

  public void stopMonitoring() {
    if (Preconditions.isConnected(device)) {
      device.stop();
    }
  }

  public TGDevice getDevice() {
    return device;
  }
}
