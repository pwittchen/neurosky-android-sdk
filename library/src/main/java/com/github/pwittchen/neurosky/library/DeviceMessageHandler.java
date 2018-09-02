package com.github.pwittchen.neurosky.library;

import android.os.Handler;
import android.os.Message;

//TODO: prepare another handler with more specific interface for different signals and states
public class DeviceMessageHandler extends Handler {

  private final DeviceMessageReceiver deviceMessageReceiver;

  DeviceMessageHandler(final DeviceMessageReceiver receiver) {
    super();
    this.deviceMessageReceiver = receiver;
  }

  @Override public void handleMessage(Message message) {
    // message.arg1 -> level
    // message.obj -> object
    //TODO: create sub-handlers for brainwaves and state change
    super.handleMessage(message);
    deviceMessageReceiver.onReceive(message);
  }
}
