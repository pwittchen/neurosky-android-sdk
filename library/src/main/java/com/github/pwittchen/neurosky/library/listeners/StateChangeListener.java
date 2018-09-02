package com.github.pwittchen.neurosky.library.listeners;

public interface StateChangeListener {
  void onIdle();

  void onConnecting();

  void onConnected();

  void onNotFound();

  void onNotPaired();

  void onDisconnected();
}
