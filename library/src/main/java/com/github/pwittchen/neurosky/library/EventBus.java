package com.github.pwittchen.neurosky.library;

import com.github.pwittchen.neurosky.library.message.BrainEvent;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

class EventBus {

  private final Subject<Object> bus = PublishSubject.create().toSerialized();

  public static EventBus create() {
    return new EventBus();
  }

  public void send(final BrainEvent object) {
    bus.onNext(object);
  }

  @SuppressWarnings("unchecked")
  public Flowable<BrainEvent> receive(BackpressureStrategy backpressureStrategy) {
    return (Flowable<BrainEvent>) (Flowable<?>) bus
        .toFlowable(backpressureStrategy)
        .filter(object -> object instanceof BrainEvent);
  }
}
