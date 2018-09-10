package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.neurosky.thinkgear.TGDevice;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class PreconditionsTest {

  @Mock
  private TGDevice tgDevice;

  @Mock
  private BluetoothAdapter bluetoothAdapter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldBeConnecting() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTING);

    // when
    boolean isConnecting = Preconditions.isConnecting(tgDevice);

    // then
    assertThat(isConnecting).isTrue();
  }

  @Test
  public void shouldNotBeConnecting() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_DISCONNECTED);

    // when
    boolean isConnecting = Preconditions.isConnecting(tgDevice);

    // then
    assertThat(isConnecting).isFalse();
  }

  @Test
  public void shouldNotBeConnectingWhenDeviceIsNull() {
    // when
    boolean isConnecting = Preconditions.isConnecting(null);

    // then
    assertThat(isConnecting).isFalse();
  }

  @Test
  public void shouldBeConnected() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTED);

    // when
    boolean isConnected = Preconditions.isConnected(tgDevice);

    // then
    assertThat(isConnected).isTrue();
  }

  @Test
  public void shouldNotBeConnected() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_DISCONNECTED);

    // when
    boolean isConnected = Preconditions.isConnected(tgDevice);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void shouldNotBeConnectedWhenDeviceIsNull() {
    // when
    boolean isConnected = Preconditions.isConnected(null);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void shouldBeAbleToConnect() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_DISCONNECTED);

    // when
    boolean isConnected = Preconditions.canConnect(tgDevice);

    // then
    assertThat(isConnected).isTrue();
  }

  @Test
  public void shouldNotBeAbleToConnectWhenConnecting() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTING);

    // when
    boolean isConnected = Preconditions.canConnect(tgDevice);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void shouldNotBeAbleToConnectWhenConnected() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTED);

    // when
    boolean isConnected = Preconditions.canConnect(tgDevice);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void bluetoothAdapterShouldBeInitialized() {
    // when
    boolean isInitialized = Preconditions.isBluetoothAdapterInitialized();

    // then
    assertThat(isInitialized).isTrue();
  }

  @Test
  public void bluetoothShouldBeEnabled() {
    // given
    when(bluetoothAdapter.isEnabled()).thenReturn(true);

    // when
    boolean isEnabled = Preconditions.isBluetoothEnabled(bluetoothAdapter);

    // then
    assertThat(isEnabled).isTrue();
  }

  @Test
  public void bluetoothShouldNotBeEnabled() {
    // given
    when(bluetoothAdapter.isEnabled()).thenReturn(false);

    // when
    boolean isEnabled = Preconditions.isBluetoothEnabled(bluetoothAdapter);

    // then
    assertThat(isEnabled).isFalse();
  }
}