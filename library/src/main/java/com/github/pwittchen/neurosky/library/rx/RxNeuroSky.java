package com.github.pwittchen.neurosky.library.rx;

import android.support.annotation.NonNull;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.Preconditions;
import com.github.pwittchen.neurosky.library.exception.BluetoothDeviceIsConnectingOrConnectedException;
import com.github.pwittchen.neurosky.library.exception.BluetoothDeviceNotConnectedException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainWave;
import com.github.pwittchen.neurosky.library.message.Signal;
import com.github.pwittchen.neurosky.library.message.State;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Emitter;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//TODO: add proper validation with RxPreconditions
public class RxNeuroSky {

  private NeuroSky neuroSky;
  private Emitter<State> stateEmitter;
  private Emitter<Signal> signalEmitter;
  private Emitter<Set<BrainWave>> brainWavesEmitter;

  public static RxNeuroSky create() {
    return new RxNeuroSky();
  }

  private RxNeuroSky() {
    this.neuroSky = new NeuroSky(createListener());
    stateEmitter = createEmitter();
    signalEmitter = createEmitter();
    brainWavesEmitter = createEmitter();
  }

  @NonNull private Emitter createEmitter() {
    return new Emitter() {
      @Override public void onNext(Object value) {
      }

      @Override public void onError(Throwable error) {
      }

      @Override public void onComplete() {
      }
    };
  }

  @NonNull protected ExtendedDeviceMessageListener createListener() {
    return new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
        stateEmitter.onNext(state);
      }

      @Override public void onSignalChange(Signal signal) {
        signalEmitter.onNext(signal);
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
        brainWavesEmitter.onNext(brainWaves);
      }
    };
  }

  public Flowable<State> streamState() {
    return streamState(BackpressureStrategy.BUFFER);
  }

  public Flowable<State> streamState(BackpressureStrategy backpressureStrategy) {
    return Flowable.create(
        (FlowableOnSubscribe<State>) stateEmitter,
        backpressureStrategy
    );
  }

  public Flowable<Signal> streamSignal() {
    return streamSignal(BackpressureStrategy.BUFFER);
  }

  public Flowable<Signal> streamSignal(BackpressureStrategy backpressureStrategy) {
    return
        RxPreconditions
            .isConnected(neuroSky.getDevice())
            .toFlowable()
            .switchMap(isConnected -> {
              if (isConnected) {
                return Flowable.create(
                    (FlowableOnSubscribe<Signal>) signalEmitter,
                    backpressureStrategy
                );
              } else {
                return Flowable.error(new BluetoothDeviceNotConnectedException());
              }
            });
  }

  public Flowable<Set<BrainWave>> streamBrainWaves() {
    return streamBrainWaves(BackpressureStrategy.BUFFER);
  }

  public Flowable<Set<BrainWave>> streamBrainWaves(BackpressureStrategy backpressureStrategy) {
    return
        RxPreconditions
            .isConnected(neuroSky.getDevice())
            .toFlowable()
            .switchMap(isConnected -> {
              if (isConnected) {
                return Flowable.create(
                    (FlowableOnSubscribe<Set<BrainWave>>) brainWavesEmitter,
                    backpressureStrategy
                );
              } else {
                return Flowable.error(new BluetoothDeviceNotConnectedException());
              }
            });
  }

  public Completable connect() {
    if (Preconditions.canConnect(neuroSky.getDevice())) {
      return Completable.error(new BluetoothDeviceIsConnectingOrConnectedException());
    }

    return Completable.create(emitter ->
        Completable
            .fromRunnable(() -> neuroSky.connect())
            .toFlowable()
            .filter(state -> state.equals(State.CONNECTED))
            .timeout(30, TimeUnit.SECONDS)
            .subscribe(
                state -> emitter.onComplete(),
                emitter::tryOnError
            )
    );
  }

  public Completable disconnect() {
    return Completable.create(emitter ->
        Completable
            .fromRunnable(() -> neuroSky.disconnect())
            .toFlowable()
            .switchMap(o -> streamState())
            .filter(state -> state.equals(State.DISCONNECTED))
            .timeout(30, TimeUnit.SECONDS)
            .subscribe(
                state -> emitter.onComplete(),
                emitter::tryOnError
            )
    );
  }

  public Completable startMonitoring() {
    return Completable.create(emitter ->
        RxPreconditions
            .isConnected(neuroSky.getDevice())
            .subscribe(isConnected -> {
              if (isConnected) {
                neuroSky.startMonitoring();
                emitter.onComplete();
              } else {
                emitter.onError(new BluetoothDeviceNotConnectedException());
              }
            }, emitter::onError)
    );
  }

  public Completable stopMonitoring() {
    return Completable.create(emitter -> {
      RxPreconditions
          .isConnected(neuroSky.getDevice())
          .subscribe(isConnected -> {
            if (isConnected) {
              neuroSky.stopMonitoring();
              emitter.onComplete();
            } else {
              emitter.onError(new BluetoothDeviceNotConnectedException());
            }
          }, emitter::onError);
    });
  }
}
