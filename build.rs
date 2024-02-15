const COMMANDS: &[&str] = &["getLocation", "checkPermissions", "requestPermissions"];

fn main() {
    tauri_plugin::Builder::new(COMMANDS)
        .android_path("android")
        .ios_path("ios")
        .build();
}
