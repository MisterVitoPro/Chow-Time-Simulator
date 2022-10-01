class Board {

    val spaces: List<Space> = buildList {
        repeat(NUM_OF_SPACES){ this.add(Space(it)) }
    }

    fun getFoodSpace(t: FoodType): Space {
        return spaces.first { it.foodType == t }
    }

    /**
     * Create Game Board Spaces and Connections
     */
    fun create(){
        spaces[0].type = SpaceType.STARTING
        spaces[0].addConnection(getSpaceById(1))
            .addConnection(getSpaceById(19))
            .addConnection(getSpaceById(33))
            .addConnection(getSpaceById(47))
            .addConnection(getSpaceById(61))

        (1..10).forEach{
            spaces[it].setType(SpaceType.ONE_WAY).addConnection(getSpaceById(it+1))
            when(it){
                4 -> spaces[it].setType(SpaceType.GOLDEN_DIE)
                7 -> spaces[it].setType(SpaceType.BONUS_DIE)
                10 -> spaces[it].setType(SpaceType.FOOD).foodType = FoodType.HAY
            }
        }

        (11..18).forEach{
            spaces[it].addConnection(getSpaceById(it-1)).addConnection(getSpaceById(it+1))
            when(it){
                12 -> spaces[it].setType(SpaceType.SHORTCUT).addConnection(getSpaceById(16), true)
                13,16 -> spaces[it].setType(SpaceType.BONUS_DIE)
                15 -> spaces[it].addConnection(getSpaceById(20))
                18 -> spaces[it].setType(SpaceType.GOLDEN_DIE)
            }
        }

        spaces[19].addConnection(getSpaceById(18)).addConnection(getSpaceById(0)) //Add Starting Space as connection
        spaces[20].addConnection(getSpaceById(15)).addConnection(getSpaceById(21)).setType(SpaceType.BONUS_DIE)

        (21..32).forEach{
            spaces[it].addConnection(getSpaceById(it-1)).addConnection(getSpaceById(it+1))
            when(it){
                24 -> spaces[it].setType(SpaceType.FOOD).foodType = FoodType.OAT
                26,32 -> spaces[it].setType(SpaceType.BONUS_DIE)
                29 -> spaces[it].setType(SpaceType.GOLDEN_DIE).addConnection(getSpaceById(34))
            }
        }
        spaces[33].addConnection(getSpaceById(32)).addConnection(getSpaceById(0)) //Add Starting Space as connection
        spaces[34].addConnection(getSpaceById(35)).addConnection(getSpaceById(15))

        (35..46).forEach{
            spaces[it].addConnection(getSpaceById(it-1)).addConnection(getSpaceById(it+1))
            when(it) {
                35, 40 -> spaces[it].setType(SpaceType.BONUS_DIE)
                38 -> spaces[it].setType(SpaceType.FOOD).foodType = FoodType.SEED
                41 -> spaces[it].setType(SpaceType.SHORTCUT).addConnection(getSpaceById(45), true)
                43 -> spaces[it].addConnection(getSpaceById(48))
                44 -> spaces[it].setType(SpaceType.GOLDEN_DIE)
            }
        }
        spaces[47].addConnection(getSpaceById(46)).addConnection(getSpaceById(0)).setType(SpaceType.BONUS_DIE) //Add Starting Space as connection
        spaces[48].addConnection(getSpaceById(43)).addConnection(getSpaceById(0)).setType(SpaceType.BONUS_DIE)

        (49..60).forEach {
            spaces[it].addConnection(getSpaceById(it - 1)).addConnection(getSpaceById(it + 1))
            when (it) {
                52 -> spaces[it].setType(SpaceType.FOOD).foodType = FoodType.CORN
                54 -> spaces[it].setType(SpaceType.GOLDEN_DIE)
                57 -> spaces[it].addConnection(getSpaceById(62))
                58, 60 -> spaces[it].setType(SpaceType.BONUS_DIE)
            }
        }

        spaces[61].addConnection(getSpaceById(60)).addConnection(getSpaceById(0)) //Add Starting Space as connection
        spaces[62].addConnection(getSpaceById(63)).addConnection(getSpaceById(57))

        (63..69).forEach {
            spaces[it].addConnection(getSpaceById(it - 1)).addConnection(getSpaceById(it + 1))
            when (it) {
                66 -> spaces[it].setType(SpaceType.FOOD).foodType = FoodType.GRASS
            }
        }
        spaces[70].addConnection(getSpaceById(69)).setType(SpaceType.SHORTCUT).addConnection(getSpaceById(0), true)
    }

    fun getSpaceById(id: Int): Space {
        return spaces.first { it.id == id }
    }

    fun printSpaces(){
        println(spaces.map { it.type })
    }

    fun BFS(id: Int, currentTarget: Int): List<Space>{
        //
        return listOf()
    }

 /*   var found: List<Int> = listOf()

    private fun DFSUtil(v: Int, visited: HashMap<Int, Boolean>, target: Int) {
        if(found.isNotEmpty()) return
        visited[v] = true

        val i: Iterator<Int> = spaces[v].getConnectedSpaces().map { it.id }.listIterator()
        while (i.hasNext()) {
            val n = i.next()
            if(n == target){
                visited[n] = true
                println("Found! ${visited.filter { it.value }.keys}")
                found = visited.filter { it.value }.keys.toList()
                return
            }else if(!visited[n]!!) DFSUtil(n, visited, target)
        }
    }

    fun DFS(startingId: Int, target: Int): List<Int> {
        println("Looking for Space: $target")
        // Mark all the vertices as
        // not visited(set as
        // false by default in java)
        found = listOf()
        val visited: HashMap<Int, Boolean> = spaces.associate { it.id to false } as HashMap<Int, Boolean>

        // Call the recursive helper
        // function to print DFS
        // traversal
        DFSUtil(startingId, visited, target)
        return found
    }
*/
    companion object {
        const val NUM_OF_SPACES = 71
    }

}

class Space(val id: Int) {

    var type: SpaceType = SpaceType.BASIC
    var foodType: FoodType = FoodType.NONE
    var connections = mutableListOf<Connection>()
        private set

    fun addConnection(space: Space, isShortcut: Boolean = false): Space {
        connections.add(Connection(space, isShortcut))
        return this
    }

    fun getConnectedSpaces(): List<Space>{
        return connections.map { it.space }.toList()
    }

    fun setType(t: SpaceType): Space {
        this.type = t
        return this
    }

}

data class Connection(val space:Space, val isShortcut:Boolean = false)

enum class SpaceType {
    BASIC,
    FOOD,
    STARTING,
    BONUS_DIE,
    GOLDEN_DIE,
    SHORTCUT,
    ONE_WAY
}