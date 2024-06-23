# Tauri Plugin geolocation

## Install

add directly to your package.json

```json
"tauri-plugin-geolocation-api": "github:xsensor-team/tauri-plugin-geolocation#main"
```

and to your cargo.toml

```toml
tauri-plugin-geolocation = { git = "https://github.com/xsensor-team/tauri-plugin-geolocation.git" }
```

and connect the plugin in your tauri builder

```rust
tauri::Builder::default()
    .setup()
    .plugin(tauri_plugin_geolocation::init())
    .invoke_handler(tauri::generate_handler![])
    .run(tauri::generate_context!())
    .expect("Error while building tauri application");
```

### iOS instructions

make sure to add the following permission to your `info.plist`

```xml
<key>NSLocationWhenInUseUsageDescription</key>
<string></string>
```

### Android instructions

make sure to add location permissions to your `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

## Usage

See the [examples](https://github.com/xsensor-team/tauri-plugin-geolocation/tree/main/examples/tauri-app) for more information

```javascript
import { startLocationUpdates } from 'tauri-plugin-geolocation-api'

 ...
import { invoke } from "@tauri-apps/api/core"
import { listen } from "@tauri-apps/api/event"
import { useEffect } from "react"

export default function useListenToLocationUpdate() {
  useEffect(() => {
    invoke("plugin:geolocation|startLocationUpdates")

    const unlisten = listen("location-updated", async (event) => {
        // get geolocation info from event
    })

    return () => {
      invoke("plugin:geolocation|stopLocationUpdates")
      unlisten.then((f) => f())
    }
  }, [])
}
```
