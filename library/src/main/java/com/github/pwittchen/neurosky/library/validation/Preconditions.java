package com.github.pwittchen.neurosky.library.validation;

import android.bluetooth.BluetoothAdapter;
import com.neurosky.thinkgear.TGDevice;

public interface Preconditions {
  boolean isConnecting(TGDevice device);

  boolean isConnected(TGDevice device);

  boolean canConnect(TGDevice device);

  boolean isBluetoothAdapterInitialized();

  boolean isBluetoothAdapterInitialized(BluetoothAdapter bluetoothAdapter);

  boolean isBluetoothEnabled();

  boolean isBluetoothEnabled(BluetoothAdapter bluetoothAdapter);
}
