package com.android.personal.pentago.model

import androidx.collection.ArraySet
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.personal.pentago.observers.AchievementObserver

@Entity
class PlayerProfile(userName: String, profilePicture: String, marbleColour: String)
{
    @PrimaryKey var userName: String = userName
        set(value)
        {
            //Enforcing username uniqueness when the username is being set/changed
            if(value in userNameSet)
            {
                throw IllegalArgumentException("This username has already been used. Please choose another.")
            }
            else
            {
                userNameSet.remove(field) //Removes the current username from the list of taken usernames
                field = value //Note: Setting to the property directly will cause the custom setter to be set recursively
                userNameSet.add(field) //Adds the new username to the list of taken usernames
            }
        }
    var profilePicture: String = profilePicture
        set(value)
        {
            if(value !in validProfilePicSet)
            {
                throw IllegalArgumentException("A valid profile picture string must be passed to PlayerProfile class constructor. Check the PlayerProfile class for the list of valid profile picture strings.")
            }
            else
            {
                field = value
            }
        }
    var marbleColour: String = marbleColour
        set(value)
        {
            //Checks if the colour passed to the constructor is valid
            if(marbleColour !in Marble.validColourSet)
            {
                throw IllegalArgumentException("A valid marble colour string must be passed to the PlayerProfile class constructor. Check the Marble class for the list of valid colours.")
            }
            else
            {
                field = value
            }
        }

    private val playerStats: PlayerStatistics = PlayerStatistics() //Object that stores the statistics of the player

    init
    {
        //Checks whether a valid profile picture string has been passed
        if(profilePicture !in validProfilePicSet)
        {
            throw IllegalArgumentException("A valid profile picture string must be passed to PlayerProfile class constructor. Check the PlayerProfile class for the list of valid profile picture strings.")
        }
        //Checks if the colour passed to the constructor is valid
        if(marbleColour !in Marble.validColourSet)
        {
            throw IllegalArgumentException("A valid marble colour string must be passed to the PlayerProfile class constructor. Check the Marble class for the list of valid colours.")
        }
        //Enforces that usernames be unique
        if(userName in userNameSet)
        {
            throw IllegalArgumentException("This username has already been used. Please choose another.")
        }
        else
        {
            userNameSet.add(userName)
        }
    }

    companion object
    {
        //Profile Picture constants

        const val AI_ROBOT_PP = "AI Bot"
        const val ANDROID_ROBOT_PP = "Android Robot"
        const val BEACH_PP = "Beach"
        const val DEFAULT_PP = "Default"
        const val DESERT_PP = "Desert"
        const val GIRAFFE_PP = "Giraffe"
        const val LION_PP = "Lion"
        const val MOUNTAIN_PP = "Mountain"
        const val OSTRICH_PP = "Ostrich"
        const val TIGER_PP = "Tiger"
        const val TREE_PP = "Tree"
        const val ZEBRA_PP = "Zebra"

        val validProfilePicSet = setOf(ANDROID_ROBOT_PP, BEACH_PP, DEFAULT_PP, DESERT_PP, GIRAFFE_PP, LION_PP, MOUNTAIN_PP, OSTRICH_PP, TIGER_PP, TREE_PP, ZEBRA_PP)
        val userNameSet: MutableSet<String> = ArraySet()
    }
    //Wrapper Functions for incrementing statistics
    fun updateWins(): MutableList<Achievement>
    {
        return playerStats.updateWins()
    }
    fun updateLosses(): MutableList<Achievement>
    {
        return playerStats.updateLosses()
    }

    fun updateDraws(): MutableList<Achievement>
    {
        return playerStats.updateDraws()
    }

    fun updateTotalMovesMade(): MutableList<Achievement>
    {
        return playerStats.updateTotalMovesMade()
    }

    fun getAchievements(): List<Achievement>
    {
        return playerStats.achievements
    }

    fun addAchievementObserver(observer: AchievementObserver)
    {
        playerStats.addAchievementObserver(observer)
    }

    fun removeAchievementObserver(observer: AchievementObserver)
    {
        playerStats.removeAchievementObserver(observer)
    }

    //Nested class that allows objects that store player statistics to be instantiated
    class PlayerStatistics
    {
        var numGamesPlayed: Int = 0
        var numWins: Int = 0
        var numLosses: Int = 0
        var numDraws: Int = 0
        var winPercentage: Double = 0.0
        var totalMovesMade: Int = 0
        val achievementObserversList = ArrayList<AchievementObserver>()
        var achievements = ArrayList<Achievement>()
            get()
            {
                val achievementList = ArrayList<Achievement>()

                for(observer in achievementObserversList)
                {
                    achievementList.addAll(observer.getAchievementList())
                }

                return achievementList
            }

        fun updateWins(): MutableList<Achievement>
        {
            val earnedAchievementList = ArrayList<Achievement>()

            ++numGamesPlayed
            ++numWins
            winPercentage = (numWins.toDouble() / numGamesPlayed.toDouble()) * 100

            for(observer in achievementObserversList)
            {
                earnedAchievementList.addAll(observer.updateAchievements(this))
            }

            return earnedAchievementList
        }

        fun updateLosses(): MutableList<Achievement>
        {
            val earnedAchievementList = ArrayList<Achievement>()

            ++numGamesPlayed
            ++numLosses
            winPercentage = (numWins.toDouble() / numGamesPlayed.toDouble()) * 100

            for(observer in achievementObserversList)
            {
                earnedAchievementList.addAll(observer.updateAchievements(this))
            }

            return earnedAchievementList
        }

        fun updateDraws(): MutableList<Achievement>
        {
            val earnedAchievementList = ArrayList<Achievement>()

            ++numGamesPlayed
            ++numDraws
            winPercentage = (numWins.toDouble() / numGamesPlayed.toDouble()) * 100

            for(observer in achievementObserversList)
            {
                earnedAchievementList.addAll(observer.updateAchievements(this))
            }

            return earnedAchievementList
        }

        fun updateTotalMovesMade(): MutableList<Achievement>
        {
            val earnedAchievementList = ArrayList<Achievement>()

            ++totalMovesMade

            for(observer in achievementObserversList)
            {
                earnedAchievementList.addAll(observer.updateAchievements(this))
            }

            return earnedAchievementList
        }

        fun addAchievementObserver(observer: AchievementObserver)
        {
            achievementObserversList.add(observer)
        }

        fun removeAchievementObserver(observer: AchievementObserver)
        {
            achievementObserversList.remove(observer)
        }
    }
}