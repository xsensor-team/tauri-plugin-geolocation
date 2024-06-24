const COMMANDS: &[&str] = &[
    "stopLocationUpdates",
    "startLocationUpdates",
    "register_listener",
];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
