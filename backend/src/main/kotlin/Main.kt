import links.*
import threads.ThreadService
import ui.CefWindow
import ui.WindowManager
import utils.AssetService

fun main(args: Array<String>) {

    val srvMgr = ServiceManager()

    srvMgr.add(ThreadService())
    srvMgr.add(WindowManager())
    srvMgr.add(ChainService())
    srvMgr.add(AssetService())

    srvMgr.initialize()

    srvMgr.get<WindowManager>().runMainWindow()

    srvMgr.teardown()
}