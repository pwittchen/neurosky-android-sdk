package com.github.pwittchen.neurosky.library.exception;

public class BluetoothDeviceIsConnectingOrConnectedException extends RuntimeException {
  @Override public String getMessage() {
    return "Bluetooth device is connecting or connnected";
  }
}
