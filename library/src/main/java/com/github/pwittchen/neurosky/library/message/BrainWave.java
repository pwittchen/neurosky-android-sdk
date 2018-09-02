package com.github.pwittchen.neurosky.library.message;

public enum BrainWave {
  DELTA(1),
  THETA(2),
  LOW_ALPHA(3),
  HIGH_ALPHA(4),
  LOW_BETA(5),
  HIGH_BETA(6),
  LOW_GAMMA(7),
  MID_GAMMA(7);

  private int type;
  private int value;

  BrainWave(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public int getValue() {
    return value;
  }

  public BrainWave value(int value) {
    this.value = value;
    return this;
  }
}
