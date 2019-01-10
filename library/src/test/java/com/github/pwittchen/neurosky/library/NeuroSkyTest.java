package com.github.pwittchen.neurosky.library;

import androidx.annotation.NonNull;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.github.pwittchen.neurosky.library.validation.Preconditions;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("PMD") // in test classes we can have many static imports
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class NeuroSkyTest {

  @Mock
  private DeviceMessageListener deviceMessageListener;

  @Mock
  private Preconditions preconditions;

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
    NeuroSky neuroSky = spy(new NeuroSky(createExtendedDeviceMessageListener()));

    // then
    assertThat(neuroSky).isNotNull();
    assertThat(neuroSky.getDevice()).isNotNull();
    assertThat(neuroSky.getHandler()).isNotNull();
  }

  @NonNull private DeviceMessageListener createExtendedDeviceMessageListener() {
    return new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
      }

      @Override public void onSignalChange(Signal signal) {
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
      }
    };
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

  @Test
  public void shouldInitializeHandlerAndDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);

    // when
    NeuroSky neuroSky = new NeuroSky(deviceMessageListener, preconditions);

    // then
    assertThat(neuroSky.getHandler()).isNotNull();
    assertThat(neuroSky.getDevice()).isNotNull();
  }

  @Test
  public void shouldNotInitializeHandlerAndDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(false);

    // when
    NeuroSky neuroSky = new NeuroSky(deviceMessageListener, preconditions);

    // then
    assertThat(neuroSky.getHandler()).isNull();
    assertThat(neuroSky.getDevice()).isNull();
  }

  @Test(expected = NullPointerException.class)
  public void shouldThrowAnExceptionWhenPreconditionsObjectIsNull() {
    new NeuroSky(deviceMessageListener, null);
  }

  @Test
  public void shouldConnectToDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.canConnect(neuroSky.getDevice())).thenReturn(true);

    // when
    neuroSky.connect();

    // then
    verify(neuroSky).openConnection();
  }

  @Test
  public void shouldNotConnectToDeviceWhenCannot() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.canConnect(neuroSky.getDevice())).thenReturn(false);

    // when
    neuroSky.connect();

    // then
    verify(neuroSky, times(0)).openConnection();
  }

  @Test
  public void shouldDisconnectFromDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(true);

    // when
    neuroSky.disconnect();

    // then
    verify(neuroSky).closeConnection();
    assertThat(neuroSky.getDevice()).isNotNull();
  }

  @Test
  public void shouldNotDisconnectFromDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(false);

    // when
    neuroSky.disconnect();

    // then
    verify(neuroSky, times(0)).closeConnection();
  }

  @Test
  public void shouldStartMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(true);

    // when
    neuroSky.start();

    // then
    verify(neuroSky).startMonitoring();
  }


  @Test
  public void shouldNotStartMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(false);

    // when
    neuroSky.start();

    // then
    verify(neuroSky, times(0)).startMonitoring();
  }

  @Test
  public void shouldStopMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(true);

    // when
    neuroSky.stop();

    // then
    verify(neuroSky).stopMonitoring();
  }

  @Test
  public void shouldNotStopMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    NeuroSky neuroSky = spy(new NeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(false);

    // when
    neuroSky.stop();

    // then
    verify(neuroSky, times(0)).stopMonitoring();
  }
}