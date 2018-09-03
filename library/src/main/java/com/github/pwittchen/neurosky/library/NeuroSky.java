package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.rx.RxNeuroSky;
import com.neurosky.thinkgear.TGDevice;

public class NeuroSky {

  private TGDevice device;
  private boolean rawSignalEnabled = false;

  public NeuroSky(final DeviceMessageListener listener) {
    if (Preconditions.isBluetoothAdapterInitialized()) {
      DeviceMessageHandler handler = new DeviceMessageHandler(listener);
      device = new TGDevice(BluetoothAdapter.getDefaultAdapter(), handler);
    }
  }

  public static RxNeuroSky rx() {
    return RxNeuroSky.create();
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
