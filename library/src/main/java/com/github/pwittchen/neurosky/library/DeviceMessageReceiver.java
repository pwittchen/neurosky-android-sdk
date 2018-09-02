package com.github.pwittchen.neurosky.library;

import android.os.Message;

public interface DeviceMessageReceiver {
  void onReceive(Message message);
}
