package com.github.pwittchen.neurosky.library.exception;

public class BluetoothConnectingOrConnectedException extends RuntimeException {
  @Override public String getMessage() {
    return "device is already connecting or connected via Bluetooth";
  }
}
