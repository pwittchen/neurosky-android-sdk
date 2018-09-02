package com.github.pwittchen.neurosky;

import android.bluetooth.BluetoothAdapter;
import android.support.annotation.Nullable;
import com.neurosky.thinkgear.TGDevice;

//TODO: create builder
//TODO: create rx methods
//TODO: organize gradle config
public class NeuroSky {
  private BluetoothAdapter bluetoothAdapter;
  private DeviceSignalHandler handler;
  private TGDevice device;
  private boolean rawSignalEnabled = false;

  public void init() {
    bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    if (bluetoothAdapter != null) {
      handler = new DeviceSignalHandler();
      device = new TGDevice(bluetoothAdapter, handler);
    }
  }

  public void connect() {
    if (!isConnecting() && !isConnected()) {
      device.connect(rawSignalEnabled);
    }
  }

  public void disconnect() {
    if (isConnected()) {
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
    if (isConnected()) {
      device.start();
    }
  }

  public void stopMonitoring() {
    if (isConnected()) {
      device.stop();
    }
  }

  @Nullable public TGDevice getDevice() {
    return device;
  }

  @Nullable public DeviceSignalHandler getHandler() {
    return handler;
  }

  //TODO: move methods below to separate Preconditions class

  private boolean isConnecting() {
    return device != null && device.getState() == TGDevice.STATE_CONNECTING;
  }

  private boolean isConnected() {
    return device != null && device.getState() == TGDevice.STATE_CONNECTED;
  }

  private boolean isBluetoothEnabled() {
    return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());
  }
}
