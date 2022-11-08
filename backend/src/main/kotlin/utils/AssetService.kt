package utils

import Service
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.callback.CefCallback
import org.cef.handler.CefCookieAccessFilter
import org.cef.handler.CefResourceHandler
import org.cef.handler.CefResourceRequestHandler
import org.cef.handler.CefResourceRequestHandlerAdapter
import org.cef.misc.BoolRef
import org.cef.misc.IntRef
import org.cef.misc.StringRef
import org.cef.network.CefCookie
import org.cef.network.CefRequest
import org.cef.network.CefResponse
import org.cef.network.CefURLRequest
import java.io.File
import java.io.InputStream
import java.net.URI

class AssetService(): Service {

    val assetsPackaged get() = assetExists()

    private val cl get() = this.javaClass

    private fun sanitizePath(vararg path: String): String {
        return "/" + listOf(
            listOf("assets"), path.toList()
        ).flatten()
            .map({ it.trim({ c -> c.isWhitespace() || c == '/' }) })
            .filter({ it.isNotBlank() })
            .joinToString("/")
    }

    private fun relativeSanitizePath(vararg path: String): String {
        return sanitizePath(*path).substringAfter("/assets")
    }

    fun assetExists(vararg path: String): Boolean {
        return cl.getResource(sanitizePath(*path)) != null
    }

    fun assetIsFile(vararg path: String): Boolean {
        cl.getResourceAsStream(sanitizePath(*path))?.use({
            return it.available() > 0
        })
        return false
    }

    fun findAssetWithIndexHtml(vararg path: String): String? {
        if(assetIsFile(*path))
            return relativeSanitizePath(*path)
        if(assetIsFile(*path, "index.html"))
            return relativeSanitizePath(*path, "index.html")
        return null
    }

    fun assetStream(vararg path: String): InputStream? {
        return cl.getResourceAsStream(sanitizePath(*path))
    }

    fun assetData(vararg path: String): ByteArray? {
        return assetStream(*path)?.use({ it.readBytes() })
    }

    private fun resHandler(vararg path: String): CefResourceHandler? {
        val qPath = findAssetWithIndexHtml(*path) ?: return null
        val ext = File(qPath).extension
        val dataStream = assetStream(qPath) ?: return null
        return object: CefResourceHandler {
            override fun processRequest(request: CefRequest?, callback: CefCallback?): Boolean {
                callback?.Continue()
                return true
            }

            override fun getResponseHeaders(response: CefResponse?, responseLength: IntRef?, redirectUrl: StringRef?) {
                response?.mimeType = when(ext) {
                    "htm", "html" -> "text/html"
                    "css" -> "text/css"
                    "js" -> "text/javascript"
                    "png" -> "image/png"
                    else -> ""
                }
                println("getting $qPath $ext ${response?.mimeType}")
            }

            override fun readResponse(
                dataOut: ByteArray,
                bytesToRead: Int,
                bytesRead: IntRef,
                callback: CefCallback
            ): Boolean {
                val n = dataStream.read(dataOut, 0, bytesToRead)
                if(n == -1) {
                    dataStream.close()
                    return false
                }
                bytesRead.set(n)
                return true
            }

            override fun cancel() {
                dataStream.close()
            }
        }
    }

    val requestHandler: CefResourceRequestHandler = object: CefResourceRequestHandlerAdapter() {
        override fun onBeforeResourceLoad(browser: CefBrowser?, frame: CefFrame?, request: CefRequest?): Boolean {
            request?.url?.let({
                return findAssetWithIndexHtml("frontend", URI(it).path) == null
            })
            return true
        }

        override fun getResourceHandler(
            browser: CefBrowser?,
            frame: CefFrame?,
            request: CefRequest?
        ): CefResourceHandler? {
            request?.url?.let({
                return resHandler("frontend", URI(it).path)
            })
            return null
        }
    }
}