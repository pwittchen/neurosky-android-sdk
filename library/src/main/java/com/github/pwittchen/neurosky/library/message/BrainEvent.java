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
package com.github.pwittchen.neurosky.library.message;

import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.Set;

@SuppressWarnings("PMD") // I want to have the same methods names as field names
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
