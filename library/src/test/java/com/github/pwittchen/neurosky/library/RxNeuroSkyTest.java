package com.github.pwittchen.neurosky.library;

import com.github.pwittchen.neurosky.library.exception.BluetoothConnectingOrConnectedException;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotConnectedException;
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException;
import com.github.pwittchen.neurosky.library.listener.DeviceMessageListener;
import com.github.pwittchen.neurosky.library.validation.Preconditions;
import io.reactivex.observers.TestObserver;
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
public class RxNeuroSkyTest {

  @Mock
  private Preconditions preconditions;

  @Mock
  private DeviceMessageListener deviceMessageListener;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

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
    assertThat(throwable.getMessage()).isEqualTo(new BluetoothNotEnabledException().getMessage());
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldConnectToDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.canConnect(neuroSky.getDevice())).thenReturn(true);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.connect().subscribe(testObserver);

    // then
    testObserver.assertComplete();
    verify(neuroSky).openConnection();
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldNotConnectToDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.canConnect(neuroSky.getDevice())).thenReturn(false);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.connect().subscribe(testObserver);

    // then
    testObserver.assertError(BluetoothConnectingOrConnectedException.class);
    testObserver.assertErrorMessage(new BluetoothConnectingOrConnectedException().getMessage());
    verify(neuroSky, times(0)).openConnection();
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldDisconnectFromDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(true);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.disconnect().subscribe(testObserver);

    // then
    testObserver.assertComplete();
    verify(neuroSky).closeConnection();
    assertThat(neuroSky.getDevice()).isNotNull();
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldNotDisconnectFromDevice() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(false);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.disconnect().subscribe(testObserver);

    // then
    testObserver.assertError(BluetoothNotConnectedException.class);
    testObserver.assertErrorMessage(new BluetoothNotConnectedException().getMessage());
    verify(neuroSky, times(0)).closeConnection();
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldStartMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(true);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.start().subscribe(testObserver);

    // then
    testObserver.assertComplete();
    verify(neuroSky).startMonitoring();
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldNotStartMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(false);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.start().subscribe(testObserver);

    // then
    testObserver.assertError(BluetoothNotConnectedException.class);
    testObserver.assertErrorMessage(new BluetoothNotConnectedException().getMessage());
    verify(neuroSky, times(0)).startMonitoring();
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldStopMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(true);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.stop().subscribe(testObserver);

    // then
    testObserver.assertComplete();
    verify(neuroSky).stopMonitoring();
  }

  @Test
  @SuppressWarnings("unchecked call")
  public void shouldNotMonitoring() {
    // given
    when(preconditions.isBluetoothAdapterInitialized()).thenReturn(true);
    when(preconditions.isBluetoothEnabled()).thenReturn(true);
    RxNeuroSky neuroSky = spy(new RxNeuroSky(deviceMessageListener, preconditions));
    when(preconditions.isConnected(neuroSky.getDevice())).thenReturn(false);
    TestObserver testObserver = new TestObserver();

    // when
    neuroSky.stop().subscribe(testObserver);

    // then
    testObserver.assertError(BluetoothNotConnectedException.class);
    testObserver.assertErrorMessage(new BluetoothNotConnectedException().getMessage());
    verify(neuroSky, times(0)).stopMonitoring();
  }
}