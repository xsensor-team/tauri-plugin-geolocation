import SwiftRs
import Tauri
import UIKit
import WebKit
import CoreLocation

class GeolocationService: NSObject, CLLocationManagerDelegate {
    private let locationManager = CLLocationManager()
    var onLocationUpdate: ((CLLocation) -> Void)?
    var onLocationFail: ((Error) -> Void)?

    override init() {
        super.init()
        locationManager.delegate = self
        locationManager.requestWhenInUseAuthorization()
    }

    func startUpdatingLocation() {
        locationManager.startUpdatingLocation()
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
    private var invoke: Invoke?


    @objc public override func checkPermissions(_ invoke: Invoke) {
        invoke.resolve()
    }

    @objc public override func requestPermissions(_ invoke: Invoke) {
        invoke.resolve()
    }

    @objc public func getLocation(_ invoke: Invoke) throws {
        self.invoke = invoke
        geolocationService.onLocationUpdate = { [weak self] location in
            let locationData = ["lat": location.coordinate.latitude, "lng": location.coordinate.longitude]
            self?.invoke?.resolve(locationData)
        }
        geolocationService.onLocationFail = { [weak self] error in
            self?.invoke?.reject(error.localizedDescription)
        }
        geolocationService.startUpdatingLocation()
    }
}

@_cdecl("init_plugin_geolocation")
func initPlugin() -> Plugin {
    return GeolocationPlugin()
}
