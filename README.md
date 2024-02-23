## Example to fetch gear informations from Car API

This example will show how to use Car Api, get an CarPropertyManager and register it to a listener. 

### Create an automotive android project

First of all you need to add car library to the [gradle](https://github.com/mkett/android-car-gear-example/blob/main/automotive/build.gradle.kts)

```
android {
    ...
    useLibrary ("android.car")
}
```

Add automotive feature and add gear permission in [manifest.xml](https://github.com/mkett/android-car-gear-example/blob/main/automotive/src/main/AndroidManifest.xml)

```
<uses-feature android:name=„android.hardware.type.automotive" android:required="true" />
```

```
<uses-permission android:name="android.car.permission.CAR_POWERTRAIN" />
```

To use car services you need to [create a car connention](https://github.com/mkett/android-car-gear-example/blob/main/automotive/src/main/java/com/example/automotive/gear/MainActivity.kt). Through the car object you can get a special manager that provides different car informations. You can use 
CarServiceLifecycleListener to verify, if the connection was seccessful. The connection has to be disconnected at the end.
```
car = Car.createCar(
            this, null, Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER
        ) { car: Car, ready: Boolean ->
            if (ready) {
                carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
            }
        }
```

Finally you [define a CarPropertyEventCallback to get change events for gear and register our callback](https://github.com/mkett/android-car-gear-example/blob/main/automotive/src/main/java/com/example/automotive/gear/MainActivity.kt).
```
carPropertyManager?.let {
        it.registerCallback(carPropertyCallback, VehiclePropertyIds.GEAR_SELECTION,
            CarPropertyManager.SENSOR_RATE_NORMAL
        )
    }
```

### Automotive Emulator

On emulator, you can find and change the gear at *Car data* -> *Car sensor data*.

![Android Automotive Emulator Gear Example](https://github.com/mkett/android-car-gear-example/blob/main/Android-Emulator-Gear-State.png)
