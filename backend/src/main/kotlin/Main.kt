import links.*
import threads.ThreadService
import ui.CefWindow
import ui.WindowManager

fun main(args: Array<String>) {

//    val process = ProcessBuilder(listOf(
//            "bash", "-c", "for (( c=1; ; c++ )); do echo \"asdf: \$c\"; sleep 1; done;"
//        )).start()
//
//    val sLink = ProcessSourceLink(process)
//
//    sLink.addChild(object: Link() {
//        override fun processLine(line: String): String? {
//            println(line)
//            return null
//        }
//    })
//
//    process.waitFor()
//
//    JSONObject().toString()


    val srvMgr = ServiceManager()

    srvMgr.add(ThreadService())
    srvMgr.add(WindowManager())

    srvMgr.initialize()

    srvMgr.get<WindowManager>().runMainWindow()
//    val chain = buildChain(listOf(
//        LinkDesc("process", listOf("bash", "-c", "for (( c=1; ; c++ )); do echo \"asdf: \$c\"; sleep 1; done;")),
//        LinkDesc("log")
//    ))
//
//    srvMgr.get<ThreadService>().iWorkers.add(chain.source)
//
//    Thread.sleep(5000)

    srvMgr.teardown()
}