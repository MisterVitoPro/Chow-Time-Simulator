import Statistics.incrementSpaceHit

class Game(private val numOfPlayers: Int) {

    private val board: Board = Board()
    private val foodBag: FoodTokenBag = FoodTokenBag()
    private var players: List<Player> = listOf()
    var currentTurn = 0

    fun setup(){
        println("Initializing Setup")
        board.create()
        foodBag.create()
        players = listOf(Player(0), Player(1))

        for(p in players){
            val token = foodBag.drawFromBag()
            p.currentToken = token
            p.currentTarget = board.getFoodSpace(token).id
        }

    }

    fun run(){
        println("Running Game")
        var currentPlayer = players[0]
        while(!winConditionMet(currentPlayer) && currentTurn < MAX_TURNS_BEFORE_ENDING_GAME){
            currentPlayer = nextPlayersTurn()
            val startingSpace: Space = board.getSpaceById(currentPlayer.currentSpaceId)
            val spaceToMove:Int = rollDice(startingSpace.type)
            val landedSpace = movePlayer(spaceToMove, currentPlayer)
            landingSpaceCheck(landedSpace, currentPlayer)
        }
        if(currentTurn == MAX_TURNS_BEFORE_ENDING_GAME){
            println("!!!!! No Winner after $MAX_TURNS_BEFORE_ENDING_GAME Turns !!!!!")
        } else {
            println("!!!!! Player ${currentPlayer.id} Wins !!!!!")
        }
        Statistics.print()
    }

    private fun nextPlayersTurn(): Player {
        currentTurn += 1
        val p = players[currentTurn%numOfPlayers]
        println("=== Turn $currentTurn with Player ${p.id} (${p.goldCoins})===")
        return p
    }

    private fun rollDice(space: SpaceType): Int {
        return when(space){
            SpaceType.BONUS_DIE -> Dice.rollTurnDie(true)
            SpaceType.GOLDEN_DIE -> Dice.rollGoldenDie()
            else -> Dice.rollGoldenDie()
        }
    }

    private fun movePlayer(spacesToMove: Int, p: Player): Space {
        println("Starting: ${p.currentSpaceId}")
        val startSpace = p.currentSpaceId
        var current: Space = board.spaces[startSpace]

        val spacesToTraverse = board.BFS(current.id, p.currentTarget)
        for( i in 0 until spacesToMove) {
            current = spacesToTraverse[i]
            println("Moving to ${current.id} [${current.type}]")
            if(current.type == SpaceType.STARTING && current.type == SpaceType.FOOD) {
                break
            }
        }

        //current = board.getSpaceById(spacesToTraverse[spacesToMove])
//        repeat(spacesToMove) {
//            current =  current.getConnectedSpaces()
//            //println("Moved to ${current.id}")
//        }
        p.currentSpaceId = current.id
        incrementSpaceHit(p.currentSpaceId)
        println("Ending: ${p.currentSpaceId}")
        return current
    }

    private fun landingSpaceCheck(s: Space, p:Player){
        println("Player ${p.id} landed on ${s.id} - ${s.type}")
        when(s.type){
            SpaceType.FOOD -> feedAnimal(s,p)
            SpaceType.STARTING -> drawFromTokenBag(p)
            SpaceType.SHORTCUT -> takeShortcut(s, p)
            else -> return
        }
    }

    private fun takeShortcut(s: Space, p:Player){
        p.currentSpaceId = s.connections.first { it.isShortcut }.space.id
        println("Player takes shortcut to Space ${p.currentSpaceId}")
    }

    private fun feedAnimal(s: Space, p:Player) {
        if(p.currentToken == s.foodType){
            p.currentToken = FoodType.NONE
            p.rewardCoins()
        }
    }

    private fun drawFromTokenBag(p: Player){
        if(p.currentToken == FoodType.NONE && p.goldCoins < COINS_REQUIRED_TO_WIN) {
            p.currentToken = foodBag.drawFromBag()
            println("Player ${p.id} drew a ${p.currentToken} Food Token.")
        } else {
            println("Player ${p.id} on Starting Space already has a token: ${p.currentToken}.")
        }
    }

    private fun winConditionMet(p:Player?): Boolean {
        return if (p != null) {
             p.currentGoldCoins() == COINS_REQUIRED_TO_WIN && board.getSpaceById(p.currentSpaceId).type == SpaceType.STARTING
        } else {
            println("Player is null")
            false
        }
    }

    companion object {
        const val COINS_GIVEN_FOR_FEEDING = 2
        const val COINS_REQUIRED_TO_WIN = 6
        const val MAX_TURNS_BEFORE_ENDING_GAME = 2000
    }

}

class FoodTokenBag {

    private var bag: MutableList<FoodType> = mutableListOf()

    fun create(){
        for(f in FoodType.values()){
            if(f == FoodType.NONE) continue
            repeat(NUM_OF_EACH_TOKENS) { bag.add(f) }
        }
        bag.shuffle()
    }

    fun drawFromBag(): FoodType {
        println(bag.size)
        return bag.removeFirst()
    }

    companion object {
        const val NUM_OF_EACH_TOKENS = 3
    }

}

