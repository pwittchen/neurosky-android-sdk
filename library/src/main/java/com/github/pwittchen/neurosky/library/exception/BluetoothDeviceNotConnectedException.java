package com.github.pwittchen.neurosky.library.exception;

public class BluetoothDeviceNotConnectedException extends RuntimeException {
  @Override public String getMessage() {
    return "bluetooth device is not connected";
  }
}
