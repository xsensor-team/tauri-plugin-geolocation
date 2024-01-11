import { invoke } from "@tauri-apps/api/core";

// export async function execute() {
//   return await invoke("plugin:geolocation|execute");
// }

export async function getLocation() {
  console.log("[tauri-plugin-geolocation] getLocation");
  return await invoke("plugin:geolocation|getLocation");
}
