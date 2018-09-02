package com.github.pwittchen.neurosky.library.message;

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
