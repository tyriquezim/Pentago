package com.android.personal.pentago.observers

import com.android.personal.pentago.model.Achievement
import com.android.personal.pentago.model.PlayerProfile

interface AchievementObserver
{
    fun updateAchievements(playerStats: PlayerProfile.PlayerStatistics): MutableList<Achievement>

    //Each implementing class will use a forloop to decide the value its target statistic must climb between achievements. This function uses the forloop index to create the number that appears in the achievement description
    fun indexToStatisticFunction(index: Int): Number

    fun getAchievementList(): List<Achievement>

    //Will be used to name achievement levels
    fun simpleRomanNumeralConverter(number: Int): String
    {
        lateinit var romanNumeral: String

        romanNumeral = when(number)
        {
                1 -> "I"
                2 -> "II"
                3 -> "III"
                4 -> "IV"
                5 -> "V"
                6 -> "VI"
                7 -> "VII"
                8 -> "VIII"
                9 -> "IX"
                10 -> "X"
                else -> throw IllegalArgumentException("Function only converts numbers between 1 to 10 inclusive to roman numerals")
        }

        return romanNumeral
    }
}