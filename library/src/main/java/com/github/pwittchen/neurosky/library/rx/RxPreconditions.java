package com.github.pwittchen.neurosky.library.rx;

import com.github.pwittchen.neurosky.library.Preconditions;
import com.neurosky.thinkgear.TGDevice;
import io.reactivex.Single;

public class RxPreconditions {
  private RxPreconditions() {
  }

  public static Single<Boolean> isConnecting(TGDevice device) {
    return Single.just(Preconditions.isConnecting(device));
  }

  public static Single<Boolean> isConnected(TGDevice device) {
    return Single.just(Preconditions.isConnected(device));
  }

  public static Single<Boolean> canConnect(TGDevice device) {
    return Single.just(Preconditions.canConnect(device));
  }

  public static Single<Boolean> isBluetoothAdapterInitialized() {
    return Single.just(Preconditions.isBluetoothAdapterInitialized());
  }

  public static Single<Boolean> isBluetoothEnabled() {
    return Single.just(Preconditions.isBluetoothEnabled());
  }
}
