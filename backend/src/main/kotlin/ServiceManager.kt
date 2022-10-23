import kotlin.reflect.KClass

interface Service {

    val deps: List<KClass<*>> get() = emptyList()

    fun setup(srvMgr: ServiceManager) { }
    fun teardown() { }
}

class ServiceManager {

    val services = HashMap<KClass<*>, Service>()
    var loadOrder: ArrayList<KClass<*>> = ArrayList()
    var initialized: Boolean = false
        private set

    inline fun <reified K: Service> get(): K {
        return services[K::class] as K
    }

    inline fun <reified K: Service> add(srv: K) {
        if(initialized) throw RuntimeException("initialized already")
        services[K::class] = srv
    }

    private data class InitState(
        val loaded: HashMap<KClass<*>, Service> = HashMap(),
        val unloaded: HashMap<KClass<*>, Service> = HashMap(),
        val loadStack: ArrayList<KClass<*>> = ArrayList(),
        val loadOrder: ArrayList<KClass<*>> = ArrayList(),
    )

    fun initialize() {
        if(initialized) return

        val state = InitState()

        state.unloaded.putAll(services.entries.map({ it.toPair() }))

        while(state.unloaded.isNotEmpty()) {
            initialize(state.unloaded.entries.first().toPair(), state)
        }

        loadOrder = state.loadOrder

        initialized = true
    }

    private fun initialize(toLoad: Pair<KClass<*>, Service>, state: InitState) {

        state.loadStack.add(toLoad.first)

        toLoad.second.deps.forEach({ dep ->
            if(state.loadStack.contains(dep))
                throw RuntimeException("circular dependency $dep at $state")

            if(state.unloaded.contains(dep)) {
                initialize(Pair(dep, state.unloaded[dep]!!), state)
            }
        })

        toLoad.second.setup(this)
        state.loaded[toLoad.first] = toLoad.second
        state.unloaded.remove(toLoad.first)
        state.loadOrder.add(toLoad.first)

        state.loadStack.removeLast()
    }

    fun teardown() {
        if(!initialized) return

        loadOrder.reversed().forEach({
            services[it]!!.teardown()
        })

        initialized = false
    }


}