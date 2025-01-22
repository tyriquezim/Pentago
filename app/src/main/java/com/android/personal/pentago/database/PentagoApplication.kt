package com.android.personal.pentago.database

import android.app.Application
import com.android.personal.pentago.PentagoRepository

//Class that is concerned with the application's lifecycle
class PentagoApplication: Application()
{
    override fun onCreate()
    {
        super.onCreate()
        PentagoRepository.initialise(this) //Instantiated in the Application class so that the repository is only initialised once during the lifetime of the app rather in an activity or fragment which can be destroyed and recreated frequently
    }
}