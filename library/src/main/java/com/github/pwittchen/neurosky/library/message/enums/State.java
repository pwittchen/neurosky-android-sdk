package com.github.pwittchen.neurosky.library.message.enums;

import com.neurosky.thinkgear.TGDevice;

public enum State {
  UNKNOWN(-1),
  IDLE(TGDevice.STATE_IDLE),
  CONNECTING(TGDevice.STATE_CONNECTING),
  CONNECTED(TGDevice.STATE_CONNECTED),
  NOT_FOUND(TGDevice.STATE_NOT_FOUND),
  NOT_PAIRED(TGDevice.STATE_NOT_PAIRED),
  DISCONNECTED(TGDevice.STATE_DISCONNECTED);

  private int type;

  State(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }
}
