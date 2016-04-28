public class SequentialPlay implements ComputerMove {
  SequentialPlay() { }

  public int makeAMove(char[] board) {
    int move;
    boolean errFlag = false;

    for (move = 0; move < 9 && !errFlag; move++) {
      // check spot is empty
      errFlag = board[move] == ' ' ? true : false;
    }

    return move-1;
  }
}
