package com.obsoft.inci2019

import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.obsoft.inci2019.models.LocationServices

import kotlinx.android.synthetic.main.activity_checkout.*

class CheckoutActivity : AppCompatActivity(), LocationListener {
    val PERMISSION_ID = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        LocationServices.startLocationUpdates(this, PERMISSION_ID)
    }

    fun getCurrentLocation(view: View) {
        showLocation()
    }

    fun showLocation() {
        val location = LocationServices.getLastLocation(this, PERMISSION_ID)
        if (location != null) {
            Toast.makeText(
                this,
                location?.longitude.toString() + " -- " + location?.latitude.toString(),
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
                showLocation()
            }
        }
    }


    override fun onLocationChanged(p0: Location?) {
        print(p0?.longitude.toString()+ " -- "+ p0?.latitude.toString())
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        print(p0)
    }

    override fun onProviderEnabled(p0: String?) {
        print(p0)
    }

    override fun onProviderDisabled(p0: String?) {
        print(p0)
    }

}
