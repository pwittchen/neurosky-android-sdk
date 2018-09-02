package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.neurosky.thinkgear.TGDevice;

//TODO: create builder
//TODO: create rx methods
//TODO: create enum representing device states, signals and brainwaves
public class NeuroSky {
  private DeviceSignalHandler handler;
  private TGDevice device;
  private boolean rawSignalEnabled = false;

  public void init() {
    if (Preconditions.isBluetoothAdapterInitialized()) {
      handler = new DeviceSignalHandler();
      device = new TGDevice(BluetoothAdapter.getDefaultAdapter(), handler);
    }
  }

  public void connect() {
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
}
