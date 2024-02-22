package com.example.automotive.gear

import android.car.Car
import android.car.VehiclePropertyIds
import android.car.hardware.CarPropertyValue
import android.car.hardware.property.CarPropertyManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    /** car object to communicate with car services */
    private lateinit var car : Car

    /** manager to receive car properties like gear information */
    private var carPropertyManager: CarPropertyManager? = null

    /** listener to receive car property changes */
    private var carPropertyCallBack = object : CarPropertyManager.CarPropertyEventCallback {
        override fun onChangeEvent(carPropertyValue: CarPropertyValue<*>) {
            updateUi(carPropertyValue.value.toString())
        }
        override fun onErrorEvent(propId: Int, zone: Int) { }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // connect to car and get manager
        car = Car.createCar(
            this, null, Car.CAR_WAIT_TIMEOUT_WAIT_FOREVER
        ) { car: Car, ready: Boolean ->
            if (ready) {
                carPropertyManager = car.getCarManager(Car.PROPERTY_SERVICE) as CarPropertyManager
            }
        }

    }

    override fun onResume() {
        super.onResume()
        registerListener()
    }

    override fun onPause() {
        //unregister listener
        carPropertyManager?.unregisterCallback(carPropertyCallBack)
        super.onPause()
    }


    override fun onDestroy() {
        //disconnect to car object
        if (car.isConnected) car.disconnect()
        super.onDestroy()
    }

    /**
     * register as car property listener
     */
    private fun registerListener() {
        carPropertyManager?.registerCallback(carPropertyCallBack, VehiclePropertyIds.GEAR_SELECTION,
            CarPropertyManager.SENSOR_RATE_NORMAL
        )
    }

    private fun updateUi(gearValue: String) {
        findViewById<TextView>(R.id.tvGear).text = gearValue
    }

}