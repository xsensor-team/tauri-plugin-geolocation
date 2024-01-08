package com.plugin.geolocation

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import app.tauri.annotation.Command
import app.tauri.plugin.Invoke
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.tasks.CancellationTokenSource

@TauriPlugin
class GeolocationPlugin(private val activity: Activity) : Plugin(activity) {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    init {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    private fun checkPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }

    @Command
    fun getLocation(invoke: Invoke) {
        if (!checkPermissions()) {
            requestPermissions()
            invoke.reject("Location permission not granted")
            return
        }

        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, cancellationTokenSource.token)
            .addOnSuccessListener { location ->
                if (location != null) {
                    val locationData = JSObject()
                    locationData.put("lat", location.latitude)
                    locationData.put("lng", location.longitude)
                    invoke.resolve(locationData)
                } else {
                    invoke.reject("Location is null")
                }
            }
            .addOnFailureListener { exception ->
                invoke.reject(exception.message ?: "Failed to get location")
            }
    }
}