
const hookListeners = new Map<string, (arg: any) => void>();

export function initHook() {
    (<any> window).cefHook = function(arg: any) {
        console.log('cefHook called with arg: ' + JSON.stringify(arg));
        hookListeners.get(arg.name)(arg.arg);
    }
}

export function setHookListener(name: string, listener: (arg: any) => void) {
    hookListeners.set(name, listener);
}

export function removeHookListener(name: string) {
    hookListeners.delete(name);
}

export function setLineListener(listener: (arg: {name: string, line: string}) => void) {
    setHookListener("line", listener);
}

export function removeLineListener() {
    removeHookListener("line");
}

export function callBackend(req: string): Promise<string> {
    return new Promise((resolve, reject) => {
        (<any> window).cefQuery({
            request: req,
            onSuccess: function(response: string) {
                resolve(response);
            },
            onFailure: function(error_code: number, error_message: string) {
                reject(error_message);
            }
        })
    });
}

function createFunc<argT, retT>(name: string) {
    return async function(arg: argT): Promise<retT> {
        return JSON.parse(await callBackend(JSON.stringify({ name: name, arg: arg })));
    }
}

export class LinkDesc {
    linkName: string;
    args: string[];
}
export class ChainArg {
    name: string;
    desc: LinkDesc[];
}
export const createChain = createFunc<ChainArg, boolean>("createChain");