package threads

import Service
import ServiceManager
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService

class ThreadService(private val corePoolSize: Int = 8) : Service {

    lateinit var pool: ScheduledExecutorService
        private set

    private lateinit var iwThread: IntermittentWorkerThread
    val iWorkers: CopyOnWriteArraySet<IntermittentWorker> get() = iwThread.workers

    override fun setup(srvMgr: ServiceManager) {
        pool = Executors.newScheduledThreadPool(corePoolSize)
        iwThread = IntermittentWorkerThread(CopyOnWriteArraySet<IntermittentWorker>())
    }

    override fun teardown() {
        pool.shutdown()
        iwThread.running = false
    }
}