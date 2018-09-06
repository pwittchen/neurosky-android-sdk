package com.github.pwittchen.neurosky.library.exception;

public class BluetoothNotConnectedException extends RuntimeException {
  @Override public String getMessage() {
    return "Device is not connected via Bluetooth";
  }
}
