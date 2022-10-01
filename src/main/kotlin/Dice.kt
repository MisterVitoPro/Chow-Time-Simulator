import java.util.concurrent.ThreadLocalRandom

object Dice {

    private val whiteDie1: Array<Int> = arrayOf(0,1,1,2,2,2)
    private val whiteDie2: Array<Int> = arrayOf(1,1,2,2,3,3)
    private val bonusDie: Array<Int> = arrayOf(0,1,1,1,2,2)
    private val goldenDie: Array<Int> = arrayOf(5,5,5,6,6,6)

    fun rollTurnDie(withBonus: Boolean = false): Int {
       var mainRoll = whiteDie1.random() + whiteDie2.random()
        if(withBonus){
            mainRoll += bonusDie.random()
        }
        println("Rolled a $mainRoll")
        return mainRoll
    }

    fun rollGoldenDie(): Int {
        val roll = goldenDie.random()
        println("Rolled a $roll")
        return roll
    }
}