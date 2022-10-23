
export function initHook() {
    (<any> window).cefHook = function(arg: any) {
        console.log('cefHook called with arg: ' + JSON.stringify(arg));
    }
}

export function callBackend(arg: any): Promise<string> {
    return new Promise((resolve, reject) => {
        (<any> window).cefQuery({
            request: JSON.stringify(arg),
            onSuccess: function(response: string) {
                resolve(response);
            },
            onFailure: function(error_code: number, error_message: string) {
                reject(error_message);
            }
        })
    });
}
