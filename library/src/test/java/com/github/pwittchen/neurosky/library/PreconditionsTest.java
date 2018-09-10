package com.github.pwittchen.neurosky.library;

import android.bluetooth.BluetoothAdapter;
import com.neurosky.thinkgear.TGDevice;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
  public void bluetoothAdapterShouldBeInitializedWhenItsNotNull() {
    // when
    boolean isInitialized = Preconditions.isBluetoothAdapterInitialized(bluetoothAdapter);

    // then
    assertThat(isInitialized).isTrue();
  }

  @Test
  public void bluetoothAdapterShouldBeInitialized() {
    // when
    boolean isInitialized = Preconditions.isBluetoothAdapterInitialized();

    // then
    assertThat(isInitialized).isTrue();
  }

  @Test
  public void bluetoothAdapterShouldNotBeInitializedWhenItsNull() {
    // when
    boolean isInitialized = Preconditions.isBluetoothAdapterInitialized(null);

    // then
    assertThat(isInitialized).isFalse();
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

  @Test
  public void bluetoothShouldNotBeEnabledWhenAdapterIsNull() {
    // when
    boolean isEnabled = Preconditions.isBluetoothEnabled(null);

    // then
    assertThat(isEnabled).isFalse();
  }

  @Test
  public void constructorShouldBePrivate() throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {

    Constructor<Preconditions> constructor = Preconditions.class.getDeclaredConstructor();
    assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
    constructor.setAccessible(true);
    constructor.newInstance();
  }
}