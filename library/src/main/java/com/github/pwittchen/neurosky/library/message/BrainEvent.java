package com.github.pwittchen.neurosky.library.message;

import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.Set;

public class BrainEvent {
  private final State state;
  private final Signal signal;
  private final Set<BrainWave> brainWaves;

  public BrainEvent(final State state, final Signal signal, final Set<BrainWave> brainWaves) {
    this.state = state;
    this.signal = signal;
    this.brainWaves = brainWaves;
  }

  public State state() {
    return state;
  }

  public Signal signal() {
    return signal;
  }

  public Set<BrainWave> brainWaves() {
    return brainWaves;
  }
}
