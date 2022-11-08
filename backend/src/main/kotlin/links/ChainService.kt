package links

import Service
import ServiceManager
import kotlinx.serialization.Serializable
import threads.ThreadService
import ui.WindowManager
import kotlin.reflect.KClass

class ChainService: Service {

    override val deps: List<KClass<*>>
        get() = listOf(WindowManager::class)

    @Serializable
    data class ChainArg(
        val name: String,
        val desc: ChainDesc
    )

    private val chains = HashMap<String, Chain>()

    override fun setup(srvMgr: ServiceManager) {

        srvMgr.get<WindowManager>().addMainFunc<ChainArg, Boolean>("createChain", {
            if(chains.containsKey(it.name)) {
                false
            } else {
                chains[it.name] = buildChain(it.desc)
                chains[it.name]?.addLink(WindowLink(srvMgr, it.name))
                chains[it.name]?.let({
                    srvMgr.get<ThreadService>().iWorkers.add(it.source)
                })
                true
            }
        })

    }
}