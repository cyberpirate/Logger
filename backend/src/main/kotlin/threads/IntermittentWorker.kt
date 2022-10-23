package threads

interface IntermittentWorker {

    val needsWork: Boolean
    val finished: Boolean

    fun work()

}