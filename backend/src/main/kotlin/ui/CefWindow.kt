package ui

import ServiceManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import me.friwi.jcefmaven.CefAppBuilder
import me.friwi.jcefmaven.MavenCefAppHandlerAdapter
import org.cef.CefApp
import org.cef.CefClient
import org.cef.browser.CefBrowser
import org.cef.browser.CefFrame
import org.cef.browser.CefMessageRouter
import org.cef.callback.CefAuthCallback
import org.cef.callback.CefCallback
import org.cef.callback.CefNativeAdapter
import org.cef.callback.CefQueryCallback
import org.cef.handler.*
import org.cef.misc.BoolRef
import org.cef.network.CefRequest
import threads.ThreadService
import utils.AssetService
import java.awt.BorderLayout
import java.awt.Component
import java.awt.KeyboardFocusManager
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

class CefWindow(
    startURL: String,
    val srvMgr: ServiceManager,
    val jsFuncs: Map<String, (String) -> String> = mapOf(),
    useOSR: Boolean = false,
    isTransparent: Boolean = false,
): JFrame() {

    private val cefApp_: CefApp
    private val client_: CefClient
    private val browser_: CefBrowser
    private val browerUI_: Component
    private var browserFocus_ = true

    @Serializable
    data class JsFunc(val name: String)

    init {
        // (0) Initialize CEF using the maven loader
        val builder = CefAppBuilder()
        // windowless_rendering_enabled must be set to false if not wanted.
        builder.cefSettings.windowless_rendering_enabled = useOSR
        // USE builder.setAppHandler INSTEAD OF CefApp.addAppHandler!
        // Fixes compatibility issues with MacOSX
        builder.setAppHandler(object : MavenCefAppHandlerAdapter() {
            override fun stateHasChanged(state: CefApp.CefAppState) {
                // Shutdown the app if the native CEF part is terminated
                if (state == CefApp.CefAppState.TERMINATED) System.exit(0)
            }
        })

        // (1) The entry point to JCEF is always the class CefApp. There is only one
        //     instance per application and therefore you have to call the method
        //     "getInstance()" instead of a CTOR.
        //
        //     CefApp is responsible for the global CEF context. It loads all
        //     required native libraries, initializes CEF accordingly, starts a
        //     background task to handle CEF's message loop and takes care of
        //     shutting down CEF after disposing it.
        //
        //     WHEN WORKING WITH MAVEN: Use the builder.build() method to
        //     build the CefApp on first run and fetch the instance on all consecutive
        //     runs. This method is thread-safe and will always return a valid app
        //     instance.
        cefApp_ = builder.build()

        // (2) JCEF can handle one to many browser instances simultaneous. These
        //     browser instances are logically grouped together by an instance of
        //     the class CefClient. In your application you can create one to many
        //     instances of CefClient with one to many CefBrowser instances per
        //     client. To get an instance of CefClient you have to use the method
        //     "createClient()" of your CefApp instance. Calling an CTOR of
        //     CefClient is not supported.
        //
        //     CefClient is a connector to all possible events which come from the
        //     CefBrowser instances. Those events could be simple things like the
        //     change of the browser title or more complex ones like context menu
        //     events. By assigning handlers to CefClient you can control the
        //     behavior of the browser. See tests.detailed.MainFrame for an example
        //     of how to use these handlers.
        client_ = cefApp_.createClient()

        // (3) Create a simple message router to receive messages from CEF.
        val msgRouter = CefMessageRouter.create()

        msgRouter.addHandler(object: CefNativeAdapter(), CefMessageRouterHandler {
            private val json = Json(Json.Default, { ignoreUnknownKeys = true })

            override fun onQuery(
                browser: CefBrowser,
                frame: CefFrame,
                queryId: Long,
                request: String,
                persistent: Boolean,
                callback: CefQueryCallback
            ): Boolean {

                val f = json.decodeFromString<JsFunc>(request)

                jsFuncs[f.name]?.let({
                    srvMgr.get<ThreadService>().pool.submit({
                        try {
                            callback.success(it(request))
                        } catch(e: Exception) {
                            e.printStackTrace()
                            callback.failure(1, e.message ?: "Unknown error")
                        }
                    })
                    return true
                })

                return false
            }

            override fun onQueryCanceled(browser: CefBrowser, frame: CefFrame, queryId: Long) {
                TODO("Not yet implemented")
            }

        }, true)

        client_.addMessageRouter(msgRouter)

        // (4) One CefBrowser instance is responsible to control what you'll see on
        //     the UI component of the instance. It can be displayed off-screen
        //     rendered or windowed rendered. To get an instance of CefBrowser you
        //     have to call the method "createBrowser()" of your CefClient
        //     instances.
        //
        //     CefBrowser has methods like "goBack()", "goForward()", "loadURL()",
        //     and many more which are used to control the behavior of the displayed
        //     content. The UI is held within a UI-Compontent which can be accessed
        //     by calling the method "getUIComponent()" on the instance of CefBrowser.
        //     The UI component is inherited from a java.awt.Component and therefore
        //     it can be embedded into any AWT UI.
        browser_ = client_.createBrowser(startURL, useOSR, isTransparent)
        browerUI_ = browser_.uiComponent

        if(srvMgr.get<AssetService>().assetsPackaged)
            client_.addRequestHandler(AssetRequestHandler(srvMgr))


        // Clear focus from the address field when the browser gains focus.
        client_.addFocusHandler(object : CefFocusHandlerAdapter() {
            override fun onGotFocus(browser: CefBrowser) {
                if (browserFocus_) return
                browserFocus_ = true
                KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner()
                browser.setFocus(true)
            }

            override fun onTakeFocus(browser: CefBrowser, next: Boolean) {
                browserFocus_ = false
            }
        })

        // (6) All UI components are assigned to the default content pane of this
        //     JFrame and afterwards the frame is made visible to the user.
        contentPane.add(browerUI_, BorderLayout.CENTER)
        pack()
        setSize(800, 600)
        isVisible = true

        // (7) To take care of shutting down CEF accordingly, it's important to call
        //     the method "dispose()" of the CefApp instance if the Java
        //     application will be closed. Otherwise you'll get asserts from CEF.
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent) {
                CefApp.getInstance().dispose()
                dispose()
            }
        })
    }

    fun executeHook(arg: String) {
        browser_.executeJavaScript("window.cefHook($arg);", browser_.url, 0)
    }
}