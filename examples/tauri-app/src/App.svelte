<script>
  import { invoke } from "@tauri-apps/api/core"
  import Greet from "./lib/Greet.svelte"
  import { startLocationUpdates } from "tauri-plugin-geolocation-api"
  import { listen } from "@tauri-apps/api/event"

  let response = ""

  function updateResponse(returnValue) {
    response = returnValue
  }

  async function _execute() {
    try {
      // check permission state
      const permission = await invoke("plugin:geolocation|checkPermissions")
      // if (permission.location === 'prompt-with-rationale') {
      //   // show information to the user about why permission is needed
      // }

      console.log("permission", permission)

      // request permission
      if (permission?.location?.startsWith("prompt")) {
        const state = await invoke("plugin:geolocation|requestPermissions", {
          permissions: ["location"]
        })
        console.log("state", state)
        await startLocationUpdates()

        const unlisten = listen("location-updated", async (event) => {
          updateResponse(event)
        })

        return
      }
      await startLocationUpdates()

      const unlisten = listen("location-updated", async (event) => {
        updateResponse(event)
      })
    } catch (e) {
      updateResponse(e)
    }
  }
</script>

<main class="container">
  <h1>Welcome to Tauri!</h1>

  <div class="row">
    <a href="https://vitejs.dev" target="_blank">
      <img src="/vite.svg" class="logo vite" alt="Vite Logo" />
    </a>
    <a href="https://tauri.app" target="_blank">
      <img src="/tauri.svg" class="logo tauri" alt="Tauri Logo" />
    </a>
    <a href="https://svelte.dev" target="_blank">
      <img src="/svelte.svg" class="logo svelte" alt="Svelte Logo" />
    </a>
  </div>

  <p>Click on the Tauri, Vite, and Svelte logos to learn more.</p>

  <div class="row">
    <Greet />
  </div>

  <div>
    <button on:click={_execute}>Execute</button>
    <div>{@html response}</div>
  </div>
</main>

<style>
  .logo.vite:hover {
    filter: drop-shadow(0 0 2em #747bff);
  }

  .logo.svelte:hover {
    filter: drop-shadow(0 0 2em #ff3e00);
  }
</style>
