package links

class Chain(val source: ProcessSourceLink) {

    private var chainEnd: Link = source

    fun addLink(link: Link) {
        chainEnd.addChild(link)
        chainEnd = link
    }

}