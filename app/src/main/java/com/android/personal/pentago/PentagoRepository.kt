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

    private val mutex: Mutex = Mutex() //Mutex lock for accessing the database as different coroutines will perform operations on the database. Outside class don't need to worry about preventing race conditions for database access

    suspend fun insertPlayerProfile(playerProfile: PlayerProfile)
    {
        mutex.withLock()
        {
            database.playerProfileDAO().insertPlayerProfile(playerProfile)
        }
    }

    suspend fun insertPlayerProfiles(playerProfiles: List<PlayerProfile>)
    {
        mutex.withLock()
        {
            database.playerProfileDAO().insertPlayerProfiles(playerProfiles)
        }
    }

    suspend fun deletePlayerProfile(playerId: Int)
    {
        mutex.withLock()
        {
            database.playerProfileDAO().deletePlayerProfile(playerId)
        }
    }

    suspend fun updatePlayerProfile(playerId: Int, modelProfile: PlayerProfile)
    {
        mutex.withLock()
        {
            database.playerProfileDAO().updatePlayerProfile(playerId, modelProfile.userName, modelProfile.profilePicture, modelProfile.marbleColour)
        }
    }

    suspend fun getPlayerProfile(playerId: Int): PlayerProfile
    {
        lateinit var desiredPlayerProfile: PlayerProfile

        mutex.withLock()
        {
            desiredPlayerProfile = database.playerProfileDAO().getPlayerProfile(playerId)
        }

        return  desiredPlayerProfile
    }

    suspend fun getPlayerProfiles(): List<PlayerProfile>
    {
        lateinit var playerProfileList: List<PlayerProfile>
        mutex.withLock()
        {
            playerProfileList = database.playerProfileDAO().getPlayerProfiles()
        }

        return playerProfileList
    }

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