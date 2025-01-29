package com.android.personal.pentago

import androidx.lifecycle.ViewModel

class ProfileSettingsViewModel: ViewModel()
{
    lateinit var currentPlayer1UsernameString: String
    lateinit var currentPlayer2UsernameString: String

    fun hasP1UsernameStringInitialised(): Boolean = ::currentPlayer1UsernameString.isInitialized

    fun hasP2UsernameStringInitialised(): Boolean = ::currentPlayer2UsernameString.isInitialized

}