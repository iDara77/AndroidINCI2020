package com.obsoft.inci2019.models

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.obsoft.inci2019.CheckoutActivity

object LocationServices {
    //The minimum distance to change updates in meters
    private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10  // 10 meters


    //The minimum time between updates in milliseconds
    private const val MIN_TIME_BW_UPDATES: Long = 1000 //1000 * 60 * 1; // 1 minute

    private var locationManager: LocationManager? = null

    fun checkPermissions(activity: AppCompatActivity): Boolean {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            return true
        }
        return false
    }

    fun requestPermissions(activity: AppCompatActivity, permissionId: Int) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            permissionId
        )
    }

    fun isLocationEnabled(activity: AppCompatActivity): Boolean {
        locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(activity: AppCompatActivity, permissionId: Int) : Location? {
        if (checkPermissions(activity)) {
            if (isLocationEnabled(activity)) {
                return locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            } else {
                Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent)
            }
        } else {
            requestPermissions(activity, permissionId)
        }
        return null
    }

    @SuppressLint("MissingPermission")
    fun startLocationUpdates(activity: CheckoutActivity, permissionId: Int) {
        if (checkPermissions(activity)) {
            if (isLocationEnabled(activity)) {
                locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), activity);
            } else {
                Toast.makeText(activity, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent)
            }
        } else {
            requestPermissions(activity, permissionId)
        }
    }
}

//abstract class LocationActivityListener : AppCompatActivity, LocationListener {}
