import { invoke } from "@tauri-apps/api/core";

export async function getLocation() {
  console.log("[tauri-plugin-geolocation] getLocation");

  return await invoke("plugin:geolocation|getLocation");
}
