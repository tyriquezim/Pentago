package com.android.personal.pentago.model

import android.util.ArraySet
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
class Achievement(var achievementTitle: String, var achievementDescription: String)
{
    var hasBeenEarned: Boolean = false
        set(value)
        {
            if(field && !value)
            {
                throw IllegalArgumentException("Cannot set the hasBeenEarned property back to false once it has been set to true (Cannot 'lose' earned achievements).")
            }
            if(value)
            {
                date = Clock.System.now().toLocalDateTime(TimeZone.of(ZoneId.systemDefault().toString())) //If the achievement has been earned, log the exact date and time.
            }
            field = value
        }
    var date: LocalDateTime? = null
    var hasBeenDisplayed = false
}