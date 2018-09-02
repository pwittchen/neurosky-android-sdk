package com.github.pwittchen.neurosky.library.listener;

import android.os.Message;

public interface DeviceMessageListener {
  void onMessageReceived(Message message);
}
