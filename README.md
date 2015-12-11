# Bletia / RxBletia
[![wercker status](https://app.wercker.com/status/480ca2bbd43cc73740554a6ad347cca5/s/master "wercker status")](//app.wercker.com/project/bykey/480ca2bbd43cc73740554a6ad347cca5)
[![LICENSE](https://img.shields.io/badge/license-Apache%202.0-brightgreen.svg "LICENSE")](//github.com/izumin5210/Bletia/blob/master/LICENSE.md)
[![Download](https://api.bintray.com/packages/izumin5210/maven/bletia/images/download.svg)](https://bintray.com/izumin5210/maven/bletia/_latestVersion)

* Bletia: Promisified BluetoothGatt wrapper library(including [JDeferred](https://github.com/jdeferred/jdeferred))
* RxBletia: RxJava binding APIs for Android's BluetoothGatt

## Download

Use Bletia:

```groovy
dependencies {
    // use Bletia
    compile 'info.izumin.android:bletia:2.0.0'
}
```

Use RxBletia:

```groovy
dependencies {
    // use RxBletia
    compile 'info.izumin.android:bletia:2.0.0'
    compile 'io.reactivex:rxjava:1.0.16'
}
```

## Quick Examples
### Instantiate

```java
// Pass application context to constructor.
// If you use Bletia:
Bletia bletia = new Bletia(context);

// If you use RxBletia:
RxBletia bletia = new RxBletia(context);
```

### Connect to BLE device

```java
// BluetoothDevice device
// If you use Bletia:
bletia.connect(device);

// If you use RxBletia:
bletia.connect(device).subscribe();
```

### Enable notification

```java
// BluetoothGattCharacteristic characteristic 
// If you use Bletia:
bletia.enableNotification(characteristic, true)
        .then(new DoneCallback<BluetoothGattCharacteristic>() {
            @Override
            public void onDone(BluetoothGattCharacteristic result) {
                // Call when the request was successfully.
            }
        })
        .fail(new FailCallback<BletiaException>() {
            @Override
            public void onFail(BletiaException result) {
                // Call when the request was failure.
            }
        });

// If you use RxBletia:
bletia.enableNotification(characteristic, true)
        .subscribe(
                result -> {
                    // Call when you receive the notification.
                },
                throwable -> {
                    // Call when the request was failure.
                }
        );
```

### Read characteristic

```java
// BluetoothGattCharacteristic characteristic 
// If you use Bletia:
bletia.readCharacteristic(characteristic)
        .then(new DoneCallback<BluetoothGattCharacteristic>() {
            @Override
            public void onDone(BluetoothGattCharacteristic result) {
                // Call when the request was successfully.
            }
        })
        .fail(new FailCallback<BletiaException>() {
            @Override
            public void onFail(BletiaException result) {
                // Call when the request was failure.
            }
        });

// If you use RxBletia:
bletia.readCharacteristic(characteristic)
        .subscribe(
                result -> {
                    // Call when the request was successfully.
                },
                throwable -> {
                    // Call when the request was failure.
                }
        );
```

### Write characteristic

```java
// BluetoothGattCharacteristic characteristic
// If you use Bletia:
bletia.writeCharacteristic(characteristic)
        .then(new DoneCallback<BluetoothGattCharacteristic>() {
            @Override
            public void onDone(BluetoothGattCharacteristic result) {
                // Call when the request was successfully.
            }
        })
        .fail(new FailCallback<BletiaException>() {
            @Override
            public void onFail(BletiaException result) {
                // Call when the request was failure.
            }
        });
    
// If you use RxBletia:
bletia.writeCharacteristic(characteristic)
        .subscribe(
                result -> {
                    // Call when the request was successfully.
                },
                throwable -> {
                    // Call when the request was failure.
                }
        );
```

### Read descriptor

```java
// BluetoothGattDescriptor descriptor
// If you use Bletia:
bletia.readDescriptor(descriptor)
        .then(new DoneCallback<BluetoothGattDescriptor>() {
            @Override
            public void onDone(BluetoothGattDescriptor result) {
                // Call when the request was successfully.
            }
        })
        .fail(new FailCallback<BletiaException>() {
            @Override
            public void onFail(BletiaException result) {
                // Call when the request was failure.
            }
        });
    
// If you use RxBletia:
bletia.readDescriptor(descriptor)
        .subscribe(
                result -> {
                    // Call when the request was successfully.
                },
                throwable -> {
                    // Call when the request was failure.
                }
        );
```

### Write descriptor

```java
// BluetoothGattDescriptor descriptor
// If you use Bletia:
bletia.writeDescriptor(descriptor)
    .then(new DoneCallback<BluetoothGattDescriptor>() {
            @Override
            public void onDone(BluetoothGattDescriptor result) {
                // Call when the request was successfully.
            }
        })
        .fail(new FailCallback<BletiaException>() {
            @Override
            public void onFail(BletiaException result) {
                // Call when the request was failure.
            }
        });
    
// If you use RxBletia:
bletia.writeDescriptor(descriptor)
        .subscribe(
                result -> {
                    // Call when the request was successfully.
                },
                throwable -> {
                    // Call when the request was failure.
                }
        );
```

### Read remote RSSI

```java
// If you use RxBletia:
bletia.readRemoteRssi()
        .then(new DoneCallback<Integer>() {
            @Override
            public void onDone(Integer result) {
                // Call when the request was successfully.
            }
        })
        .fail(new FailCallback<BletiaException>() {
            @Override
            public void onFail(BletiaException result) {
                // Call when the request was failure.
            }
        });
    
// If you use RxBletia:
bletia.readRemoteRssi()
        .subscribe(
                result -> {
                    // Call when the request was successfully.
                },
                throwable -> {
                    // Call when the request was failure.
                }
        );
```

## Sample

* [bletia-sample](https://github.com/izumin5210/Bletia/tree/master/samples/bletia-sample)
* [rxbletia-sample](https://github.com/izumin5210/Bletia/tree/master/samples/rxbletia-sample)



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
