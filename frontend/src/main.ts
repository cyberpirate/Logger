import App from './App.svelte';
import { initHook } from './CefHook'

initHook();

const app = new App({
	target: document.body,
	props: {
		name: 'world'
	}
});

export default app;