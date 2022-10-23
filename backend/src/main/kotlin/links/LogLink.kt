package links

class LogLink: Link() {
    override fun processLine(line: String): String? {
        println(line)
        return null
    }
}