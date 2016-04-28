/** interface ComputerPlay
  *
  * This interface is meant to be used to
  * define an algorithm for a computer player
  * that is playing tic tac toe.
  **/
interface ComputerMove {
  /**
    * This function chooses a move for the computer.
    * It must choose a move that 1. is between 0-8
    * and 2. has not yet been taken by the player or
    * the computer. The function has
    * knowledge of the current state of the board.
    *
    * @Param board - Current state of the board
    * @return computer's move
    **/
  int makeAMove(char[] board);
}
