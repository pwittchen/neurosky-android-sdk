package com.github.pwittchen.neurosky.library;

import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class RxNeuroSkyTest {

  @Test
  public void shouldCreateRxNeuroSkyObject() {
    // when
    RxNeuroSky neuroSky = spy(new RxNeuroSky());

    // then
    assertThat(neuroSky).isNotNull();
    assertThat(neuroSky.getDevice()).isNotNull();
    assertThat(neuroSky.getHandler()).isNotNull();
  }

  @Test
  public void shouldEnableRawSignal() {
    // given
    RxNeuroSky neuroSky = new RxNeuroSky();

    // when
    neuroSky.enableRawSignal();

    // then
    assertThat(neuroSky.isRawSignalEnabled()).isTrue();
  }

  @Test
  public void shouldDisableRawSignal() {
    // given
    RxNeuroSky neuroSky = new RxNeuroSky();

    // when
    neuroSky.disableRawSignal();

    // then
    assertThat(neuroSky.isRawSignalEnabled()).isFalse();
  }

  @Test
  public void shouldThrowAnErrorWhenTryingToConnectToDeviceWithBluetoothDisabled() {
    // when
    RxNeuroSky neuroSky = new RxNeuroSky();

    // when
    Throwable throwable = neuroSky.connect().blockingGet();

    // then
    assertThat(throwable).isInstanceOf(BluetoothNotEnabledException.class);
  }
}