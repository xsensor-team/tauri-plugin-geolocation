import { invoke } from "@tauri-apps/api/core"

export async function startLocationUpdates() {
  console.log("[tauri-plugin-geolocation] startLocationUpdates")

  return await invoke("plugin:geolocation|startLocationUpdates")
}

export async function stopLocationUpdates() {
  console.log("[tauri-plugin-geolocation] stopLocationUpdates")

  return await invoke("plugin:geolocation|stopLocationUpdates")
}
