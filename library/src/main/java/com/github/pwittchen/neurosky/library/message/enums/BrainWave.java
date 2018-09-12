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
package com.github.pwittchen.neurosky.library.message.enums;

public enum BrainWave {
  DELTA(1),
  THETA(2),
  LOW_ALPHA(3),
  HIGH_ALPHA(4),
  LOW_BETA(5),
  HIGH_BETA(6),
  LOW_GAMMA(7),
  MID_GAMMA(8);

  private int type;
  private int value;

  BrainWave(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }

  public int getValue() {
    return value;
  }

  public BrainWave value(int value) {
    this.value = value;
    return this;
  }
}
