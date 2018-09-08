package com.github.pwittchen.neurosky.library;

import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
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
  public void shouldCreateNeuroSkyObjectWithExtendedDeviceMessageListener() {
    // when
    NeuroSky neuroSky = spy(new NeuroSky(new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
      }

      @Override public void onSignalChange(Signal signal) {
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
      }
    }));

    // then
    assertThat(neuroSky).isNotNull();
    assertThat(neuroSky.getDevice()).isNotNull();
    assertThat(neuroSky.getHandler()).isNotNull();
  }

  @Test
  public void shouldEnableRawSignal() {
    // given
    NeuroSky neuroSky = new NeuroSky(deviceMessageListener);

    // when
    neuroSky.enableRawSignal();

    // then
    assertThat(neuroSky.isRawSignalEnabled()).isTrue();
  }

  @Test
  public void shouldDisableRawSignal() {
    // given
    NeuroSky neuroSky = new NeuroSky(deviceMessageListener);

    // when
    neuroSky.disableRawSignal();

    // then
    assertThat(neuroSky.isRawSignalEnabled()).isFalse();
  }

  @Test(expected = BluetoothNotEnabledException.class)
  public void shouldThrowAnExceptionWhenTryingToConnectToDeviceWithBluetoothDisabled() {
    // given
    NeuroSky neuroSky = new NeuroSky(deviceMessageListener);

    // when
    neuroSky.connect();

    // then an exception is thrown
  }
}