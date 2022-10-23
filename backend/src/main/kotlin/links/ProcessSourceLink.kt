package links

import threads.IntermittentWorker
import java.io.BufferedReader
import java.io.InputStreamReader

class ProcessSourceLink(private val process: Process): Link(), IntermittentWorker {

    private val stdout = BufferedReader(InputStreamReader(process.inputStream))

    override val needsWork: Boolean
        get() = stdout.ready()

    override val finished: Boolean
        get() = !(stdout.ready() || process.isAlive)

    override fun work() {
        acceptLine(stdout.readLine() ?: return)
    }

    override fun processLine(line: String): String {
        return line
    }

    override fun close() {
        process.destroy()
        super.close()
    }
}