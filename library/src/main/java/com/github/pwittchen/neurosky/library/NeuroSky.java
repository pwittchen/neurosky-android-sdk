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

  public boolean isRawSignalEnabled() {
    return rawSignalEnabled;
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

  public DeviceMessageHandler getHandler() {
    return handler;
  }
}
