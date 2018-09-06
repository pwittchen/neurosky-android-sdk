package com.github.pwittchen.neurosky.library.message;

import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.HashSet;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class BrainEventTest {

  @Test
  public void shouldCreateBrainEvent() {
    // when
    BrainEvent brainEvent = new BrainEvent(State.IDLE, Signal.UNKNOWN, new HashSet<>());

    // then
    assertThat(brainEvent).isNotNull();
    assertThat(brainEvent.state()).isEqualTo(State.IDLE);
    assertThat(brainEvent.signal()).isEqualTo(Signal.UNKNOWN);
    assertThat(brainEvent.brainWaves()).isEmpty();
  }
}