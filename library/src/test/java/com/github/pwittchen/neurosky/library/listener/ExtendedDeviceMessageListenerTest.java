package com.github.pwittchen.neurosky.library.listener;

import android.os.Message;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.neurosky.thinkgear.TGEegPower;
import java.util.HashSet;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ExtendedDeviceMessageListenerTest {

  private ExtendedDeviceMessageListener listener;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);

    listener = spy(new ExtendedDeviceMessageListener() {
      @Override public void onStateChange(State state) {
      }

      @Override public void onSignalChange(Signal signal) {
      }

      @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
      }
    });
  }

  @Test
  public void shouldInvokeOnStateChange() {
    // given
    Message message = new Message();
    message.what = Signal.STATE_CHANGE.getType();
    message.arg1 = State.CONNECTING.getType();

    // when
    listener.onMessageReceived(message);

    // then
    verify(listener).onStateChange(State.CONNECTING);
  }

  @Test
  public void shouldInvokeOnSignalChange() {
    // given
    Signal expectedSignal = Signal.ATTENTION.value(52);
    Message message = new Message();
    message.what = Signal.ATTENTION.getType();
    message.arg1 = 52;

    // when
    listener.onMessageReceived(message);

    // then
    verify(listener).onSignalChange(expectedSignal);
  }

  @Test
  public void shouldInvokeOnBrainwavesChange() {
    // given
    Message message = new Message();
    message.what = Signal.EEG_POWER.getType();
    int delta = 1;
    int theta = 2;
    int lowAlpha = 3;
    int highAlpha = 4;
    int lowBeta = 5;
    int highBeta = 6;
    int lowGamma = 7;
    int midGamma = 8;
    message.obj = new TGEegPower(
        delta, theta, lowAlpha, highAlpha, lowBeta, highBeta, lowGamma, midGamma
    );
    Set<BrainWave> expectedBrainWaves = new HashSet<>();
    expectedBrainWaves.add(BrainWave.DELTA.value(delta));
    expectedBrainWaves.add(BrainWave.THETA.value(theta));
    expectedBrainWaves.add(BrainWave.LOW_ALPHA.value(lowAlpha));
    expectedBrainWaves.add(BrainWave.HIGH_ALPHA.value(highAlpha));
    expectedBrainWaves.add(BrainWave.LOW_BETA.value(lowBeta));
    expectedBrainWaves.add(BrainWave.HIGH_BETA.value(highBeta));
    expectedBrainWaves.add(BrainWave.LOW_GAMMA.value(lowGamma));
    expectedBrainWaves.add(BrainWave.MID_GAMMA.value(midGamma));

    // when
    listener.onMessageReceived(message);

    verify(listener).onBrainWavesChange(expectedBrainWaves);
    assertThat(expectedBrainWaves.size()).isEqualTo(8);
  }
}