package links

import java.io.Closeable
import java.lang.RuntimeException
import java.util.concurrent.CopyOnWriteArraySet

abstract class Link: Closeable {

    private var parent: Link? = null
    private val children = CopyOnWriteArraySet<Link>()
    val hasChildren: Boolean = children.isNotEmpty()

    protected abstract fun processLine(line: String): String?

    fun acceptLine(line: String) {
        val pLine = processLine(line) ?: return

        children.forEach({
            it.acceptLine(pLine)
        })
    }

    fun addChild(link: Link) {
        if(link.parent != null) throw RuntimeException("already has parent")
        children.add(link)
        link.parent = this
    }

    override fun close() {
        parent?.children?.remove(this)

        if(parent?.hasChildren == false) {
            parent?.close()
        }

        parent = null
    }
}