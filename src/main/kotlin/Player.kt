import Game.Companion.COINS_GIVEN_FOR_FEEDING
import Game.Companion.COINS_REQUIRED_TO_WIN

class Player(val id: Int) {

    var goldCoins: Int = 0
        private set
    var currentToken: FoodType = FoodType.NONE
    var currentSpaceId: Int = 0
    var currentTarget: Int = 0

    fun rewardCoins(){
        goldCoins += COINS_GIVEN_FOR_FEEDING
        println("Player ${this.id} successfully earned $COINS_GIVEN_FOR_FEEDING coins!")
    }

    fun currentGoldCoins():Int {
        return goldCoins
    }

    fun findPath(startPos: Int,b: Board){
        val start = b.getSpaceById(startPos)



    }

}