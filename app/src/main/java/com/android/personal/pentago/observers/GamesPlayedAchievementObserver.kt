package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("GamesPlayedAchievementObserver")
class GamesPlayedAchievementObserver: AchievementObserver
{
    val gamesPlayedAchievementMap = HashMap<Int, Achievement>()

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

    override fun getAchievementList(): List<Achievement>
    {
        return gamesPlayedAchievementMap.values.toList()
    }

    override fun initialiseAchievementMap()
    {
        lateinit var gamesPlayedAchievement: Achievement

        for(i in 1..10)
        {
            gamesPlayedAchievement = Achievement("Pentago Player Master ${simpleRomanNumeralConverter(i)}","Play a total of ${indexToStatisticFunction(i)} Pentago games!")
            gamesPlayedAchievementMap.put(indexToStatisticFunction(i).toInt(), gamesPlayedAchievement)
        }
    }
}