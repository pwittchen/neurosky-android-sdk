package com.github.pwittchen.neurosky.library;

import com.github.pwittchen.neurosky.library.message.BrainEvent;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import io.reactivex.BackpressureStrategy;
import io.reactivex.subscribers.TestSubscriber;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class EventBusTest {

  @Test
  public void shouldCreateBus() {
    // when
    EventBus bus = EventBus.create();

    // then
    assertThat(bus).isNotNull();
  }

  @Test
  public void constructorShouldBePrivate() throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {
    Constructor<EventBus> constructor = EventBus.class.getDeclaredConstructor();
    assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
    constructor.setAccessible(true);
    constructor.newInstance();
  }

  @Test
  public void shouldReceiveEvent() {
    // given
    TestSubscriber subscriber = new TestSubscriber();
    EventBus bus = EventBus.create();
    BrainEvent event = new BrainEvent(State.IDLE, Signal.UNKNOWN, new HashSet<>());

    // when
    bus.receive(BackpressureStrategy.BUFFER).subscribe(subscriber);

    // then
    bus.send(event);
    subscriber.assertValue(event);
  }
}