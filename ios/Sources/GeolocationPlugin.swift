import CoreLocation
import SwiftRs
import Tauri

class GeolocationService: NSObject, CLLocationManagerDelegate {
  private let locationManager = CLLocationManager()
  var onLocationUpdate: ((CLLocation) -> Void)?
  var onLocationFail: ((Error) -> Void)?

  override init() {
    super.init()
    locationManager.delegate = self
    locationManager.requestWhenInUseAuthorization()
    locationManager.desiredAccuracy = kCLLocationAccuracyBest
    locationManager.distanceFilter = 10  // Update every 10 meters
  }

  func startUpdatingLocation() {
    locationManager.startUpdatingLocation()
  }

  func stopUpdatingLocation() {
    locationManager.stopUpdatingLocation()
  }

  func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
    guard let location = locations.last else { return }
    onLocationUpdate?(location)
  }

  func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
    onLocationFail?(error)
  }
}

class GeolocationPlugin: Plugin {
  private var geolocationService = GeolocationService()

  @objc public func startLocationUpdates(_ invoke: Invoke) throws {
    geolocationService.onLocationUpdate = { [weak self] location in
      self?.trigger(
        "locationUpdated",
        data: [
          "latitude": location.coordinate.latitude,
          "longitude": location.coordinate.longitude,
          "accuracy": location.horizontalAccuracy,
          "altitude": location.altitude,
          "timestamp": location.timestamp.timeIntervalSince1970 * 1000,  // Convert to milliseconds
        ]
      )
      invoke.resolve([
        "latitude": location.coordinate.latitude,
        "longitude": location.coordinate.longitude,
        "accuracy": location.horizontalAccuracy,
        "altitude": location.altitude,
        "timestamp": location.timestamp.timeIntervalSince1970 * 1000,  // Convert to milliseconds
      ])
    }

    geolocationService.onLocationFail = { [weak self] error in
      self?.trigger(
        "locationError", data: ["message": error.localizedDescription]
      )

      invoke.resolve(["message": error.localizedDescription])
    }

    geolocationService.startUpdatingLocation()
    // invoke.resolve(["message": "starting location updates"])
  }

  @objc public func stopLocationUpdates(_ invoke: Invoke) throws {
    geolocationService.stopUpdatingLocation()
    invoke.resolve(["message": "stopping location updates"])
  }
}

@_cdecl("init_plugin_geolocation")
func initPlugin() -> Plugin {
  return GeolocationPlugin()
}
