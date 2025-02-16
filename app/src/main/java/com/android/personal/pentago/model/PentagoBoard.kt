package com.android.personal.pentago.model

import androidx.collection.emptyLongSet
import kotlin.random.Random

class PentagoBoard(val player1Profile: PlayerProfile, val player2Profile: PlayerProfile, val againstAiOpponent: Boolean)
{
    var pentagoBoard = Array<Array<Marble?>>(6) { Array<Marble?>(6) { null } } //6x6 array which represent the pentago board
    private var currentTurnPlayerProfile = player1Profile

    fun playTurn(rowNumber: Int, columnNumber: Int, subgridString: String, rotationString: String)
    {
        placeMarble(rowNumber, columnNumber)
        rotateSubGrid(subgridString, rotationString)

        if(currentTurnPlayerProfile.equals(player1Profile))
        {
            currentTurnPlayerProfile = player2Profile
        }
        else
        {
            if(currentTurnPlayerProfile.equals(player2Profile))
            {
                currentTurnPlayerProfile = player1Profile
            }
        }
    }

    private fun aiPlayTurn()
    {
        var rowNumber = Random.nextInt(0, 6)
        var columnNumber = Random.nextInt(0, 6)
        var subgridInt = Random.nextInt(0, 3)
        var rotationInt = Random.nextInt(0, 1)
        var hasCellBeenFound: Boolean = false
        val newMarble = Marble(player2Profile, player2Profile.marbleColour) //The AI player will always be player2

        if(!againstAiOpponent)
        {
            throw IllegalStateException("Cannot play AI opponent's turn. This Pentago game is between human players.")
        }

        while(!hasCellBeenFound)
        {
            if(pentagoBoard[rowNumber][columnNumber] == null)
            {
                hasCellBeenFound = true
                pentagoBoard[rowNumber][columnNumber] = newMarble
            }
            else
            {
                rowNumber = Random.nextInt(0, 6)
                columnNumber = Random.nextInt(0, 6)
            }
        }

        if(rotationInt == 0) //Clockwise
        {
            if(subgridInt == 0) //Top Left grid
            {
                rotateSubGrid(TOP_LEFT_SUBGRID, CLOCKWISE_ROTATION)
            }
            else
            {
                if(subgridInt == 1)
                {
                    rotateSubGrid(TOP_RIGHT_SUBGRID, CLOCKWISE_ROTATION)
                }
                else
                {
                    if(subgridInt == 2)
                    {
                        rotateSubGrid(BOTTOM_LEFT_SUBGRID, CLOCKWISE_ROTATION)
                    }
                    else
                    {
                        if(subgridInt == 3)
                        {
                            rotateSubGrid(BOTTOM_RIGHT_SUBGRID, CLOCKWISE_ROTATION)
                        }
                    }
                }
            }
        }
        else
        {
            if(rotationInt == 1) //Anti-clockwise
            {
                if(subgridInt == 0) //Top Left grid
                {
                    rotateSubGrid(TOP_LEFT_SUBGRID, ANTI_CLOCKWISE_ROTATION)
                }
                else
                {
                    if(subgridInt == 1)
                    {
                        rotateSubGrid(TOP_RIGHT_SUBGRID, ANTI_CLOCKWISE_ROTATION)
                    }
                    else
                    {
                        if(subgridInt == 2)
                        {
                            rotateSubGrid(BOTTOM_LEFT_SUBGRID, ANTI_CLOCKWISE_ROTATION)
                        }
                        else
                        {
                            if(subgridInt == 3)
                            {
                                rotateSubGrid(BOTTOM_RIGHT_SUBGRID, ANTI_CLOCKWISE_ROTATION)
                            }
                        }
                    }
                }
            }
        }

        currentTurnPlayerProfile = player1Profile
    }

    private fun placeMarble(rowNumber: Int, columnNumber: Int)
    {
        val newMarble = Marble(currentTurnPlayerProfile, currentTurnPlayerProfile.marbleColour)

        if(rowNumber < 0 || rowNumber > 5)
        {
            throw IllegalArgumentException("Illegal move! A marble can only be placed in a row with index 0 to 5")
        }
        if(columnNumber < 0 || columnNumber > 5)
        {
            throw IllegalArgumentException("Illegal move! A marble can only be placed in a column with index 0 to 5")
        }

        if(pentagoBoard[rowNumber][columnNumber] != null)
        {
            throw IllegalStateException("Cannot place marble at position $rowNumber, $columnNumber. There is already a marble in this cell.")
        }
        else
        {
            pentagoBoard[rowNumber][columnNumber] = newMarble
        }
    }

