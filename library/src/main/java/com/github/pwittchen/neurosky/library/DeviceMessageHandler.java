package com.github.pwittchen.neurosky.library;

import android.os.Handler;
import android.os.Message;
import com.github.pwittchen.neurosky.library.listeners.BrainWaveListener;
import com.github.pwittchen.neurosky.library.listeners.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.listeners.SignalListener;
import com.github.pwittchen.neurosky.library.listeners.StateChangeListener;

//TODO: prepare another handler with more specific interface for different signals and states
public class DeviceMessageHandler extends Handler {

  private DeviceMessageListener listener;
  private SignalListener signalListener;
  private StateChangeListener stateChangeListener;
  private BrainWaveListener brainWaveListener;

  DeviceMessageHandler(final DeviceMessageListener deviceMessageListener) {
    this.listener = deviceMessageListener;
  }

  public DeviceMessageHandler(
      SignalListener signalListener,
      StateChangeListener stateChangeListener,
      BrainWaveListener brainWaveListener) {
    this.signalListener = signalListener;
    this.stateChangeListener = stateChangeListener;
    this.brainWaveListener = brainWaveListener;
  }

  @Override public void handleMessage(Message message) {
    // message.arg1 -> level
    // message.obj -> object
    //TODO: create sub-handlers for brainwaves and state change
    super.handleMessage(message);

    useListener(message);
    useSignalListener();
  }

  private void useSignalListener() {
    if (signalListener != null) {
      //TODO: implement
    }
  }

  private void useListener(Message message) {
    if (listener != null) {
      listener.listen(message);
    }
  }
}
