package com.github.pwittchen.neurosky.library;

import android.os.Handler;
import android.os.Message;
import com.github.pwittchen.neurosky.library.listeners.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.BrainWave;
import com.github.pwittchen.neurosky.library.message.Signal;
import com.github.pwittchen.neurosky.library.message.State;
import com.neurosky.thinkgear.TGEegPower;
import java.util.HashSet;
import java.util.Set;

//TODO: prepare another handler with more specific interface for different signals and states
public class DeviceMessageHandler extends Handler {

  private DeviceMessageListener listener;

  DeviceMessageHandler(final DeviceMessageListener deviceMessageListener) {
    this.listener = deviceMessageListener;
  }

  @Override public void handleMessage(Message message) {
    super.handleMessage(message);

    if (listener != null) {
      listener.listen(message);
    }

    //TODO: utilize values below - e.g. create an object containing Signal, State and BrainWave
    //getSignal(message);
    //getState(message);
    //getBrainWaves(message);
  }

  private Signal getSignal(Message message) {
    for (Signal signal : Signal.values()) {
      if (message.what == signal.getType()) {
        return signal.value(message.arg1);
      }
    }

    return Signal.UNKNOWN;
  }

  private State getState(Message message) {
    if (message.what == Signal.STATE_CHANGE.getType()) {
      for (State state : State.values()) {
        if (message.arg1 == state.getType()) {
          return state;
        }
      }
    }
    return State.UNKNOWN;
  }

  private Set<BrainWave> getBrainWaves(Message message) {
    Set<BrainWave> brainWaves = new HashSet<>();

    if (message.what == Signal.EEG_POWER.getType()) {
      TGEegPower eegPower = (TGEegPower) message.obj;
      brainWaves.add(BrainWave.DELTA.value(eegPower.delta));
      brainWaves.add(BrainWave.THETA.value(eegPower.theta));
      brainWaves.add(BrainWave.LOW_ALPHA.value(eegPower.lowAlpha));
      brainWaves.add(BrainWave.HIGH_ALPHA.value(eegPower.highAlpha));
      brainWaves.add(BrainWave.LOW_BETA.value(eegPower.lowBeta));
      brainWaves.add(BrainWave.LOW_GAMMA.value(eegPower.lowGamma));
      brainWaves.add(BrainWave.MID_GAMMA.value(eegPower.midGamma));
    }

    return brainWaves;
  }
}
