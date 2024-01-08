import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class GeolocationPlugin(private val activity: Activity) : Plugin(activity), LocationListener {
    // ... existing code ...

    @Command
    fun getLocation(invoke: Invoke) {
        this.invoke = invoke
     val response = JSObject()
                response.put("error", "Permission denied by user")
                invoke?.reject(response)
            // requestPermissions()

    }

    // private fun requestPermissions() {
    //     ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_REQUEST_CODE)
    // }

    // override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    //     if (requestCode == PERMISSION_REQUEST_CODE) {
    //         if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
    //             locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this)

    //             // getLocation(invoke)
    //         } else {
    //             // User denied the permission
    //             val response = JSObject()
    //             response.put("error", "Permission denied by user")
    //             invoke?.reject(response)
    //         }
    //     }
    // }

    // companion object {
    //     private const val PERMISSION_REQUEST_CODE = 100
    // }

    // ... existing location listener methods ...
}