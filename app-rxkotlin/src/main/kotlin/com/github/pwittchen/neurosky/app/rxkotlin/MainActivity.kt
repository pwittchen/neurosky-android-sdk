package com.github.pwittchen.neurosky.app.rxkotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.github.pwittchen.neurosky.library.NeuroSky
import com.github.pwittchen.neurosky.library.exception.BluetoothNotEnabledException
import com.github.pwittchen.neurosky.library.message.enums.BrainWave
import com.github.pwittchen.neurosky.library.message.enums.Signal
import com.github.pwittchen.neurosky.library.message.enums.State
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.btn_connect
import kotlinx.android.synthetic.main.activity_main.btn_disconnect
import kotlinx.android.synthetic.main.activity_main.btn_start_monitoring
import kotlinx.android.synthetic.main.activity_main.btn_stop_monitoring
import kotlinx.android.synthetic.main.activity_main.tv_attention
import kotlinx.android.synthetic.main.activity_main.tv_blink
import kotlinx.android.synthetic.main.activity_main.tv_meditation
import kotlinx.android.synthetic.main.activity_main.tv_state
import java.util.Locale

class MainActivity : AppCompatActivity() {

  companion object {
    const val LOG_TAG = "NeuroSky"
  }

  private lateinit var neuroSky: NeuroSky
  private lateinit var disposable: Disposable

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    neuroSky = NeuroSky()
    initButtonListeners()
  }

  private fun initButtonListeners() {
    btn_connect.setOnClickListener {
      neuroSky
          .connectCompletable()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              { showMessage("connecting") },
              { throwable ->
                showMessage(throwable.message.toString())
                Log.d(LOG_TAG, throwable.message)
              })
    }

    btn_disconnect.setOnClickListener {
      neuroSky
          .disconnectCompletable()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              { showMessage("disconnected") },
              { throwable -> showMessage(throwable.message.toString()) }
          )
    }

    btn_start_monitoring.setOnClickListener {
      neuroSky
          .startMonitoringCompletable()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              { showMessage("started monitoring...") },
              { throwable -> showMessage(throwable.message.toString()) }
          )
    }

    btn_stop_monitoring.setOnClickListener {
      neuroSky
          .stopMonitoringCompletable()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(
              { showMessage("stopped monitoring...") },
              { throwable -> showMessage(throwable.message.toString()) }
          )
    }
  }

  private fun showMessage(message: String) {
    Toast
        .makeText(this, message, Toast.LENGTH_SHORT)
        .show()
  }

  override fun onResume() {
    super.onResume()
    disposable = neuroSky
        .stream()
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          handleStateChange(it.state())
          handleSignalChange(it.signal())
          handleBrainWavesChange(it.brainWaves())
        }
  }

  override fun onPause() {
    super.onPause()
    if (!disposable.isDisposed) {
      disposable.dispose()
    }
  }

  private fun handleStateChange(state: State) {
    if (state == State.CONNECTED) {
      neuroSky.startMonitoring()
    }

    tv_state.text = state.toString()
    Log.d(LOG_TAG, state.toString())
  }

  private fun handleSignalChange(signal: Signal) {
    when (signal) {
      Signal.ATTENTION -> tv_attention.text = getFormattedMessage("attention: %d", signal)
      Signal.MEDITATION -> tv_meditation.text = getFormattedMessage("meditation: %d", signal)
      Signal.BLINK -> tv_blink.text = getFormattedMessage("blink: %d", signal)
    }

    Log.d(LOG_TAG, String.format("%s: %d", signal.toString(), signal.value))
  }

  private fun getFormattedMessage(
    messageFormat: String,
    signal: Signal
  ): String {
    return String.format(Locale.getDefault(), messageFormat, signal.value)
  }

  private fun handleBrainWavesChange(brainWaves: Set<BrainWave>) {

    if (brainWaves.isEmpty()) {
      return
    }

    val stringBuilder = StringBuilder()

    for (bw in brainWaves) {
      val message = String.format(Locale.getDefault(), "%s: %d", bw.toString(), bw.value)
      stringBuilder
          .append(message)
          .append(", ")
    }

    Log.d(LOG_TAG, stringBuilder.substring(0, stringBuilder.toString().length - 2))
  }
}
