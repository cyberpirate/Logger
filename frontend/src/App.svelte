<script lang="ts">
	import "../node_modules/svelte-material-ui/bare.css";
	import Button from "@smui/button/src/Button.svelte";
	export let name: string;
	import { callBackend, createChain } from "./CefHook";
	import { lineStore } from "./stores";
	import ChainList from "./ChainList/ChainList.svelte"
	import LogList from "./LogList/LogList.svelte"

	// async function buttonClick() {
		

	// 	let chain = {
	// 		name: "test",
	// 		desc: [
	// 			{ linkName: "process", args: ["bash", "-c", "for (( c=1; ; c++ )); do echo \"asdf: \$c\"; sleep 1; done;"]}
	// 		]
	// 	}

	// 	console.log(await createChain(chain));

	// }

</script>

<!-- <main>
	<h1>Hello {name}!</h1>
	<p>Visit the <a href="https://svelte.dev/tutorial">Svelte tutorial</a> to learn how to build Svelte apps.</p>
	<Button on:click={buttonClick} variant="raised">
		Raised
	</Button>
	<p>{JSON.stringify(Object.keys($lineStore))}</p>
	<p>
		{#if $lineStore["test"]}
			{#each $lineStore["test"] as line}
				{line}<br />
			{/each}
		{/if}
	</p>
</main> -->

<div class="drawer-container">
	<ChainList />
	<LogList />
</div>

<style>
	/* These classes are only needed because the
	  drawer is in a container on the page. */
	.drawer-container {
	  position: relative;
	  display: flex;
	  height: 350px;
	  max-width: 600px;
	  border: 1px solid
		var(--mdc-theme-text-hint-on-background, rgba(0, 0, 0, 0.1));
	  overflow: hidden;
	  z-index: 0;
	}
</style>

<!-- <style>
	main {
		text-align: center;
		padding: 1em;
		max-width: 240px;
		margin: 0 auto;
	}

	h1 {
		color: #ff3e00;
		text-transform: uppercase;
		font-size: 4em;
		font-weight: 100;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style> -->