use tauri::{
  plugin::{Builder, TauriPlugin},
  Manager, Runtime,
};

use std::{collections::HashMap, sync::Mutex};

pub use models::*;

#[cfg(desktop)]
mod desktop;
#[cfg(mobile)]
mod mobile;

mod commands;
mod error;
mod models;

pub use error::{Error, Result};

#[cfg(desktop)]
use desktop::Geolocation;
#[cfg(mobile)]
use mobile::Geolocation;

#[derive(Default)]
struct MyState(Mutex<HashMap<String, String>>);

/// Extensions to [`tauri::App`], [`tauri::AppHandle`] and [`tauri::Window`] to access the geolocation APIs.
pub trait GeolocationExt<R: Runtime> {
  fn geolocation(&self) -> &Geolocation<R>;
}

impl<R: Runtime, T: Manager<R>> crate::GeolocationExt<R> for T {
  fn geolocation(&self) -> &Geolocation<R> {
    self.state::<Geolocation<R>>().inner()
  }
}

/// Initializes the plugin.
pub fn init<R: Runtime>() -> TauriPlugin<R> {
  Builder::new("geolocation")
    .invoke_handler(tauri::generate_handler![commands::execute])
    .setup(|app, api| {
      #[cfg(mobile)]
      let geolocation = mobile::init(app, api)?;
      #[cfg(desktop)]
      let geolocation = desktop::init(app, api)?;
      app.manage(geolocation);

      // manage state so it is accessible by the commands
      app.manage(MyState::default());
      Ok(())
    })
    .build()
}
