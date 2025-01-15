package com.android.personal.pentago.model

import android.util.ArraySet
import java.util.Date

class Achievement(achievementTitle: String, achievementDescription: String)
{
    val achievementTitle: String = achievementTitle
    val achievementDescription: String = achievementDescription
    var hasBeenEarned: Boolean = false
        set(value)
        {
            if(field && !value)
            {
                throw IllegalArgumentException("Cannot set the hasBeenEarned property back to false once it has been set to true (Cannot 'lose' earned achievements).")
            }
            if(value)
            {
                date = Date() //If the achievement has been earned, log the exact date and time
            }
            field = value
        }
    var date: Date? = null
}