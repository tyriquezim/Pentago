package com.android.personal.pentago.model

import androidx.collection.emptyLongSet
import kotlin.random.Random

class PentagoBoard(val player1Profile: PlayerProfile, val player2Profile: PlayerProfile, val againstAiOpponent: Boolean)
{
    var pentagoBoard = Array<Array<Marble?>>(6) { Array<Marble?>(6) { null } } //6x6 array which represent the pentago board
    var currentTurnPlayerProfile = player1Profile

    //Might not get used
    fun playTurn(rowNumber: Int, columnNumber: Int, subgridString: String, rotationString: String): PlayerProfile?
    {
        var winner: PlayerProfile?

        placeMarble(rowNumber, columnNumber)
        winner = checkWinCondition(rowNumber, columnNumber)

        if(winner == null) //Since a player can win without having to rotate if the condition is met before rotation
        {
            rotateSubGrid(subgridString, rotationString)
            winner = checkWinCondition(rowNumber, columnNumber)
        }

        if(winner == null) //If there still is no winner then update the turns
        {
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

        return winner
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

    private fun updateTurns()
    {
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

    fun placeMarble(rowNumber: Int, columnNumber: Int)
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
            throw IllegalArgumentException("Cannot place marble at position $rowNumber, $columnNumber. There is already a marble in this cell.")
        }
        else
        {
            pentagoBoard[rowNumber][columnNumber] = newMarble
        }
    }

    fun aiPlaceMarble()
    {
        var rowNumber = Random.nextInt(0, 6)
        var columnNumber = Random.nextInt(0, 6)
        var hasCellBeenFound: Boolean = false

        if(!againstAiOpponent)
        {
            throw IllegalStateException("Cannot play AI opponent's turn. This Pentago game is between human players.")
        }

        while(!hasCellBeenFound)
        {
            try
            {
                placeMarble(rowNumber, columnNumber)
                hasCellBeenFound = true
            }
            catch(e: IllegalArgumentException)
            {
                rowNumber = Random.nextInt(0, 6)
                columnNumber = Random.nextInt(0, 6)
            }
        }
    }

    fun aiRotateSubGrid()
    {
        var subgridInt = Random.nextInt(0, 3)
        var rotationInt = Random.nextInt(0, 1)

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
    }

    fun rotateSubGrid(subgridString: String, rotationString: String)
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
        updateTurns()
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

    fun checkWinCondition(rowNumber: Int, columnNumber: Int): PlayerProfile?
    {
        var winner: PlayerProfile? = null

        winner = checkHorizontal(rowNumber, columnNumber)

        if(winner == null)
        {
            winner = checkVertical(rowNumber, columnNumber)

            if(winner == null)
            {
                winner = checkLeadingDiagonal(rowNumber, columnNumber)

                if(winner == null)
                {
                    winner = checkAntiDiagonal(rowNumber, columnNumber)
                }
            }
        }

        return winner
    }

    private fun checkHorizontal(rowNumber: Int, columnNumber: Int): PlayerProfile?
    {
        //This function checks for a win condition horizontally. It performs the check from Left to right
        val currentMarble = checkNotNull(pentagoBoard[rowNumber][columnNumber]) { "Error! Null was found where a marble was supposed to exist. The function must only take the row and column number of recently placed marbles." } //There SHOULD be a marble here because this function requires arguments of the recently placed marble
        var currentHorizontalMarble: Marble? = null
        var winner: PlayerProfile? = null
        var count: Int = 1
        var checkRowIndex = rowNumber
        var checkColumnIndex = columnNumber
        var rightDeadEnd = false
        var foundLeftmostMarble = false
        var nextLeftHorizontalMarble: Marble?

        //Finds the rightmost marble belonging to the current player
        while(!foundLeftmostMarble)
        {
            if(checkColumnIndex != 0) //If the current marbles column number is not at the very left already (prevent IndexOutOfBounds)
            {
                nextLeftHorizontalMarble = pentagoBoard[checkRowIndex][checkColumnIndex - 1]

                if(nextLeftHorizontalMarble != null && nextLeftHorizontalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    --checkColumnIndex
                }
                else
                {
                    foundLeftmostMarble = true
                }
            }
            else
            {
                foundLeftmostMarble = true
            }
        }

        //Counts the marbles from the rightmost disc in the chain belonging to the current player
        while(count < 5 && !rightDeadEnd)
        {
            if(checkColumnIndex != 5) //If it is not at the very right already
            {
                currentHorizontalMarble = pentagoBoard[checkRowIndex][++checkColumnIndex] //the next marble to the right
                if(currentHorizontalMarble != null && currentHorizontalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    ++count //Found a marble belonging to the current player next to it so count is incremented
                }
                else
                {
                    rightDeadEnd = true
                }
            }
            else
            {
                rightDeadEnd = true
            }
        }
        if(count == 5)
        {
            winner = currentMarble.marbleOwner
        }

        return winner
    }

    private fun checkVertical(rowNumber: Int, columnNumber: Int): PlayerProfile?
    {
        //This function checks for a win condition vertically. It performs the check from Top to bottom
        val currentMarble = checkNotNull(pentagoBoard[rowNumber][columnNumber]) { "Error! Null was found where a marble was supposed to exist. The function must only take the row and column number of recently placed marbles." } //There SHOULD be a marble here because this function requires arguments of the recently placed marble
        var currentVerticalMarble: Marble? = null
        var winner: PlayerProfile? = null
        var count: Int = 1
        var checkRowIndex = rowNumber
        var checkColumnIndex = columnNumber
        var bottomDeadEnd = false
        var foundTopmostMarble = false
        var nextTopVerticalMarble: Marble?

        //Finds the topmost marble belonging to the current player
        while(!foundTopmostMarble)
        {
            if(checkRowIndex != 0) //If the current marbles column number is not at the very top already (prevent IndexOutOfBounds)
            {
                nextTopVerticalMarble = pentagoBoard[checkRowIndex - 1][checkColumnIndex]

                if(nextTopVerticalMarble != null && nextTopVerticalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    --checkRowIndex
                }
                else
                {
                    foundTopmostMarble = true
                }
            }
            else
            {
                foundTopmostMarble = true
            }
        }

        //Counts the marbles from the topmost disc in the chain belonging to the current player
        while(count < 5 && !bottomDeadEnd)
        {
            if(checkRowIndex != 5) //If it is not at the very bottom already
            {
                currentVerticalMarble = pentagoBoard[++checkRowIndex][checkColumnIndex] //the next marble to the right
                if(currentVerticalMarble != null && currentVerticalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    ++count //Found a marble belonging to the current player next to it so count is incremented
                }
                else
                {
                    bottomDeadEnd = true
                }
            }
            else
            {
                bottomDeadEnd = true
            }
        }
        if(count == 5)
        {
            winner = currentMarble.marbleOwner
        }

        return winner
    }

    private fun checkLeadingDiagonal(rowNumber: Int, columnNumber: Int): PlayerProfile?
    {
        //This function checks for a win condition diagonally from the top left corner to the bottom right corner.
        val currentMarble = checkNotNull(pentagoBoard[rowNumber][columnNumber]) { "Error! Null was found where a marble was supposed to exist. The function must only take the row and column number of recently placed marbles." } //There SHOULD be a marble here because this function requires arguments of the recently placed marble
        var currentDiagonalMarble: Marble? = null
        var winner: PlayerProfile? = null
        var count: Int = 1
        var checkRowIndex = rowNumber
        var checkColumnIndex = columnNumber
        var bottomRightDiagonalDeadEnd = false
        var foundTopLeftmostMarble = false
        var nextTopLeftDiagonalMarble: Marble?

        //Finds the topleftmost marble belonging to the current player
        while(!foundTopLeftmostMarble)
        {
            if(checkRowIndex != 0 && checkColumnIndex != 0) //If the current marbles column number is not at the top left already (prevent IndexOutOfBounds)
            {
                nextTopLeftDiagonalMarble = pentagoBoard[checkRowIndex - 1][checkColumnIndex - 1]

                if(nextTopLeftDiagonalMarble != null && nextTopLeftDiagonalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    --checkRowIndex
                    --checkColumnIndex
                }
                else
                {
                    foundTopLeftmostMarble = true
                }
            }
            else
            {
                foundTopLeftmostMarble = true
            }
        }

        //Counts the marbles from the topleftmost marble in the chain belonging to the current player
        while(count < 5 && !bottomRightDiagonalDeadEnd)
        {
            if(checkRowIndex != 5 && checkColumnIndex != 5) //If it is not at the top left already
            {
                currentDiagonalMarble = pentagoBoard[++checkRowIndex][++checkColumnIndex] //the next marble to the bottom right
                if(currentDiagonalMarble != null && currentDiagonalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    ++count //Found a marble belonging to the current player next to it so count is incremented
                }
                else
                {
                    bottomRightDiagonalDeadEnd = true
                }
            }
            else
            {
                bottomRightDiagonalDeadEnd = true
            }
        }
        if(count == 5)
        {
            winner = currentMarble.marbleOwner
        }

        return winner
    }

    private fun checkAntiDiagonal(rowNumber: Int, columnNumber: Int): PlayerProfile?
    {
        //This function checks for a win condition diagonally from the top right corner to the bottom left corner.
        val currentMarble = checkNotNull(pentagoBoard[rowNumber][columnNumber]) { "Error! Null was found where a marble was supposed to exist. The function must only take the row and column number of recently placed marbles." } //There SHOULD be a marble here because this function requires arguments of the recently placed marble
        var currentDiagonalMarble: Marble? = null
        var winner: PlayerProfile? = null
        var count: Int = 1
        var checkRowIndex = rowNumber
        var checkColumnIndex = columnNumber
        var bottomLeftDiagonalDeadEnd = false
        var foundTopRightmostMarble = false
        var nextTopRightDiagonalMarble: Marble?

        //Finds the toprightmost marble belonging to the current player
        while(!foundTopRightmostMarble)
        {
            if(checkRowIndex != 0 && checkColumnIndex != 5) //If the current marbles column number is not at the top right already (prevent IndexOutOfBounds)
            {
                nextTopRightDiagonalMarble = pentagoBoard[checkRowIndex - 1][checkColumnIndex + 1]

                if(nextTopRightDiagonalMarble != null && nextTopRightDiagonalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    --checkRowIndex
                    ++checkColumnIndex
                }
                else
                {
                    foundTopRightmostMarble = true
                }
            }
            else
            {
                foundTopRightmostMarble = true
            }
        }

        //Counts the marbles from the toprightmost marble in the chain belonging to the current player
        while(count < 5 && !bottomLeftDiagonalDeadEnd)
        {
            if(checkRowIndex != 5 && checkColumnIndex != 0) //If it is not at the bottom left already
            {
                currentDiagonalMarble = pentagoBoard[++checkRowIndex][--checkColumnIndex] //the next marble to the bottom right
                if(currentDiagonalMarble != null && currentDiagonalMarble.marbleOwner == currentMarble.marbleOwner)
                {
                    ++count //Found a marble belonging to the current player next to it so count is incremented
                }
                else
                {
                    bottomLeftDiagonalDeadEnd = true
                }
            }
            else
            {
                bottomLeftDiagonalDeadEnd = true
            }
        }
        if(count == 5)
        {
            winner = currentMarble.marbleOwner
        }

        return winner
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