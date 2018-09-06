NeuroSky Android SDK
=====================

NeuroSky MindWave Mobile Android SDK

This SDK allows you to write mobile Android apps interacting with the brain via [NeuroSky](http://neurosky.com/) MindWave Mobile device connected to the phone or tablet via Bluetooth. You can write apps controlled by your brain or perform data acquisition and analysis of the brain signals (attention, meditation, brain waves, raw signal) and eye blinks. NeuroSky uses EEG technology for gathering brain data and EMG sensor for detecting blinks.

**Please note**: this SDK is not a product of NeuroSky company, but it depends on its software and hardware

**This project is not production-ready yet!**

Contents
--------

- [Usage](#usage)
  - [Java](#java)
    - [Listener](#listener)
    - [RxJava](#rxjava)
  - [Kotlin](#kotlin)
    - [Listener](#listener-1)
    - [RxKotlin](#rxkotlin)
- [Examples](#examples)
- [Download](#download)
- [Tests](#tests)
- [Code style](#code-style)
- [Static code analysis](#static-code-analysis)
- [JavaDoc](#javadoc)
- [Changelog](#changelog)
- [Releasing](#releasing)
- [References](#references)
- [License](#license)

Usage
-----

### Java

#### Listener

```java
// initialize NeuroSky object with listener
NeuroSky neuroSky = new NeuroSky(new ExtendedDeviceMessageListener() {
  @Override public void onStateChange(State state) {
    // handle state change...
  }

  @Override public void onSignalChange(Signal signal) {
    // handle signal change...
  }

  @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
    // handle brain waves change...
  }
});

// connect to the device
try {
  neuroSky.connect();
} catch (BluetoothNotEnabledException e) {
  // handle exception...
}

// disconnect from the device
neuroSky.disconnect();

// start monitoring (should start automatically after establishing connection)
neuroSky.startMonitoring();

// stop monitoring
neuroSky.stopMonitoring();
```

You can also create simpler listener with `DeviceMessageListener` interface and handle `android.os.Message` objects.

```java
NeuroSky neuroSky = new NeuroSky(message -> {
  // handle message...
});
```

Nevertheless, in that case, you'll have to process and handle data manually.

#### RxJava

```java
neuroSky = new NeuroSky();

neuroSky
    .stream()
    .subscribeOn(Schedulers.computation())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(brainEvent -> {
      handleStateChange(brainEvent.state());
      handleSignalChange(brainEvent.signal());
      handleBrainWavesChange(brainEvent.brainWaves());
    });

// connecting, disconnecting, starting and stopping monitoring is the same as in the previous example
```

Default backpressure strategy is BUFFER. In order to customize backpressure strategy, you can use the following method:

```java
Flowable<BrainEvent> stream(backpressureStrategy)
```

You can also leverage capabilities of RxJava and its error handling by using the following methods:

```java
Completable connectCompletable()
Completable disconnectCompletable()
Completable startMonitoringCompletable()
Completable stopMonitoringCompletable()
```

### Kotlin

#### Listener

```kotlin
// initialize NeuroSky object with listener
val neuroSky = NeuroSky(object : ExtendedDeviceMessageListener() {
  override fun onStateChange(state: State) {
    handleStateChange(state)
  }

  override fun onSignalChange(signal: Signal) {
    handleSignalChange(signal)
  }

  override fun onBrainWavesChange(brainWaves: Set<BrainWave>) {
    handleBrainWavesChange(brainWaves)
  }
})

// connect to the device
try {
  neuroSky.connect()
} catch (e: BluetoothNotEnabledException) {
  // handle exception...
}

// disconnect from the device
neuroSky.disconnect()

// start monitoring (should start automatically after establishing connection)
neuroSky.startMonitoring()

// stop monitoring
neuroSky.stopMonitoring()
```

You can also create simpler listener with `DeviceMessageListener` interface and handle `android.os.Message` objects.

```kotlin
val neuroSky = NeuroSky(DeviceMessageListener {
  // handle message here...
})
```

Nevertheless, in that case, you'll have to process and handle data manually.

#### RxKotlin

```kotlin
val neuroSky = NeuroSky()

neuroSky
  .stream()
  .subscribeOn(Schedulers.computation())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe {
    handleStateChange(it.state())
    handleSignalChange(it.signal())
    handleBrainWavesChange(it.brainWaves())
  }

// connecting, disconnecting, starting and stopping monitoring is the same as in the previous example
```

Default backpressure strategy is BUFFER. In order to customize backpressure strategy, you can use the following method:

```java
Flowable<BrainEvent> stream(backpressureStrategy)
```

You can also leverage capabilities of RxKotlin and its error handling by using the following methods:

```java
Completable connectCompletable()
Completable disconnectCompletable()
Completable startMonitoringCompletable()
Completable stopMonitoringCompletable()
```

Examples
--------

You can find examples of library usage in the following directories:
- `app-java` (example with listener)
- `app-kotlin` (example with listener)
- `app-rxjava`
- `app-rxkotlin`

Download
--------

TBD.

**Please note**: this library is released as a **fat aar** and contains all its dependencies within a single `*.aar` file. It's done this way because this library depends on ThinkGear library, which is distributed as a `ThinkGear.jar` file by the NeuroSky company. ThinkGear is also not available on the Maven Central repository. I wanted to make usage of this library as simple as possible without bothering about additional dependencies and custom configuration. Now, with this approach we can add a single dependency to our project and we're good to go.

Tests
-----

Tests are available in `library/src/test/java/` directory and can be executed on JVM without any emulator or Android device from Android Studio or CLI with the following command:

```
./gradlew test
```

To generate test coverage report, run the following command:

```
./gradlew test jacocoTestReport
```

Code style
----------

Code style used in the project is called `SquareAndroid` from Java Code Styles repository by Square available at: https://github.com/square/java-code-styles.

Static Code Analysis
--------------------

Static code analysis runs Checkstyle, PMD and Lint. It can be executed with command:

 ```
 ./gradlew check
 ```

JavaDoc
-------

JavaDoc is available at: http://pwittchen.github.io/neurosky-android-sdk/javadoc

Changelog
---------

See [CHANGELOG.md](https://github.com/pwittchen/neurosky-android-sdk/blob/master/CHANGELOG.md) file.

Releasing
---------

See [RELEASING.md](https://github.com/pwittchen/neurosky-android-sdk/blob/master/RELEASING.md) file.

References
-----------
- http://neurosky.com/
- http://developer.neurosky.com/
- https://store.neurosky.com/products/android-developer-tools-4
- https://github.com/pwittchen/EEGReader
- http://wittchen.io/tags/bci/
- https://play.google.com/store/apps/details?id=com.pwittchen.eeganalyzer
- https://github.com/topics/neurosky
- https://slides.com/piotrwittchen/brain-computer-interface-for-mobile-devices
- http://support.neurosky.com/kb/science/how-to-convert-raw-values-to-voltage
- http://support.neurosky.com/kb/science/eeg-band-frequencies
- http://developer.neurosky.com/docs/lib/exe/fetch.php?media=thinkgear_socket_protocol.pdf

License
-------

    Copyright 2018 Piotr Wittchen

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
