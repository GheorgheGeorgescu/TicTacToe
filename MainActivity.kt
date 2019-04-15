package com.example.tictactoe

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity(), View.OnClickListener {

    // Declaring 2D Array of size 3
    private val buttons = Array<Array<Button?>>(3) { arrayOfNulls(3) }

    // Player 1 starts with "X"
    private var player1Turn = true

    // There is a max of 9 rows; to state draw
    private var roundCount: Int = 0

    private var player1Points: Int = 0
    private var player2Points: Int = 0

    // To display points of player 1 and player 2
    private var textViewPlayer1: TextView? = null
    private var textViewPlayer2: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Reference to text views for player 1 and player 2 variables
        textViewPlayer1 = findViewById(R.id.text_view_p1)
        textViewPlayer2 = findViewById(R.id.text_view_p2)

        // Reference of buttons by unique ID to 2D array by performing a nested for loop
        for (i in 0..2) {
            for (j in 0..2) {
                // going through columns and rows of each button
                val buttonID = "button_$i$j"
                // ??? Not too sure; may refer to package and resource ID of corresponding buttons
                val resID = resources.getIdentifier(buttonID, "id", packageName)
                buttons[i][j] = findViewById(resID)
                // "This" refers to the main activity; this (method) is implemented in the public class MainActivity as View.OnClickListener
                buttons[i][j]!!.setOnClickListener(this)
            }
        }

        // Rest button; when click it runs the restGame function
        val buttonReset = findViewById<Button>(R.id.button_reset)
        buttonReset.setOnClickListener { resetGame() }
    }

    // On-Click method; what to do when a playing field button is clicked
    // 'v' as View
    override fun onClick(v: View) {

        // To return nothing if button not an empty string
        // Sets 'v' as a variable for Button
        if ((v as Button).text.toString() != "") {
            return
        }

        // If it is an empty string then... it sets "X" for player 1's turn and "O" for player 2's turn
        if (player1Turn) {
            v.text = "X"
        } else {
            v.text = "O"
        }

        // To count rounds after each turn
        roundCount++

        // uses the checkForWin method below this code... thus it runs if true, else it switches turns
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins()
            } else {
                player2Wins()
            }
        } else if (roundCount == 9) {
            draw()
            // to switch turns and continue game if no one won or if it's not a draw yet
        } else {
            // This sets 'true' to 'false' thus enabling player 2
            player1Turn = !player1Turn
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array<Array<String?>>(3) { arrayOfNulls(3) }

        // Go through all the buttons and save them as strings in a 2D array
        /* Setting 2D Array values:    [0][0] [0][1] [0][2]
                                       [1][0] [1][1] [1][2]
                                       [2][0] [2][1] [2][2]
        */
        for (i in 0..2) {
            for (j in 0..2) {
                field[i][j] = buttons[i][j]!!.text.toString()
            }
        }

        // Going through combinations, by for loops and if statements, to see if there is a cross match of field letters
        // We use the 'i' in the for loop to save time by ranging the numbers by rows, thus less if statements
        for (i in 0..2) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && field[i][0] != "")
                // Returns true so we know that there is a winner
                return true

        }

        // Same as above for loop, but now the 'i' ranges through columns instead of rows
        for (i in 0..2) {
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && field[0][i] != "")
                return true

        }

        // to check diagonal cross matches
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && field[0][0] != "")
            return true

        // Short way of quick-checking a return of true or false with having to do "else false" statement:
        /*
            if (foo()) {
return true
} else {
return false
}
can be simplified to return foo().


return if (field[0][2] == field[1][1]
        && field[0][2] == field[2][0]
        && field[0][2] != ""
    ) {
        true
    } else false

         */
        return (field[0][2] == field[1][1] && field[0][2] == field[2][0] && field[0][2] != "")
    }

    // To add a point to player 1 if he/she won; plus a pop-up display
    private fun player1Wins() {
        player1Points++
        Toast.makeText(this, "Player 1 wins!", Toast.LENGTH_SHORT).show()
        updatePointsText()
        resetBoard()
    }

    // To add a point to player 2 if he/she won; plus a pop-up display
    private fun player2Wins() {
        player2Points++
        Toast.makeText(this, "Player 2 wins!", Toast.LENGTH_SHORT).show()
        // Two more functions to keep score and continue playing
        updatePointsText()
        resetBoard()
    }

    // A void function o indicate that's a draw
    private fun draw() {
        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show()
        resetBoard()
    }

    // To keep track of scores
    private fun updatePointsText() {
        textViewPlayer1!!.text = "Player 1: $player1Points"
        textViewPlayer2!!.text = "Player 2: $player2Points"
    }

    // To reset board it sets all button texts to empty strings
    private fun resetBoard() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j]!!.text = ""
            }
        }

        // To set number of turns back from 9 to 0; to indicate a draw when necessary
        roundCount = 0
        // To not mix player identities; to basically reset the game with player 1 using 'X' and player 2 using 'O'
        player1Turn = true
    }

    // To reset game entirely; points and board
    private fun resetGame() {
        player1Points = 0
        player2Points = 0
        // To set points to 0
        updatePointsText()
        resetBoard()
    }

    // To keep values of variables during device orientation change
    override fun onSaveInstanceState(outState: Bundle) {
        // Method that is called when device rotates
        super.onSaveInstanceState(outState)
        // outState Bundle takes in variable values by storing them with key words and then the actual value following
        outState.putInt("roundCount", roundCount)
        outState.putInt("player1Points", player1Points)
        outState.putInt("player2Points", player2Points)
        outState.putBoolean("player1Turn", player1Turn)
    }

    // To read/restore the outState Bundle back into the app
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        // By passing the 'key' name of each variable we bring back its value and continue the game left off from the previous rotation
        roundCount = savedInstanceState.getInt("roundCount")
        player1Points = savedInstanceState.getInt("player1Points")
        player2Points = savedInstanceState.getInt("player2Points")
        player1Turn = savedInstanceState.getBoolean("player1Turn")
    }
}
