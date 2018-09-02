package com.github.pwittchen.neurosky.library.listeners;

public interface BrainWaveListener {
  void onDelta(int value);

  void onTheta(int value);

  void onLowAlpha(int value);

  void onHighAlpha(int value);

  void onLowBeta(int value);

  void onHighBeta(int value);

  void onLowGamma(int value);

  void onMidGamma(int value);
}
