package com.github.pwittchen.neurosky.library.exception;

public class BluetoothNotEnabledException extends RuntimeException {
  @Override public String getMessage() {
    return "Bluetooth is not enabled";
  }
}
