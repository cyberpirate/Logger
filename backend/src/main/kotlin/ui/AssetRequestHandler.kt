package ui

import ServiceManager
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefAuthCallback
import org.cef.callback.CefCallback
import org.cef.handler.CefLoadHandler
import org.cef.handler.CefRequestHandler
import org.cef.handler.CefRequestHandlerAdapter
import org.cef.handler.CefResourceRequestHandler
import org.cef.misc.BoolRef
import org.cef.network.CefRequest
import utils.AssetService
import java.net.URI

class AssetRequestHandler(private var srvMgr: ServiceManager): CefRequestHandlerAdapter() {
    override fun onBeforeBrowse(
        browser: CefBrowser?,
        frame: CefFrame?,
        request: CefRequest?,
        user_gesture: Boolean,
        is_redirect: Boolean
    ): Boolean {
        return false
    }

    override fun onOpenURLFromTab(
        browser: CefBrowser?,
        frame: CefFrame?,
        target_url: String?,
        user_gesture: Boolean
    ): Boolean {
        return true
    }

    override fun getResourceRequestHandler(
        browser: CefBrowser?,
        frame: CefFrame?,
        request: CefRequest?,
        isNavigation: Boolean,
        isDownload: Boolean,
        requestInitiator: String?,
        disableDefaultHandling: BoolRef?
    ): CefResourceRequestHandler? {

        request?.let({
            val uri = URI(it.url)
            if(uri.host == "asset") {
                return srvMgr.get<AssetService>().requestHandler
            }
        })

        return null
    }
}