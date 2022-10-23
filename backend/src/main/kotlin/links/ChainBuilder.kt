package links

data class LinkDesc(
    val linkName: String,
    val args: List<String> = emptyList()
)

typealias ChainDesc = List<LinkDesc>

fun buildLink(linkDesc: LinkDesc): Link {
    return when(linkDesc.linkName) {
        "process" -> ProcessSourceLink(ProcessBuilder(linkDesc.args).start())
        "log" -> LogLink()
        else -> throw RuntimeException("unknown link: ${linkDesc.linkName}")
    }
}

fun buildChain(chainDesc: ChainDesc): Chain {
    require(chainDesc.isNotEmpty()) { "chain must have at least one link" }

    val source = buildLink(chainDesc.first())

    require(source is ProcessSourceLink) { "first link must be a source link" }

    val chain = Chain(source)

    chainDesc.subList(1, chainDesc.size).forEach({
        chain.addLink(buildLink(it))
    })

    return chain
}