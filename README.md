NeuroSky Android SDK
====================

Android SDK for the NeuroSky MindWave Mobile Brainwave Sensing Headset

[![Build Status](https://img.shields.io/travis/pwittchen/neurosky-android-sdk.svg?branch=master&style=flat-square)](https://travis-ci.org/pwittchen/neurosky-android-sdk) [![codecov](https://img.shields.io/codecov/c/github/pwittchen/neurosky-android-sdk/master.svg?style=flat-square&label=coverage)](https://codecov.io/gh/pwittchen/neurosky-android-sdk) ![Maven Central](https://img.shields.io/maven-central/v/com.github.pwittchen/neurosky-android-sdk.svg?style=flat-square)

This SDK allows you to write mobile Android apps interacting with the brain via [NeuroSky](http://neurosky.com/) MindWave Mobile device connected to the phone or tablet via Bluetooth. You can write apps controlled by your brain or perform data acquisition and analysis of the brain signals (attention, meditation, brain waves, raw signal) and eye blinks. NeuroSky uses EEG technology for gathering brain data and EMG sensor for detecting blinks. NeuroSky Android SDK uses ThinkGear library under the hood, which was developed by the NeuroSky company. 

**Please note**: this SDK is not a product of NeuroSky company, but it depends on its software and hardware

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
- [Documentation](#documentation)
- [Changelog](#changelog)
- [Releasing](#releasing)
- [Verified devices](#verified-devices)
- [Device diagram](#device-diagram)
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

// start monitoring
neuroSky.start();

// stop monitoring
neuroSky.stop();
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
// initialize object
RxNeuroSky neuroSky = new RxNeuroSky();

// stream data
neuroSky
  .stream()
  .subscribeOn(Schedulers.computation())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(brainEvent -> {
    // handle state in brainEvent.state();
    // handle signal in brainEvent.signal();
    // handle brainwaves in brainEvent.brainWaves();
  });

// connect to the device
neuroSky
  .connect()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
      () -> /* is connecting... */,
      throwable -> { /* handle error...*/ }
  );

// start monitoring
neuroSky
  .start()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
    () -> /* started monitoring */,
    throwable -> { /* handle error...*/ }
   );

// stop monitoring
neuroSky
  .stop()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
    () -> /* stopped monitoring */,
    throwable -> { /* handle error...*/ }
   );

// disconnect from the device
neuroSky
  .disconnect()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
    () -> /* is disconnected */,
    throwable -> { /* handle error...*/ }
  );

```

Default backpressure strategy is `BUFFER`. In order to customize backpressure strategy, you can use the following method:

```java
Flowable<BrainEvent> stream(backpressureStrategy)
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

// start monitoring
neuroSky.start()

// stop monitoring
neuroSky.stop()
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
//initialize object
val neuroSky = RxNeuroSky()

// stream data
neuroSky
  .stream()
  .subscribeOn(Schedulers.computation())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe {
    // handle state in it.state();
    // handle signal in it.signal();
    // handle brainwaves in it.brainWaves();
  }

// connect to the device
neuroSky
  .connect()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
      { /* is connecting... */ },
      { throwable -> /* handle error */ }
  )

// start monitoring
neuroSky
  .start()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
      { /* started monitoring */ },
      { throwable -> /* handle error */ }
  )

// stop monitoring
neuroSky
  .stop()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
      { /* stopped monitoring */ },
      { throwable -> /* handle error */ }
  )

// disconnect from the device
neuroSky
  .disconnect()
  .subscribeOn(Schedulers.io())
  .observeOn(AndroidSchedulers.mainThread())
  .subscribe(
      { /* is disconnected */ },
      { throwable -> /* handle error */ }
  )
```

Default backpressure strategy is `BUFFER`. In order to customize backpressure strategy, you can use the following method:

```java
Flowable<BrainEvent> stream(backpressureStrategy)
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

You can depend on the library through Gradle:

```groovy
dependencies {
  implementation 'com.github.pwittchen:neurosky-android-sdk:0.0.2'
}
```

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

Static code analysis
--------------------

Static code analysis runs Checkstyle, PMD and Lint. It can be executed with command:

 ```
 ./gradlew check
 ```

JavaDoc
-------

JavaDoc is available at: http://pwittchen.github.io/neurosky-android-sdk/javadoc

Documentation
-------------

Documentation is available at: http://pwittchen.github.io/neurosky-android-sdk/docs

Changelog
---------

See [CHANGELOG.md](https://github.com/pwittchen/neurosky-android-sdk/blob/master/CHANGELOG.md) file.

Releasing
---------

See [RELEASING.md](https://github.com/pwittchen/neurosky-android-sdk/blob/master/RELEASING.md) file.

Verified devices
----------------

This SDK was tested with the following devices:
- NeuroSky MindWave Mobile 1

**Note**: According to NeuroSky support, it should work with NeuroSky MindWave Mobile 2 as well, but I didn't have an opportunity to test it. If you have such device, you can test it and give me a feedback!

Device diagram
--------------

This is diagram of the NeuroSky MindWave Mobile 1

![](https://raw.githubusercontent.com/pwittchen/neurosky-android-sdk/master/mindwave_diagram.jpg)

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
