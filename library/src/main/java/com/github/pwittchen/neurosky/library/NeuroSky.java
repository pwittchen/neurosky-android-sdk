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
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.validation.DefaultPreconditions;
import com.github.pwittchen.neurosky.library.validation.Preconditions;
import com.neurosky.thinkgear.TGDevice;

public class NeuroSky {

  private boolean rawSignalEnabled = false;
  private TGDevice device;
  private DeviceMessageHandler handler;
  private Preconditions preconditions;

  public NeuroSky(final DeviceMessageListener listener) {
    this(listener, new DefaultPreconditions());
  }

  protected NeuroSky(final DeviceMessageListener listener, @NonNull Preconditions preconditions) {
    this.preconditions = preconditions;
    if (preconditions.isBluetoothAdapterInitialized()) {
      handler = new DeviceMessageHandler(listener);
      device = new TGDevice(BluetoothAdapter.getDefaultAdapter(), handler);
    }
  }

  public void connect() throws BluetoothNotEnabledException {
    if (!preconditions.isBluetoothEnabled()) {
      throw new BluetoothNotEnabledException();
    }

    if (canConnect()) {
      openConnection();
    }
  }

  protected void openConnection() {
    device.connect(rawSignalEnabled);
  }

  public void disconnect() {
    if (isConnected()) {
      closeConnection();
    }
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

  public void start() {
    if (isConnected()) {
      startMonitoring();
    }
  }

  protected void startMonitoring() {
    device.start();
  }

  public void stop() {
    if (isConnected()) {
      stopMonitoring();
    }
  }

  protected void stopMonitoring() {
    device.stop();
  }

  public boolean canConnect() {
    return preconditions.canConnect(device);
  }

  public boolean isConnected() {
    return preconditions.isConnected(device);
  }

  public boolean isConnecting() {
    return preconditions.isConnecting(device);
  }

  public TGDevice getDevice() {
    return device;
  }

  public DeviceMessageHandler getHandler() {
    return handler;
  }
}