    private fun rotateSubGrid(subgridString: String, rotationString: String)
    {
        if(subgridString == TOP_LEFT_SUBGRID)
        {
            rotateTopLeftSubgrid(rotationString)
        }
        else
        {
            if(subgridString == TOP_RIGHT_SUBGRID)
            {
                rotateTopRightSubgrid(rotationString)
            }
            else
            {
                if(subgridString == BOTTOM_LEFT_SUBGRID)
                {
                    rotateBottomLeftSubgrid(rotationString)
                }
                else
                {
                    if(subgridString == BOTTOM_RIGHT_SUBGRID)
                    {
                        rotateBottomRightSubgrid(rotationString)
                    }
                }
            }
        }
    }

    private fun rotateTopLeftSubgrid(rotationString: String)
    {
        val topLeftSubGrid = Array(3) { Array<Marble?>(3) { null } }

        for(i in 0..2)
        {
            for(j in 0..2)
            {
                topLeftSubGrid[i][j] = pentagoBoard[i][j]
            }
        }

        if(rotationString == CLOCKWISE_ROTATION)
        {
            //Top Row
            pentagoBoard[0][0] = topLeftSubGrid[2][0]
            pentagoBoard[0][1] = topLeftSubGrid[1][0]
            pentagoBoard[0][2] = topLeftSubGrid[0][0]

            //Middle Row
            pentagoBoard[1][0] = topLeftSubGrid[2][1]
            pentagoBoard[1][1] = topLeftSubGrid[1][1]
            pentagoBoard[1][2] = topLeftSubGrid[0][1]

            //Bottom Row
            pentagoBoard[2][0] = topLeftSubGrid[2][2]
            pentagoBoard[2][1] = topLeftSubGrid[1][2]
            pentagoBoard[2][2] = topLeftSubGrid[0][2]
        }
        else
        {
            if(rotationString == ANTI_CLOCKWISE_ROTATION)
            {
                //Bottom Row
                pentagoBoard[2][2] = topLeftSubGrid[2][0]
                pentagoBoard[2][1] = topLeftSubGrid[1][0]
                pentagoBoard[2][0] = topLeftSubGrid[0][0]

                //Middle Row
                pentagoBoard[1][2] = topLeftSubGrid[2][1]
                pentagoBoard[1][1] = topLeftSubGrid[1][1]
                pentagoBoard[1][0] = topLeftSubGrid[0][1]

                //Top Row
                pentagoBoard[0][2] = topLeftSubGrid[2][2]
                pentagoBoard[0][1] = topLeftSubGrid[1][2]
                pentagoBoard[0][0] = topLeftSubGrid[0][2]
            }
        }
    }

    private fun rotateTopRightSubgrid(rotationString: String)
    {
        val topRightSubGrid = Array(3) { Array<Marble?>(3) { null } }

        for(i in 0..2)
        {
            for(j in 0..2)
            {
                topRightSubGrid[i][j] = pentagoBoard[i][3 + j] //Added to two because the top right subgrid has column indices from 3 to 5
            }
        }

        if(rotationString == CLOCKWISE_ROTATION)
        {
            //Top Row
            pentagoBoard[0][3] = topRightSubGrid[2][0]
            pentagoBoard[0][4] = topRightSubGrid[1][0]
            pentagoBoard[0][5] = topRightSubGrid[0][0]

            //Middle Row
            pentagoBoard[1][3] = topRightSubGrid[2][1]
            pentagoBoard[1][4] = topRightSubGrid[1][1]
            pentagoBoard[1][5] = topRightSubGrid[0][1]

            //Bottom Row
            pentagoBoard[2][3] = topRightSubGrid[2][2]
            pentagoBoard[2][4] = topRightSubGrid[1][2]
            pentagoBoard[2][5] = topRightSubGrid[0][2]
        }
        else
        {
            if(rotationString == ANTI_CLOCKWISE_ROTATION)
            {
                //Bottom Row
                pentagoBoard[2][5] = topRightSubGrid[2][0]
                pentagoBoard[2][4] = topRightSubGrid[1][0]
                pentagoBoard[2][3] = topRightSubGrid[0][0]

                //Middle Row
                pentagoBoard[1][5] = topRightSubGrid[2][1]
                pentagoBoard[1][4] = topRightSubGrid[1][1]
                pentagoBoard[1][3] = topRightSubGrid[0][1]

                //Top Row
                pentagoBoard[0][5] = topRightSubGrid[2][2]
                pentagoBoard[0][4] = topRightSubGrid[1][2]
                pentagoBoard[0][3] = topRightSubGrid[0][2]
            }
        }
    }

