import { invoke } from "@tauri-apps/api/core";

export async function getlocation() {
  console.log("[tauri-plugin-geolocation] getlocation");

  return await invoke("plugin:geolocation|getlocation");
}
