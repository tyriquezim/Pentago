package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile

class MoveAchievementObserver: AchievementObserver
{
    val movesAchievementMap = HashMap<Int, Achievement>()

    init
    {
        lateinit var movesAchievement: Achievement

        for(i in 1..10)
        {
            movesAchievement = Achievement("Move Maker ${simpleRomanNumeralConverter(i)}", "Make a total of ${indexToStatisticFunction(i)} moves across all games!")
            movesAchievementMap.put(indexToStatisticFunction(i).toInt(), movesAchievement)
        }
    }
    override fun updateAchievements(playerStats: PlayerProfile.PlayerStatistics): MutableList<Achievement>
    {
        val movesMade = playerStats.totalMovesMade
        val desiredAchievement = movesAchievementMap.get(movesMade)
        val earnedAchievementList = ArrayList<Achievement>()

        //If an achievement corresponding to the number of draws exists, award it.
        if(desiredAchievement != null)
        {
            desiredAchievement.hasBeenEarned = true
            earnedAchievementList.add(desiredAchievement)
        }

        return earnedAchievementList
    }

    override fun indexToStatisticFunction(index: Int): Number
    {
        return 20 * index
    }

    override fun getAchievementList(): List<Achievement>
    {
        return movesAchievementMap.values.toList()
    }

}