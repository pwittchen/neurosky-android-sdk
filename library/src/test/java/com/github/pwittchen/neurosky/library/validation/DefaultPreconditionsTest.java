package com.github.pwittchen.neurosky.library.validation;

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
public class DefaultPreconditionsTest {

  private Preconditions preconditions;

  @Mock
  private TGDevice tgDevice;

  @Mock
  private BluetoothAdapter bluetoothAdapter;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.preconditions = new DefaultPreconditions();
  }

  @Test
  public void shouldBeConnecting() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTING);

    // when
    boolean isConnecting = preconditions.isConnecting(tgDevice);

    // then
    assertThat(isConnecting).isTrue();
  }

  @Test
  public void shouldNotBeConnecting() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_DISCONNECTED);

    // when
    boolean isConnecting = preconditions.isConnecting(tgDevice);

    // then
    assertThat(isConnecting).isFalse();
  }

  @Test
  public void shouldNotBeConnectingWhenDeviceIsNull() {
    // when
    boolean isConnecting = preconditions.isConnecting(null);

    // then
    assertThat(isConnecting).isFalse();
  }

  @Test
  public void shouldBeConnected() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTED);

    // when
    boolean isConnected = preconditions.isConnected(tgDevice);

    // then
    assertThat(isConnected).isTrue();
  }

  @Test
  public void shouldNotBeConnected() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_DISCONNECTED);

    // when
    boolean isConnected = preconditions.isConnected(tgDevice);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void shouldNotBeConnectedWhenDeviceIsNull() {
    // when
    boolean isConnected = preconditions.isConnected(null);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void shouldBeAbleToConnect() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_DISCONNECTED);

    // when
    boolean isConnected = preconditions.canConnect(tgDevice);

    // then
    assertThat(isConnected).isTrue();
  }

  @Test
  public void shouldNotBeAbleToConnectWhenConnecting() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTING);

    // when
    boolean isConnected = preconditions.canConnect(tgDevice);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void shouldNotBeAbleToConnectWhenConnected() {
    // given
    when(tgDevice.getState()).thenReturn(TGDevice.STATE_CONNECTED);

    // when
    boolean isConnected = preconditions.canConnect(tgDevice);

    // then
    assertThat(isConnected).isFalse();
  }

  @Test
  public void bluetoothAdapterShouldBeInitializedWhenItsNotNull() {
    // when
    boolean isInitialized = preconditions.isBluetoothAdapterInitialized(bluetoothAdapter);

    // then
    assertThat(isInitialized).isTrue();
  }

  @Test
  public void bluetoothAdapterShouldBeInitialized() {
    // when
    boolean isInitialized = preconditions.isBluetoothAdapterInitialized();

    // then
    assertThat(isInitialized).isTrue();
  }

  @Test
  public void bluetoothAdapterShouldNotBeInitializedWhenItsNull() {
    // when
    boolean isInitialized = preconditions.isBluetoothAdapterInitialized(null);

    // then
    assertThat(isInitialized).isFalse();
  }

  @Test
  public void bluetoothShouldBeEnabled() {
    // given
    when(bluetoothAdapter.isEnabled()).thenReturn(true);

    // when
    boolean isEnabled = preconditions.isBluetoothEnabled(bluetoothAdapter);

    // then
    assertThat(isEnabled).isTrue();
  }

  @Test
  public void bluetoothShouldNotBeEnabled() {
    // given
    when(bluetoothAdapter.isEnabled()).thenReturn(false);

    // when
    boolean isEnabled = preconditions.isBluetoothEnabled(bluetoothAdapter);

    // then
    assertThat(isEnabled).isFalse();
  }

  @Test
  public void bluetoothShouldNotBeEnabledWhenAdapterIsNull() {
    // when
    boolean isEnabled = preconditions.isBluetoothEnabled(null);

    // then
    assertThat(isEnabled).isFalse();
  }
}