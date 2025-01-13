package com.android.personal.pentago.model

class Marble(val marbleOwnerUsername: String, val marbleColour: String)
{
    init
    {
        //Checks if the colour passed to the constructor is valid
        if(marbleColour !in Marble.validColourList)
        {
            throw IllegalArgumentException("A valid colour must be passed to the Marble class constructor. Check the Marble class for the list of valid colours.")
        }
    }

    //Storing class constants that will be used to create disc objects
    companion object
    {
        const val BLACK_MARBLE = "Black"
        const val BLUE_MARBLE = "Blue"
        const val GREEN_MARBLE = "Green"
        const val ORANGE_MARBLE = "Orange"
        const val PINK_MARBLE = "Pink"
        const val PURPLE_MARBLE = "Purple"
        const val RED_MARBLE = "Red"
        const val YELLOW_MARBLE = "Yellow"
        const val METALLIC_MARBLE = "Metallic"

        val validColourList = listOf(BLACK_MARBLE, BLUE_MARBLE, GREEN_MARBLE, ORANGE_MARBLE, PINK_MARBLE, PURPLE_MARBLE, RED_MARBLE, YELLOW_MARBLE, METALLIC_MARBLE)
    }
}