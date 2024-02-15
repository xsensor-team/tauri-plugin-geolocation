package com.plugin.geolocation

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import app.tauri.annotation.Command
import app.tauri.plugin.JSObject
import app.tauri.plugin.Plugin
import app.tauri.plugin.Invoke
import android.location.Location
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.FusedLocationProviderClient
import app.tauri.annotation.TauriPlugin
import app.tauri.annotation.Permission
import android.util.Log

@TauriPlugin(
    permissions = [
        Permission(strings = [
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ],
            alias = "location"
        )
    ]
)
class GeolocationPlugin(private val myActivity: Activity) : Plugin(myActivity) {
    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(myActivity)

    @Command
    fun getlocation(invoke: Invoke) {
        // ask for user permission
        if (ContextCompat.checkSelfPermission(myActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(myActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                invoke?.reject("403:PermissionNotGranted")
                return
            }
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                Log.d("DebugTag", "success location")
                if (location != null) {
                    val response = JSObject()                  

                    response.put("lat", location.latitude)
                    response.put("lng", location.longitude)
                    invoke.resolve(response)
                } else {
                    invoke.reject("No location found")
                }
            }
            .addOnFailureListener { exception: Exception ->
                val errorMessage = exception.message ?: "Unknown error"

                invoke?.reject(errorMessage)
            }
    }
}