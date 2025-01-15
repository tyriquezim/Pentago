package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile

class WinAchievementObserver: AchievementObserver
{
    val winAchievementMap = HashMap<Int, Achievement>()

    init
    {
        lateinit var winAchievement: Achievement

        for(i in 1..10)
        {
            winAchievement = Achievement("Draw ${simpleRomanNumeralConverter(i)}", "Draw ${indexToStatisticFunction(i)} times!")
            winAchievementMap.put(indexToStatisticFunction(i).toInt(), winAchievement)
        }
    }
    override fun updateAchievements(playerStats: PlayerProfile.PlayerStatistics): MutableList<Achievement>
    {
        val numWins = playerStats.numWins
        val desiredAchievement = winAchievementMap.get(numWins)
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