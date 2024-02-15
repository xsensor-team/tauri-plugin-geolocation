import { invoke } from "@tauri-apps/api/core";

export async function getLocation() {
  console.log("[tauri-plugin-geolocation] getLocation");

  return await invoke("plugin:geolocation|getLocation");
}

export async function checkPermissions() {
  console.log("[tauri-plugin-geolocation] checkPermissions");

  return await invoke("plugin:geolocation|checkPermissions");
}

export async function requestPermissions() {
  console.log("[tauri-plugin-geolocation] requestPermissions");

  return await invoke("plugin:geolocation|requestPermissions");
}
