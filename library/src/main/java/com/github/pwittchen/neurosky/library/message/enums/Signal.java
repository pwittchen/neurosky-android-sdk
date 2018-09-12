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
package com.github.pwittchen.neurosky.library.message.enums;

import com.neurosky.thinkgear.TGDevice;

public enum Signal {
  UNKNOWN(-1),
  STATE_CHANGE(TGDevice.MSG_STATE_CHANGE),
  POOR_SIGNAL(TGDevice.MSG_POOR_SIGNAL),
  ATTENTION(TGDevice.MSG_ATTENTION),
  MEDITATION(TGDevice.MSG_MEDITATION),
  BLINK(TGDevice.MSG_BLINK),
  SLEEP_STAGE(TGDevice.MSG_SLEEP_STAGE),
  LOW_BATTERY(TGDevice.MSG_LOW_BATTERY),
  RAW_COUNT(TGDevice.MSG_RAW_COUNT),
  RAW_DATA(TGDevice.MSG_RAW_DATA),
  HEART_RATE(TGDevice.MSG_HEART_RATE),
  RAW_MULTI(TGDevice.MSG_RAW_MULTI),
  EEG_POWER(TGDevice.MSG_EEG_POWER);

  private int type;
  private int value;

  Signal(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public int getValue() {
    return value;
  }

  public Signal value(int value) {
    this.value = value;
    return this;
  }
}
