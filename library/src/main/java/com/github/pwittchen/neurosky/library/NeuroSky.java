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
import java.util.HashSet;
import java.util.Set;

public class NeuroSky {

  private final static EventBus eventBus = EventBus.create();
  private TGDevice device;
  private boolean rawSignalEnabled = false;
  private DeviceMessageHandler handler;

  public NeuroSky(final DeviceMessageListener listener) {
    if (Preconditions.isBluetoothAdapterInitialized()) {
      handler = new DeviceMessageHandler(listener);
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

  //public RxNeuroSky rx() {
  //  return new RxNeuroSky();
  //}
  //
  //public Flowable<BrainEvent> stream(BackpressureStrategy backpressureStrategy) {
  //  return eventBus.receive(backpressureStrategy);
  //}
  //
  //public Flowable<BrainEvent> stream() {
  //  return stream(BackpressureStrategy.BUFFER);
  //}

  public void connect() throws BluetoothNotEnabledException {
    if (!Preconditions.isBluetoothEnabled()) {
      throw new BluetoothNotEnabledException();
    }

    if (Preconditions.canConnect(device)) {
      device.connect(rawSignalEnabled);
    }
  }

  //public Completable connectCompletable() {
  //  return Completable.create(emitter -> {
  //    if (!Preconditions.isBluetoothEnabled()) {
  //      emitter.onError(new BluetoothNotEnabledException());
  //    }
  //
  //    if (Preconditions.canConnect(device)) {
  //      device.connect(rawSignalEnabled);
  //      emitter.onComplete();
  //    } else {
  //      emitter.onError(new BluetoothConnectingOrConnectedException());
  //    }
  //  });
  //}

  public void disconnect() {
    if (Preconditions.isConnected(device)) {
      device.close();
      device = null;
    }
  }

  //public Completable disconnectCompletable() {
  //  return Completable.create(emitter -> {
  //    if (Preconditions.isConnected(device)) {
  //      device.close();
  //      device = null;
  //      emitter.onComplete();
  //    } else {
  //      emitter.onError(new BluetoothNotConnectedException());
  //    }
  //  });
  //}

  public void enableRawSignal() {
    rawSignalEnabled = true;
  }

  public void disableRawSignal() {
    rawSignalEnabled = false;
  }

  public boolean isRawSignalEnabled() {
    return rawSignalEnabled;
  }

  public void startMonitoring() {
    if (Preconditions.isConnected(device)) {
      device.start();
    }
  }
  //
  //public Completable startMonitoringCompletable() {
  //  return Completable.create(emitter -> {
  //    if (Preconditions.isConnected(device)) {
  //      device.start();
  //      emitter.onComplete();
  //    } else {
  //      emitter.onError(new BluetoothNotConnectedException());
  //    }
  //  });
  //}

  public void stopMonitoring() {
    if (Preconditions.isConnected(device)) {
      device.stop();
    }
  }

  //public Completable stopMonitoringCompletable() {
  //  return Completable.create(emitter -> {
  //    if (Preconditions.isConnected(device)) {
  //      device.stop();
  //      emitter.onComplete();
  //    } else {
  //      emitter.onError(new BluetoothNotConnectedException());
  //    }
  //  });
  //}

  public TGDevice getDevice() {
    return device;
  }

  public DeviceMessageHandler getHandler() {
    return handler;
  }
}
