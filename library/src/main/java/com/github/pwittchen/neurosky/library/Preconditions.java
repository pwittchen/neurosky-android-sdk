package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.neurosky.thinkgear.TGDevice;

//TODO: prepare rx wrappers for this class
public class Preconditions {

  private Preconditions() {
  }

  public static boolean isConnecting(TGDevice device) {
    return device != null && device.getState() == TGDevice.STATE_CONNECTING;
  }

  public static boolean isConnected(TGDevice device) {
    return device != null && device.getState() == TGDevice.STATE_CONNECTED;
  }

  public static boolean canConnect(TGDevice device) {
    return !isConnecting(device) && !isConnected(device);
  }

  public static boolean isBluetoothAdapterInitialized() {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    return bluetoothAdapter != null;
  }

  public static boolean isBluetoothEnabled() {
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());
  }
}
