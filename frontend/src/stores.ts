import { readable, writable } from 'svelte/store';
import { setLineListener, removeLineListener, ChainArg } from './CefHook';

export const lineStore = readable<{[key: string]: string[]}>({}, function start(set) {

    let data: {[key: string]: string[]} = {};

    setLineListener((arg) => {
        if (!data[arg.name]) {
            data[arg.name] = [];
        }
        data[arg.name].push(arg.line);
        set(data);
    })

    return function stop() {
        removeLineListener();
    }
})

export const activeLogs = writable<[string, ChainArg][]>([]);
export const drawerOpen = writable(false);
export const selectedLog = writable<string | null>(null);