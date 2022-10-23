package threads

import java.util.concurrent.CopyOnWriteArraySet

class IntermittentWorkerThread(val workers: CopyOnWriteArraySet<IntermittentWorker>): Thread("IWorkerThread") {

    init {
        start()
    }

    @Volatile
    var running = true
        set(value) {
            if(field && !value) {
                field = value
                join()
            }
        }

    override fun run() {
        while(running) {

            val it = workers.iterator()
            while(running && it.hasNext()) {
                val w = it.next()

                if(w.needsWork)
                    w.work()

                if(w.finished)
                    it.remove()
            }

        }
    }
}