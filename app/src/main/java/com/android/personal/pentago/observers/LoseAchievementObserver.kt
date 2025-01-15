package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile

class LoseAchievementObserver: AchievementObserver
{
    val lossAchievementMap = HashMap<Int, Achievement>()

    init
    {
        lateinit var lossAchievement: Achievement

        for(i in 1..10)
        {
            lossAchievement = Achievement("Loser ${simpleRomanNumeralConverter(i)}", "Lose ${indexToStatisticFunction(i)} times!")
            lossAchievementMap.put(indexToStatisticFunction(i).toInt(), lossAchievement)
        }
    }
    override fun updateAchievements(playerStats: PlayerProfile.PlayerStatistics):MutableList<Achievement>
    {
        val numLosses = playerStats.numLosses
        val desiredAchievement = lossAchievementMap.get(numLosses)
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
        return 5 * index
    }
}