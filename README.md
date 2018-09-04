NeuroSky Android SDK
=====================

NeuroSky MindWave Mobile Android SDK

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

// connect to device

try {
  neuroSky.connect();
} catch (BluetoothNotEnabledException e) {
  // handle exception...
}

// disconnect from the device

neuroSky.disconnect()

// start monitoring (should start automatically after establishing connection)

neuroSky.startMonitoring()

// stop monitoring

neuroSky.stopMonitoring();

```

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

// connecting, disconnecting, starting and stopping monitoring
// is the same as in the previous example
```

### Kotlin

#### Listener

...

#### RxKotlin

...

Examples
--------

You can find examples of library usage in the following directories:
- `app-java` (example with listener)
- `app-kotlin` (example with listener)
- `app-rxjava`
- `app-rxkotlin`

Download
--------

...

Tests
-----

...

Code style
----------

...

Static Code Analysis
--------------------

...

JavaDoc
-------

...

Changelog
---------

...

Releasing
---------

...

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
