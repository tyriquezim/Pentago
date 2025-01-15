package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile

class DrawAchievementObserver: AchievementObserver
{
    val drawAchievementMap = HashMap<Int, Achievement>()

    init
    {
        lateinit var drawAchievement: Achievement

        for(i in 1..10)
        {
            drawAchievement = Achievement("Draw ${simpleRomanNumeralConverter(i)}", "Draw ${indexToStatisticFunction(i)} times!")
            drawAchievementMap.put(indexToStatisticFunction(i).toInt(), drawAchievement)
        }
    }

    override fun updateAchievements(playerStats: PlayerProfile.PlayerStatistics): MutableList<Achievement>
    {
        val numDraws = playerStats.numDraws
        val desiredAchievement = drawAchievementMap.get(numDraws)
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
        return index
    }

    override fun getAchievementList(): List<Achievement>
    {
        return drawAchievementMap.values.toList()
    }
}