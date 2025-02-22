package com.android.personal.pentago

import android.content.Context
import androidx.room.Room
import com.android.personal.pentago.database.PlayerProfileDatabase
import com.android.personal.pentago.model.PlayerProfile
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val DATABASE_NAME = "player-profile-database"

class PentagoRepository private constructor(context: Context)
{
    private val database: PlayerProfileDatabase = Room.databaseBuilder(context.applicationContext, PlayerProfileDatabase::class.java, DATABASE_NAME).build()

    val mutex: Mutex = Mutex() //Mutex lock for accessing the database as different coroutines will perform operations on the database. Must be used manually to do work involving the database

    suspend fun insertPlayerProfile(playerProfile: PlayerProfile) = database.playerProfileDAO().insertPlayerProfile(playerProfile)

    suspend fun insertPlayerProfiles(playerProfiles: List<PlayerProfile>) = database.playerProfileDAO().insertPlayerProfiles(playerProfiles)

    suspend fun deletePlayerProfile(playerId: Int) = database.playerProfileDAO().deletePlayerProfile(playerId)

    suspend fun updatePlayerProfile(playerId: Int, modelProfile: PlayerProfile) = database.playerProfileDAO().updatePlayerProfile(playerId, modelProfile.userName, modelProfile.profilePicture, modelProfile.marbleColour, modelProfile.playerStats.numGamesPlayed, modelProfile.playerStats.numWins, modelProfile.playerStats.numLosses, modelProfile.playerStats.numDraws, modelProfile.playerStats.winPercentage, modelProfile.playerStats.totalMovesMade, modelProfile.playerStats.achievementObserversList)

    suspend fun updatePlayerProfileUserName(playerId: Int, newUserName: String) = database.playerProfileDAO().updatePlayerProfileUserName(playerId, newUserName)

    suspend fun updatePlayerProfileProfilePicture(playerId: Int, newProfilePicture: String) = database.playerProfileDAO().updatePlayerProfileProfilePicture(playerId, newProfilePicture)

    suspend fun updatePlayerProfileMarbleColour(playerId: Int, newMarbleColour: String) = database.playerProfileDAO().updatePlayerProfileMarbleColour(playerId, newMarbleColour)

    suspend fun updatePlayerProfilePlayerStats(playerId: Int, newPlayerStatistics: PlayerProfile.PlayerStatistics) = database.playerProfileDAO().updatePlayerProfilePlayerStats(playerId, newPlayerStatistics.numGamesPlayed, newPlayerStatistics.numWins, newPlayerStatistics.numLosses, newPlayerStatistics.numDraws, newPlayerStatistics.winPercentage, newPlayerStatistics.totalMovesMade, newPlayerStatistics.achievementObserversList)

    suspend fun getPlayerProfile(playerId: Int): PlayerProfile = database.playerProfileDAO().getPlayerProfile(playerId)

    suspend fun getPlayerProfiles(): List<PlayerProfile> = database.playerProfileDAO().getPlayerProfiles()

    companion object
    {
        private var INSTANCE: PentagoRepository? = null

        fun initialise(context: Context)
        {
            if(INSTANCE == null)
            {
                INSTANCE = PentagoRepository(context)
            }
        }

        fun get(): PentagoRepository
        {
            return INSTANCE ?: throw IllegalStateException("PentagoRepository must be initialised")
        }
    }
}