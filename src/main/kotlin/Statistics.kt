import jdk.dynalink.linker.ConversionComparator.Comparison

object Statistics {

    private var spaceHeatMap: HashMap<Int, Int> = hashMapOf()

    fun incrementSpaceHit(sId: Int) {
        spaceHeatMap.putIfAbsent(sId, 0)
        spaceHeatMap[sId] = spaceHeatMap[sId]!! + 1
    }

    fun print(){
        val sorted = spaceHeatMap.toList().sortedByDescending { (_, v) -> v }.toMap()
        println("Most Landed Spaces:\n${sorted.map { "${it.key}:${it.value}" }}")
    }
}