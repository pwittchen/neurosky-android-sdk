/*
 * Copyright (C) 2018 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import androidx.annotation.NonNull;
import com.github.pwittchen.neurosky.library.exception.BluetoothConnectingOrConnectedException;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotConnectedException;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainEvent;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.github.pwittchen.neurosky.library.validation.DefaultPreconditions;
import com.github.pwittchen.neurosky.library.validation.Preconditions;
import com.neurosky.thinkgear.TGDevice;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import java.util.HashSet;
import java.util.Set;

public class RxNeuroSky {
  private final static EventBus eventBus = EventBus.create();
  private boolean rawSignalEnabled = false;
  private TGDevice device;
  private DeviceMessageHandler handler;
  private Preconditions preconditions;

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

  protected RxNeuroSky(final DeviceMessageListener listener) {
    this(listener, new DefaultPreconditions());
  }

  protected RxNeuroSky(final DeviceMessageListener listener, @NonNull Preconditions preconditions) {
    this.preconditions = preconditions;
    if (preconditions.isBluetoothAdapterInitialized()) {
      handler = new DeviceMessageHandler(listener);
      device = new TGDevice(BluetoothAdapter.getDefaultAdapter(), handler);
    }
  }

  public Flowable<BrainEvent> stream(BackpressureStrategy backpressureStrategy) {
    return eventBus.receive(backpressureStrategy);
  }

  public Flowable<BrainEvent> stream() {
    return stream(BackpressureStrategy.BUFFER);
  }

  public Completable connect() {
    return Completable.create(emitter -> {
      if (!preconditions.isBluetoothEnabled()) {
        emitter.onError(new BluetoothNotEnabledException());
      }

      if (preconditions.canConnect(device)) {
        openConnection();
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothConnectingOrConnectedException());
      }
    });
  }

  protected void openConnection() {
    device.connect(rawSignalEnabled);
  }

  public Completable disconnect() {
    return Completable.create(emitter -> {
      if (preconditions.isConnected(device)) {
        closeConnection();
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothNotConnectedException());
      }
    });
  }

  protected void closeConnection() {
    device.close();
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

  public Completable start() {
    return Completable.create(emitter -> {
      if (preconditions.isConnected(device)) {
        startMonitoring();
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothNotConnectedException());
      }
    });
  }

  protected void startMonitoring() {
    device.start();
  }

  public Completable stop() {
    return Completable.create(emitter -> {
      if (preconditions.isConnected(device)) {
        stopMonitoring();
        emitter.onComplete();
      } else {
        emitter.onError(new BluetoothNotConnectedException());
      }
    });
  }

  protected void stopMonitoring() {
    device.stop();
  }

  public TGDevice getDevice() {
    return device;
  }

  public DeviceMessageHandler getHandler() {
    return handler;
  }
}
