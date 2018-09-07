/*
 * Copyright (C) 2018 Piotr Wittchen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.pwittchen.neurosky.library.listener;

import android.os.Message;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.neurosky.thinkgear.TGEegPower;
import java.util.HashSet;
import java.util.Set;

public abstract class ExtendedDeviceMessageListener implements DeviceMessageListener {

  @Override public void onMessageReceived(Message message) {
    State state = getState(message);
    if (state != State.UNKNOWN) {
      onStateChange(state);
    }

    Signal signal = getSignal(message);
    if (signal != Signal.UNKNOWN) {
      onSignalChange(signal);
    }

    Set<BrainWave> brainWaves = getBrainWaves(message);
    if (!brainWaves.isEmpty()) {
      onBrainWavesChange(brainWaves);
    }
  }

  public abstract void onStateChange(State state);

  public abstract void onSignalChange(Signal signal);

  public abstract void onBrainWavesChange(Set<BrainWave> brainWaves);

  Signal getSignal(Message message) {
    for (Signal signal : Signal.values()) {
      if (message.what == signal.getType()) {
        return signal.value(message.arg1);
      }
    }

    return Signal.UNKNOWN;
  }

  State getState(Message message) {
    if (message.what == Signal.STATE_CHANGE.getType()) {
      for (State state : State.values()) {
        if (message.arg1 == state.getType()) {
          return state;
        }
      }
    }

    return State.UNKNOWN;
  }

  Set<BrainWave> getBrainWaves(Message message) {
    final Set<BrainWave> brainWaves = new HashSet<>();

    if (message.what == Signal.EEG_POWER.getType()) {
      TGEegPower eegPower = (TGEegPower) message.obj;
      brainWaves.add(BrainWave.DELTA.value(eegPower.delta));
      brainWaves.add(BrainWave.THETA.value(eegPower.theta));
      brainWaves.add(BrainWave.LOW_ALPHA.value(eegPower.lowAlpha));
      brainWaves.add(BrainWave.HIGH_ALPHA.value(eegPower.highAlpha));
      brainWaves.add(BrainWave.LOW_BETA.value(eegPower.lowBeta));
      brainWaves.add(BrainWave.HIGH_BETA.value(eegPower.highBeta));
      brainWaves.add(BrainWave.LOW_GAMMA.value(eegPower.lowGamma));
      brainWaves.add(BrainWave.MID_GAMMA.value(eegPower.midGamma));
    }

    return brainWaves;
  }
}
