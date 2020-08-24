package com.example.locationupdatebackgrnd

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.view.View
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    101
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val gpsTracker = GpsTracker(this@MainActivity)
        if (gpsTracker.canGetLocation()) {
            val latitude = gpsTracker.getLatitude()
            val longitude = gpsTracker.getLongitude()

            val t1 = findViewById<View>(R.id.t1) as TextView
            t1.setText(
                "Longtitude = " + longitude.toString() + "\n" +
                        "Latitude = " + latitude.toString()
            )
        } else {
            gpsTracker.showSettingsAlert()
        }


    }

    override fun onPause() {
        super.onPause()
//        locationManager.removeUpdates(this);
    }
}
