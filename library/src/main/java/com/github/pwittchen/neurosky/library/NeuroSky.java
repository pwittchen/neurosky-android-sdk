package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import android.os.Message;
import android.support.annotation.NonNull;
import com.neurosky.thinkgear.TGDevice;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

//TODO: create builder
//TODO: create rx methods
//TODO: create enum representing device states, signals and brainwaves
public class NeuroSky {
  private TGDevice device;
  private boolean rawSignalEnabled = false;

  public NeuroSky(final DeviceMessageReceiver receiver) {
    if (Preconditions.isBluetoothAdapterInitialized()) {
      DeviceMessageHandler handler = new DeviceMessageHandler(receiver);
      device = new TGDevice(BluetoothAdapter.getDefaultAdapter(), handler);
    }
  }

  public void connect() {
    if(Preconditions.isBluetoothEnabled()) {
      //TODO: display message about enabling bluetooth
      return;
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
}