    private fun rotateBottomLeftSubgrid(rotationString: String)
    {
        val bottomLeftSubGrid = Array(3) { Array<Marble?>(3) { null } }

        for(i in 0..2)
        {
            for(j in 0..2)
            {
                bottomLeftSubGrid[i][j] = pentagoBoard[3 + i][j]
            }
        }

        if(rotationString == CLOCKWISE_ROTATION)
        {
            //Top Row
            pentagoBoard[3][0] = bottomLeftSubGrid[2][0]
            pentagoBoard[3][1] = bottomLeftSubGrid[1][0]
            pentagoBoard[3][2] = bottomLeftSubGrid[0][0]

            //Middle Row
            pentagoBoard[4][0] = bottomLeftSubGrid[2][1]
            pentagoBoard[4][1] = bottomLeftSubGrid[1][1]
            pentagoBoard[4][2] = bottomLeftSubGrid[0][1]

            //Bottom Row
            pentagoBoard[5][0] = bottomLeftSubGrid[2][2]
            pentagoBoard[5][1] = bottomLeftSubGrid[1][2]
            pentagoBoard[5][2] = bottomLeftSubGrid[0][2]
        }
        else
        {
            if(rotationString == ANTI_CLOCKWISE_ROTATION)
            {
                //Bottom Row
                pentagoBoard[5][2] = bottomLeftSubGrid[2][0]
                pentagoBoard[5][1] = bottomLeftSubGrid[1][0]
                pentagoBoard[5][0] = bottomLeftSubGrid[0][0]

                //Middle Row
                pentagoBoard[4][2] = bottomLeftSubGrid[2][1]
                pentagoBoard[4][1] = bottomLeftSubGrid[1][1]
                pentagoBoard[4][0] = bottomLeftSubGrid[0][1]

                //Top Row
                pentagoBoard[3][2] = bottomLeftSubGrid[2][2]
                pentagoBoard[3][1] = bottomLeftSubGrid[1][2]
                pentagoBoard[3][0] = bottomLeftSubGrid[0][2]
            }
        }
    }

    private fun rotateBottomRightSubgrid(rotationString: String)
    {
        val bottomRightSubGrid = Array(3) { Array<Marble?>(3) { null } }

        for(i in 0..2)
        {
            for(j in 0..2)
            {
                bottomRightSubGrid[i][j] = pentagoBoard[3 + i][3 + j]
            }
        }

        if(rotationString == CLOCKWISE_ROTATION)
        {
            //Top Row
            pentagoBoard[3][3] = bottomRightSubGrid[2][0]
            pentagoBoard[3][4] = bottomRightSubGrid[1][0]
            pentagoBoard[3][5] = bottomRightSubGrid[0][0]

            //Middle Row
            pentagoBoard[4][3] = bottomRightSubGrid[2][1]
            pentagoBoard[4][4] = bottomRightSubGrid[1][1]
            pentagoBoard[4][5] = bottomRightSubGrid[0][1]

            //Bottom Row
            pentagoBoard[5][3] = bottomRightSubGrid[2][2]
            pentagoBoard[5][4] = bottomRightSubGrid[1][2]
            pentagoBoard[5][5] = bottomRightSubGrid[0][2]
        }
        else
        {
            if(rotationString == ANTI_CLOCKWISE_ROTATION)
            {
                //Bottom Row
                pentagoBoard[5][5] = bottomRightSubGrid[2][0]
                pentagoBoard[5][4] = bottomRightSubGrid[1][0]
                pentagoBoard[5][3] = bottomRightSubGrid[0][0]

                //Middle Row
                pentagoBoard[4][5] = bottomRightSubGrid[2][1]
                pentagoBoard[4][4] = bottomRightSubGrid[1][1]
                pentagoBoard[4][3] = bottomRightSubGrid[0][1]

                //Top Row
                pentagoBoard[3][5] = bottomRightSubGrid[2][2]
                pentagoBoard[3][4] = bottomRightSubGrid[1][2]
                pentagoBoard[3][3] = bottomRightSubGrid[0][2]
            }
        }
    }

    companion object
    {
        const val TOP_LEFT_SUBGRID = "Top Left Subgrid"
        const val TOP_RIGHT_SUBGRID = "Top Right Subgrid"
        const val BOTTOM_LEFT_SUBGRID = "Bottom Left Subgrid"
        const val BOTTOM_RIGHT_SUBGRID = "Bottom Right Subgrid"
        const val CLOCKWISE_ROTATION = "Clockwise Rotation"
        const val ANTI_CLOCKWISE_ROTATION = "Anti-Clockwise Rotation"
    }
}