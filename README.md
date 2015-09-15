# Bletia
[![wercker status](https://app.wercker.com/status/480ca2bbd43cc73740554a6ad347cca5/s/master "wercker status")](//app.wercker.com/project/bykey/480ca2bbd43cc73740554a6ad347cca5)
[![LICENSE](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg "LICENSE")](//github.com/izumin5210/Bletia/blob/master/LICENSE.md)
[![Download](https://api.bintray.com/packages/izumin5210/maven/Bletia/images/download.svg)](https://bintray.com/izumin5210/maven/Bletia/_latestVersion)

## Download

```groovy
dependencies {
    compile 'info.izumin.android:bletia:1.0.0'
}
```

## Example
### Instantiate

```java
Bletia bletia = new Bletia(context);
```

### Connect to BLE device

```java
// BluetoothDevice device
bletia.connect(device);
```

### Enable notification

```java
bletia.enableNotification(characteristic, true)
    .then(new DoneCallback<BluetoothGattCharacteristic>() {
        @Override
        public void onDone(BluetoothGattCharacteristic result) {
            // Call when the request was successfully.
        }
    })
    .fail(new FailCallback<BletiaException>() {
        @OVerride
        public void onFail(BletiaException result) {
            // Call when the request was failure.
        }
    });
```

### Read characteristic

```java
bletia.readCharacteristic(characteristic)
    .then(new DoneCallback<BluetoothGattCharacteristic>() {
        @Override
        public void onDone(BluetoothGattCharacteristic result) {
            // Call when the request was successfully.
        }
    })
    .fail(new FailCallback<BletiaException>() {
        @OVerride
        public void onFail(BletiaException result) {
            // Call when the request was failure.
        }
    });
```

### Write characteristic

```java
bletia.writeCharacteristic(characteristic)
    .then(new DoneCallback<BluetoothGattCharacteristic>() {
        @Override
        public void onDone(BluetoothGattCharacteristic result) {
            // Call when the request was successfully.
        }
    })
    .fail(new FailCallback<BletiaException>() {
        @OVerride
        public void onFail(BletiaException result) {
            // Call when the request was failure.
        }
    });
```

## License

```
Copyright 2015 izumin5210

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
