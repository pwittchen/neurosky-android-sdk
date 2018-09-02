package com.github.pwittchen.neurosky.library.listeners;

public interface SignalListener extends DeviceMessageListener {
  void onStateChange(int value);

  void onPoorSignal();

  void onAttention(int value);

  void onMeditation(int value);

  void onBlink(int value);

  void onSleepStage();

  void onLowBattery();

  void onRawCount();

  void onRawData(int value);

  void onHeartRate(int value);

  void onRawMulti(int value);

  void onEegPower(int value);
}
