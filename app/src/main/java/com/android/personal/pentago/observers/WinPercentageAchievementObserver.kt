package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile

class WinPercentageAchievementObserver: AchievementObserver
{
    val winPercentageAchievementMap = HashMap<String, Achievement>()

    init
    {
        lateinit var winPercentageAchievement: Achievement

        winPercentageAchievement = Achievement("Pentago Beginner", "Achieve a win percentage greater than 60% after playing more than 10 games.")
        winPercentageAchievementMap.put("Beginner", winPercentageAchievement)

        winPercentageAchievement = Achievement("Pentago Pro", "Achieve a win percentage greater than 80% after playing more than 10 games.")
        winPercentageAchievementMap.put("Pro", winPercentageAchievement)

        winPercentageAchievement = Achievement("Falling Marble", "Achieve a win percentage less than 40% after playing more than 10 games.")
        winPercentageAchievementMap.put("Falling", winPercentageAchievement)

        winPercentageAchievement = Achievement("Ultimate Loser", "Achieve a win percentage less than 20%. after playing more than 10 games")
        winPercentageAchievementMap.put("Loser", winPercentageAchievement)
    }
    override fun updateAchievements(playerStats: PlayerProfile.PlayerStatistics): MutableList<Achievement>
    {
        val winPerc = playerStats.winPercentage
        val numGames = playerStats.numGamesPlayed
        var desiredAchievment: Achievement? = null
        val earnedAchievementList = ArrayList<Achievement>()

        if(winPerc > 60 && numGames > 10)
        {
            desiredAchievment= winPercentageAchievementMap.get("Beginner")
            checkNotNull(desiredAchievment) {"Could not retrieve the Pentago Beginner achievement. Check code."}
            desiredAchievment.hasBeenEarned = true
            earnedAchievementList.add(desiredAchievment)
        }
        if(winPerc > 80 && numGames > 10)
        {
            desiredAchievment = winPercentageAchievementMap.get("Pro")
            checkNotNull(desiredAchievment) {"Could not retrieve the Pentago Pro achievement. Check code."}
            desiredAchievment.hasBeenEarned = true
            earnedAchievementList.add(desiredAchievment)
        }
        if(winPerc < 40 && numGames > 10)
        {
            desiredAchievment = winPercentageAchievementMap.get("Falling")
            checkNotNull(desiredAchievment) {"Could not retrieve the Falling Marble achievement. Check code."}
            desiredAchievment.hasBeenEarned = true
            earnedAchievementList.add(desiredAchievment)
        }
        if(winPerc < 20 && numGames > 10)
        {
            desiredAchievment = winPercentageAchievementMap.get("Loser")
            checkNotNull(desiredAchievment) {"Could not retrieve the Ultimate Loser achievement. Check code."}
            desiredAchievment.hasBeenEarned = true
            earnedAchievementList.add(desiredAchievment)
        }

        return earnedAchievementList
    }

    override fun indexToStatisticFunction(index: Int): Number
    {
        return index
    }
}