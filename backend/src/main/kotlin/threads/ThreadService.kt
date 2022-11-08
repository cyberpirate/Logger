package threads

import Service
import ServiceManager
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class ThreadService(private val corePoolSize: Int = 8) : Service {

    lateinit var pool: ScheduledExecutorService
        private set

    val iWorkers = CopyOnWriteArraySet<IntermittentWorker>()

    override fun setup(srvMgr: ServiceManager) {
        pool = Executors.newScheduledThreadPool(corePoolSize)
    }

    fun runIWorkers(runFor: Long = 1000) {
        val stopAt = System.currentTimeMillis() + runFor

        while(System.currentTimeMillis() < stopAt) {
            val it = iWorkers.iterator()
            while (System.currentTimeMillis() < stopAt && it.hasNext()) {
                val w = it.next()

                if (w.needsWork)
                    w.work()

                if (w.finished)
                    it.remove()
            }
        }
    }

    override fun teardown() {
        pool.shutdown()
    }
}