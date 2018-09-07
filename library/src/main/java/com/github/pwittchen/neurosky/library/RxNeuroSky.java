package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.github.pwittchen.neurosky.library.exception.BluetoothConnectingOrConnectedException;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotConnectedException;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainEvent;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.neurosky.thinkgear.TGDevice;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import java.util.HashSet;
import java.util.Set;

public class RxNeuroSky {
  private final static EventBus eventBus = EventBus.create();
  private TGDevice device;
  private boolean rawSignalEnabled = false;
  private DeviceMessageHandler handler;

  private RxNeuroSky(final DeviceMessageListener listener) {
    if (Preconditions.isBluetoothAdapterInitialized()) {
      handler = new DeviceMessageHandler(listener);
      device = new TGDevice(BluetoothAdapter.getDefaultAdapter(), handler);
    }
  }

  public RxNeuroSky() {
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
    return stream(BackpressureStrategy.BUFFER);
  }

  public Completable connect() {
    return Completable.create(emitter -> {
      if (!Preconditions.isBluetoothEnabled()) {
        emitter.onError(new BluetoothNotEnabledException());
      }

      if (Preconditions.canConnect(device)) {
        device.connect(rawSignalEnabled);
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothConnectingOrConnectedException());
      }
    });
  }

  public Completable disconnect() {
    return Completable.create(emitter -> {
      if (Preconditions.isConnected(device)) {
        device.close();
        device = null;
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothNotConnectedException());
      }
    });
  }

  public void enableRawSignal() {
    rawSignalEnabled = true;
  }

  public void disableRawSignal() {
    rawSignalEnabled = false;
  }

  public boolean isRawSignalEnabled() {
    return rawSignalEnabled;
  }

  public Completable startMonitoring() {
    return Completable.create(emitter -> {
      if (Preconditions.isConnected(device)) {
        device.start();
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothNotConnectedException());
      }
    });
  }

  public Completable stopMonitoring() {
    return Completable.create(emitter -> {
      if (Preconditions.isConnected(device)) {
        device.stop();
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothNotConnectedException());
      }
    });
  }

  public TGDevice getDevice() {
    return device;
  }

  public DeviceMessageHandler getHandler() {
    return handler;
  }
}
