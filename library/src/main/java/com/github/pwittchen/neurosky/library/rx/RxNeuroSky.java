package com.github.pwittchen.neurosky.library.rx;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainWave;
import com.github.pwittchen.neurosky.library.message.Signal;
import com.github.pwittchen.neurosky.library.message.State;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.functions.Consumer;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//TODO: add proper validation with RxPreconditions
public class RxNeuroSky {

  private NeuroSky neuroSky;

  public static RxNeuroSky create() {
    return new RxNeuroSky();
  }

  private RxNeuroSky() {
    this.neuroSky = new NeuroSky(createListener());
  }

  @NonNull protected ExtendedDeviceMessageListener createListener() {
    return new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
        //TODO: handle this! Maybe create a global emitter per each stream?
      }

      @Override public void onSignalChange(Signal signal) {
        //TODO: handle this!
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
        //TODO: handle this!
      }
    };
  }

  public Flowable<State> streamState() {
    return streamState(BackpressureStrategy.BUFFER);
  }

  public Flowable<State> streamState(BackpressureStrategy backpressureStrategy) {
    return Flowable.create(new FlowableOnSubscribe<State>() {
      @Override public void subscribe(final FlowableEmitter<State> emitter) {
        //TODO: implement
      }
    }, backpressureStrategy);
  }

  public Flowable<Signal> streamSignal() {
    return streamSignal(BackpressureStrategy.BUFFER);
  }

  public Flowable<Signal> streamSignal(BackpressureStrategy backpressureStrategy) {
    return Flowable.create(new FlowableOnSubscribe<Signal>() {
      @Override public void subscribe(FlowableEmitter<Signal> emitter) {
        //TODO: implement
      }
    }, backpressureStrategy);
  }

  public Flowable<Set<BrainWave>> streamBrainWaves() {
    return streamBrainWaves(BackpressureStrategy.BUFFER);
  }

  public Flowable<Set<BrainWave>> streamBrainWaves(BackpressureStrategy backpressureStrategy) {
    return Flowable.create(new FlowableOnSubscribe<Set<BrainWave>>() {
      @Override public void subscribe(FlowableEmitter<Set<BrainWave>> emitter) {
        //TODO: implement
      }
    }, backpressureStrategy);
  }

  public Completable connect() {
    return Completable.create(new CompletableOnSubscribe() {
      @Override public void subscribe(CompletableEmitter emitter) {
        try {
          neuroSky.connect();
          //TODO: add observing of the connection state and emit onComplete when ready
          emitter.onComplete(); //TODO: should be emitted after establishing connection
        } catch (BluetoothNotEnabledException e) {
          emitter.tryOnError(e);
        }
      }
    });
  }

  public Completable disconnect() {
    return Completable.create(new CompletableOnSubscribe() {
      @Override public void subscribe(CompletableEmitter emitter) throws Exception {
        //TODO: add observing of the connection state and emit onComplete when ready
        neuroSky.disconnect(); //TODO: should be emitted after quiting connection
      }
    });
  }

  public Completable startMonitoring() {
    return Completable.create(new CompletableOnSubscribe() {
      @Override public void subscribe(CompletableEmitter emitter) throws Exception {
        //TODO: add observing of the connection state and emit onComplete when ready
        neuroSky.startMonitoring(); //TODO: should be emitted after quiting connection
      }
    });
  }

  public Completable stopMonitoring() {
    return Completable.create(new CompletableOnSubscribe() {
      @Override public void subscribe(CompletableEmitter emitter) throws Exception {
        //TODO: add observing of the connection state and emit onComplete when ready
        neuroSky.stopMonitoring(); //TODO: should be emitted after quiting connection
      }
    });
  }
}
