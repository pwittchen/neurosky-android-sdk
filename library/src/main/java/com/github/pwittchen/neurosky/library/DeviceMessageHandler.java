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

import android.os.Handler;
import android.os.Message;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;

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
