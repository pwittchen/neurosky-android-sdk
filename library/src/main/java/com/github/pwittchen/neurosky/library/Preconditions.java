/*
 * Copyright (C) 2018 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.neurosky.thinkgear.TGDevice;

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
    return isBluetoothAdapterInitialized(BluetoothAdapter.getDefaultAdapter());
  }

  static boolean isBluetoothAdapterInitialized(BluetoothAdapter bluetoothAdapter) {
    return bluetoothAdapter != null;
  }

  public static boolean isBluetoothEnabled() {
    return isBluetoothEnabled(BluetoothAdapter.getDefaultAdapter());
  }

  static boolean isBluetoothEnabled(BluetoothAdapter bluetoothAdapter) {
    return (bluetoothAdapter != null && bluetoothAdapter.isEnabled());
  }
}
