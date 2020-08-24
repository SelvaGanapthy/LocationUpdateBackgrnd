package com.example.locationupdatebackgrnd

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import java.lang.Exception
import androidx.core.content.ContextCompat.getSystemService
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.app.Activity
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.Manifest.permission.ACCESS_FINE_LOCATION
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R.string.cancel
import android.app.AlertDialog
import android.content.DialogInterface
import androidx.core.content.ContextCompat.startActivity
import android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.provider.Settings


class GpsTracker : Service, LocationListener {

    companion object {
        val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        val MIN_TIME_BW_UPDATES: Long = 1000 * 60 * 1
    }

    private var context: Context? = null
    /* flag for GPS status*/
    private var isGPSEnabled: Boolean = false

    /* flag for network status*/
    private var isNetworkEnabled: Boolean = false

    /* flag for GPS status*/
    private var canGetLocation: Boolean = false

    private var location: Location? = null
    private var latitude: Double = 0.toDouble() // latitude
    private var longitude: Double = 0.toDouble() // longitude
    var locationManager: LocationManager? = null

    constructor(context: Context) {
        this.context = context
        getLocation()
    }

    fun getLocation(): Location {
        try {

            locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            // getting GPS status
            isGPSEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)!!

            // getting network status
            isNetworkEnabled =
                locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)!!


            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true

                // First get location from Network Provider
                if (isNetworkEnabled) {

                    //check the network permission
                    if (ActivityCompat.checkSelfPermission(
                            context!!,
                            ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context!!, ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            context as Activity,
                            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
                            101
                        )
                    }
                    locationManager?.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                    )

//                    Log.i("Network", "Network")
                    if (locationManager != null) {
                        location =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        if (location != null) {
                            latitude = location!!.getLatitude()
                            longitude = location!!.getLongitude()
                        }
                    }

                }



                if (isGPSEnabled) {


                    if (location == null) {


                        //check the network permission
                        if (ActivityCompat.checkSelfPermission(
                                context!!, ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                context!!, ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            ActivityCompat.requestPermissions(
                                context as Activity,
                                arrayOf(
                                    ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION
                                ), 101
                            )
                        }

                        locationManager!!.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this
                        )

//                        Log.i("GPS Enabled", "GPS Enabled")
                        if (locationManager != null) {
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (location != null) {
                                latitude = location!!.getLatitude()
                                longitude = location!!.getLongitude()
                            }
                        }


                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location!!
    }


    fun stopUsingGps() {
        if (locationManager != null) locationManager?.removeUpdates(this)
    }


    fun getLatitude(): Double {
        if (location != null)
            this.latitude = location?.latitude!!
        return this.latitude
    }


    fun getLongitude(): Double {

        if (location != null)
            this.longitude = location?.longitude!!
        return this.longitude

    }


    fun canGetLocation(): Boolean = this.canGetLocation


    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(context)

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings")

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?")

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
            DialogInterface.OnClickListener { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
              context?.startActivity(intent)
            })

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
        alertDialog.show()
    }





    override fun onBind(intent: Intent): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onLocationChanged(location: Location) {

    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(provider: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(provider: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}