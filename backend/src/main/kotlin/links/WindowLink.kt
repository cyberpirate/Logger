package links

import ServiceManager
import kotlinx.serialization.Serializable
import ui.WindowManager

class WindowLink(val srvMgr: ServiceManager, val name: String): Link() {

    @Serializable
    data class LineHook(val name: String, val line: String)

    override fun processLine(line: String): String? {
        println("WindowLink: $name: $line")
        srvMgr.get<WindowManager>().hookMainWindow("line", LineHook(name, line))
        return null
    }
}