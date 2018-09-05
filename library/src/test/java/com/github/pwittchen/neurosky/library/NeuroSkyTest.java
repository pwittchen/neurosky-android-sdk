package com.github.pwittchen.neurosky.library;

import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
public class NeuroSkyTest {

  @Mock
  private DeviceMessageListener deviceMessageListener;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldCreateNeuroSkyObject() {
    // when
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener));

    // then
    assertThat(neuroSky).isNotNull();
    assertThat(neuroSky.getDevice()).isNotNull();
    assertThat(neuroSky.getHandler()).isNotNull();
  }

  @Test
  public void shouldCreateNeuroSkyObjectWithImplicitListener() {
    // when
    NeuroSky neuroSky = spy(new NeuroSky());

    // then
    assertThat(neuroSky).isNotNull();
    assertThat(neuroSky.getDevice()).isNotNull();
    assertThat(neuroSky.getHandler()).isNotNull();
  }

  @Test
  public void shouldEnableRawSignal() {
    // when
    NeuroSky neuroSky = new NeuroSky(deviceMessageListener);
    neuroSky.enableRawSignal();

    // then
    assertThat(neuroSky.isRawSignalEnabled()).isTrue();
  }

  @Test
  public void shouldDisableRawSignal() {
    // when
    NeuroSky neuroSky = new NeuroSky(deviceMessageListener);
    neuroSky.disableRawSignal();

    // then
    assertThat(neuroSky.isRawSignalEnabled()).isFalse();
  }
}