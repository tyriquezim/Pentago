package com.android.personal.pentago.model

import androidx.collection.ArraySet
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.personal.pentago.PentagoRepository
import com.android.personal.pentago.observers.AchievementObserver
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.serialization.Serializable

@Serializable
@Entity
class PlayerProfile
{
    //Some properties that really should be declared with val are var so that ksp can stop complaining
    //Decided to move the responsibility of ensuring userNames and marbleColours are unique to the ProfileSettings class as Serialisation and deserialisation was complicating it due to it needing the constructor.
    @PrimaryKey var playerId: Int = totalPlayersCreated
    var userName: String = ""
        set(value)
        {
            activeUserNameSet.remove(field)
            field = value
            activeUserNameSet.add(field)
        }
    //Not placing them in the primary constructor allows me to have custom getters and setters while still having my class be Serialisable
    var profilePicture: String = DEFAULT_PP
        set(value)
        {
            if(value !in PlayerProfile.validProfilePicSet)
            {
                throw IllegalArgumentException("The Profile Picture, $value, is not a valid option. Check the PlayerProfile class companion object for a list of valid options.")
            }
            else
            {
                field = value
            }
        }
    var marbleColour: String = "Blank"
        set(value)
        {
            //Checks if the colour passed to the constructor is valid
            if(value !in Marble.validColourSet)
            {
                throw IllegalArgumentException("The marble colour, $value, is not a valid marble colour. Check the Marble class for the list of valid colours.")
            }
            else
            {
                activeMarbleColourSet.remove(field)
                field = value
                activeMarbleColourSet.add(field)
            }
        }
    @Embedded var playerStats: PlayerStatistics = PlayerStatistics() //Object that stores the statistics of the player

    constructor(userName: String, profilePicture: String, marbleColour: String)
    {
        this.userName =  userName
        this.profilePicture = profilePicture
        this.marbleColour = marbleColour

        //Increments the total number of existing players
        ++totalPlayersCreated
    }

    /*init
    {
        //Checks whether a valid profile picture string has been passed
        if(profilePicture !in validProfilePicSet)
        {
            throw IllegalArgumentException("${profilePicture} is not a valid profile picture string for a PlayerProfile object. A valid profile picture string must be passed to PlayerProfile class constructor. Check the PlayerProfile class for the list of valid profile picture strings.")
        }
        //Checks if the colour passed to the constructor is valid
        if(marbleColour !in Marble.validColourSet)
        {
            throw IllegalArgumentException("A valid marble colour string must be passed to the PlayerProfile class constructor. Check the Marble class for the list of valid colours.")
        }
        else
        {
            if(marbleColour in activeMarbleColourSet)
            {
                throw IllegalArgumentException("The marble colour, $marbleColour passed to the PlayerProfile class constructor is already in use. Choose another valid colour.")
            }
        }
        //Enforces that usernames be unique
        if(userName in activeUserNameSet)
        {
            throw IllegalArgumentException("This username, $userName, is already in use. Please choose another.")
        }
        else
        {
            activeUserNameSet.add(userName)
        }


    }*/

    companion object
    {
        private var totalPlayersCreated: Int = 0
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

        val validProfilePicSet = setOf(AI_ROBOT_PP, ANDROID_ROBOT_PP, BEACH_PP, DEFAULT_PP, DESERT_PP, GIRAFFE_PP, LION_PP, MOUNTAIN_PP, OSTRICH_PP, TIGER_PP, TREE_PP, ZEBRA_PP)
        val activeUserNameSet: MutableSet<String> = ArraySet()
        val activeMarbleColourSet: MutableSet<String> = ArraySet() //For storing marble colours. It exists to ensure no 2 players have the same marble colour at the same time.
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
        return playerStats.getAchievements()
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
    @Serializable
    class PlayerStatistics
    {
        var numGamesPlayed: Int = 0
        var numWins: Int = 0
        var numLosses: Int = 0
        var numDraws: Int = 0
        var winPercentage: Double = 0.0
        var totalMovesMade: Int = 0
        var achievementObserversList = ArrayList<AchievementObserver>()

        fun updateWins(): MutableList<Achievement>
        {
            lateinit var earnedAchievementList: ArrayList<Achievement>

            ++numGamesPlayed
            ++numWins
            winPercentage = (numWins.toDouble() / numGamesPlayed.toDouble()) * 100

            earnedAchievementList = notifyAchievementObservers()

            return earnedAchievementList
        }

        fun updateLosses(): MutableList<Achievement>
        {
            lateinit var earnedAchievementList: ArrayList<Achievement>

            ++numGamesPlayed
            ++numLosses
            winPercentage = (numWins.toDouble() / numGamesPlayed.toDouble()) * 100

            earnedAchievementList = notifyAchievementObservers()

            return earnedAchievementList
        }

        fun updateDraws(): MutableList<Achievement>
        {
            lateinit var earnedAchievementList: ArrayList<Achievement>

            ++numGamesPlayed
            ++numDraws
            winPercentage = (numWins.toDouble() / numGamesPlayed.toDouble()) * 100

            earnedAchievementList = notifyAchievementObservers()

            return earnedAchievementList
        }

        fun updateTotalMovesMade(): MutableList<Achievement>
        {
            lateinit var earnedAchievementList: ArrayList<Achievement>

            ++totalMovesMade

            earnedAchievementList = notifyAchievementObservers()

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

        fun getAchievements(): List<Achievement>
        {
            val achievementList = ArrayList<Achievement>()

            for(observer in achievementObserversList)
            {
                achievementList.addAll(observer.getAchievementList())
            }

            return achievementList
        }

        private fun notifyAchievementObservers(): ArrayList<Achievement>
        {
            val earnedAchievementList = ArrayList<Achievement>()

            for(observer in achievementObserversList)
            {
                earnedAchievementList.addAll(observer.updateAchievements(this))
            }

            return earnedAchievementList
        }
    }
}