package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile

class GamesPlayedAchievementObserver: AchievementObserver
{
    val gamesPlayedAchievementMap = HashMap<Int, Achievement>()

    init
    {
        lateinit var gamesPlayedAchievement: Achievement

        for(i in 1..10)
        {
            gamesPlayedAchievement = Achievement("Pentago Player Master ${simpleRomanNumeralConverter(i)}", "Play a total of ${indexToStatisticFunction(i)} Pentago games!")
            gamesPlayedAchievementMap.put(indexToStatisticFunction(i).toInt(), gamesPlayedAchievement)
        }
    }
    override fun updateAchievements(playerStats: PlayerProfile.PlayerStatistics): MutableList<Achievement>
    {
        val numGamesPlayed = playerStats.numGamesPlayed
        val desiredAchievement = gamesPlayedAchievementMap.get(numGamesPlayed)
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
        return 10 * index
    }
}