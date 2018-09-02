package com.github.pwittchen.neurosky.library;

import android.os.Handler;
import android.os.Message;
import com.github.pwittchen.neurosky.library.listeners.DeviceMessageListener;

public class DeviceMessageHandler extends Handler {

  private DeviceMessageListener listener;

  DeviceMessageHandler(final DeviceMessageListener deviceMessageListener) {
    this.listener = deviceMessageListener;
  }

  @Override public void handleMessage(Message message) {
    super.handleMessage(message);

    if (listener != null) {
      listener.onMessageReceived(message);
    }
  }
}
