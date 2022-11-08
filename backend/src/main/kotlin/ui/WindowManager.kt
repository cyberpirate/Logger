package ui

import Service
import ServiceManager
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import threads.ThreadService
import utils.AssetService

class WindowManager: Service {

    lateinit var srvMgr: ServiceManager

    var mainWindow: CefWindow? = null
    private val funcs = HashMap<String, (String) -> String>()

    @Serializable
    data class FuncArgs<T>(
        val name: String,
        val arg: T
    )

    override fun setup(srvMgr: ServiceManager) {
        this.srvMgr = srvMgr
    }

    inline fun <reified T, reified R>addMainFunc(name: String, noinline func: (T) -> R) {
        addMainFunc(name, func, Json.serializersModule.serializer(), Json.serializersModule.serializer())
    }

    fun <T, R>addMainFunc(
        name: String,
        func: (T) -> R,
        deserializer: DeserializationStrategy<FuncArgs<T>>,
        serializer: SerializationStrategy<R>
    ) {
        funcs[name] = {
            Json.encodeToString(serializer, func(Json.decodeFromString(deserializer, it).arg))
        }
    }

    @Serializable
    data class NotifyObject<T>(
        val name: String,
        val arg: T
    )

    inline fun <reified T>hookMainWindow(name: String, arg: T) {
        mainWindow?.executeHook(Json.encodeToString(NotifyObject(name, arg)))
    }

    fun runMainWindow() {
        val url = if(srvMgr.get<AssetService>().assetsPackaged) "http://asset" else "http://localhost:8080"
        mainWindow = CefWindow(url, srvMgr, funcs)
        while(mainWindow?.isVisible == true) {
            srvMgr.get<ThreadService>().runIWorkers()
        }
    }

}