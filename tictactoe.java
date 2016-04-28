import java.util.Scanner;

/**
  * Class tictactoe
  *
  * This programs sets a game of tic tac toe for one player to play against a
  * Computer. The program draws the tic tac toe board. Then the program asks the
  * player to select their first move. Then the computer chooses its move.
  * It keeps going until one of the players wins or both
  * draw; the victory or draw will be determined also by the program.
  * Once the game finishes the program will print out who won the game.
  * Finally, the program asks the player if they want to play again, and
  * if the player says no, the program ends.
  *
  * The computer by default will play by choosing a random cell in the board.
  * The user can pass in a class name that implements the ComputerMove to
  * alter the algorithm of how the computer chooses a cell.
  *
  * @author Eduardo Ramirez
  **/
public class tictactoe {
  // Null Unicode character
  private static final char NUL_CHAR = '\u0000';
  private static final int ROW = 3;
  private static final int COL = 3;
  private static Scanner scan = new Scanner(System.in);
  private static char[] board;
  private static ComputerMove algorithm = null;

  /** Driver of the program **/
  public static void main(String[] args) {
    boolean gameOver = false;
    char winner = '-';
    String inputStr = null;
    int gameNum = 1;
    char choice;
    int curMove;
    boolean xWin, draw, oWin = false;

    // Initialize the algorithm
    if (args.length == 1) {
      try {
        algorithm = (ComputerMove) Class.forName(args[0]).newInstance();
      } catch (Exception e) {
        System.err.println("Error: Cannot instatiate algorithm.");
        System.err.println("\tMake sure class exists.");
        System.exit(1);
      }
    } else if (args.length == 0) {
      algorithm = new randomPlay();
    } else {
      System.err.print("Error: Incorrect number of parameters. ");
      System.err.println("Look at readme.txt for more information.");
      System.exit(1);
    }

    do {
      System.out.println("\nStart playing Tic-Tac-Toe game!\n");
      System.out.println("GAME #: " + gameNum);

      board = new char[9];
      displayBoard();
      gameOver = false;

      do {
        curMove = makeAmove('X');
        xWin = isAwin('X', curMove);

        draw = isAdraw();

        if (!xWin && !draw) {
          curMove = computerMakeAmove('O');
          oWin = isAwin('O', curMove);
        }

        if (!xWin || !oWin) {
          draw = isAdraw();
        }

        displayBoard();

        if (xWin) {
          System.out.println("X player won\n");
          winner = 'X';
          gameOver = true;
          continue;
        } else if (oWin) {
          System.out.println("O player won\n");
          winner = 'O';
          gameOver = true;
          continue;
        } else if (draw) {
          System.out.println("A draw! No winner\n");
          winner = '-';
          gameOver = true;
          continue;
        }

      } while (!gameOver); // Loop until the game finishes

      System.out.print("Winner of game #" + gameNum + ": " + winner);
      System.out.print("\n\nWant to play another game (y/n)?  ");
      inputStr = scan.next();
      choice = inputStr.charAt(0);
      gameNum++;

    } while (choice != 'n' && choice != 'N'); // loop if player wants to play

    //Closes the scanner and closes the program
    scan.close();
    System.exit(0);
  }


  /**
    * Prompts user to select their move. The function
    * verifies that the move is valid and alters the board
    * to signify the state change.
    *
    * @Param player - The character of the player making the move
    **/
  public static int makeAmove(char player) {
    int move = -1;
    boolean errFlag = false;

    do {
      scan = new Scanner(System.in); // since try closes it
      System.out.print("\nmove (1-9): ");

      try{
        move = scan.nextInt();
      } catch (Exception e) {
        System.err.println("Error: Integers only please");
        errFlag = false;
        continue;
      }

      // Checks the coordinates are in range
      if (move - 1 < 0 || move - 1 > 8) {
        System.err.println("\nError: Enter number inside of the bounds");
        errFlag = false;
      } else if (board[move-1] == ' ') { // check nothing is stored in the cell
        board[move-1] = player;
        errFlag = true;
      } else {
        System.err.print("\nError: Cell selected is already taken. ");
        System.err.println("Re-enter different cell.");
        errFlag = false;
      }

    } while (!errFlag);  // loop until no errors

    return move - 1;
  }

  /**
    * Chooses a move for the computer using the given (or default)
    * algorithm. Alters state of board to reflect move.
    *
    * @Param player - The character of the player making the move
    **/
  public static int computerMakeAmove(char player) {
    int move = algorithm.makeAMove(board);
    board[move] = player;
    return move;
  }

  /**
    * Prints the current state of the board to the
    * stdout buffer.
    **/
  public static void displayBoard() {
    for (int xRow = 0; xRow < ROW; xRow++) {
      for (int yCol = 0; yCol < COL; yCol++) {
        if (board[(xRow*3)+yCol] == NUL_CHAR) {
          board[(xRow*3)+yCol] = ' ';
        }
      }
    }

    for (int xRow = 0; xRow < ROW; xRow++) {
      for (int yCol = 0; yCol < COL; yCol++) {
        System.out.print(" " + board[(xRow*3)+yCol] + (yCol == 2 ? "" : " |"));
      }

      System.out.println();

      if (xRow == 2) {
        break;
      }

      System.out.println("--- --- ---");
    }
  }

  /**
    * Checks if the passed in player is victorious either
    * vertical, horizontal, or diagonal.
    *
    * @Param ch - Character of player
    * @return True if player is victorious otherwise false
    **/
  public static boolean isAwin(char ch, int move) {
    boolean store = true;
    int x = move / 3;
    int y = move % 3;

    // Horizontal wins
    for (int yCol = 0; yCol < COL; yCol++) {
      store = store && (board[(x*3)+yCol] == ch);

      if (yCol == COL - 1 && store) {
        return store;
      }

      if (!store) {
        break;
      }
    }

    store = true;

    // Vertical wins
    for (int xRow = 0; xRow < ROW; xRow++) {
      store = store && (board[(xRow*3)+y] == ch);

      if (xRow == ROW - 1 && store) {
        return store;
      }

      if (!store) {
        break;
      }
    }

    store = true;

    // Diagonal Victories
    for (int xRow = 0, yCol = 0; xRow < ROW && yCol < COL; xRow++, yCol++) {
      if (board[(xRow*3)+yCol] != ch) {
        store = false;
        break;
      }
    }

    // Diagonal win (left to right)
    if (store) {
      return true;
    }

    store = true;
    for (int xRow = 0, yCol = COL - 1; xRow < ROW && yCol >= 0; xRow++, yCol--) {
      if (board[(xRow*3)+yCol] != ch) {
        store = false;
        break;
      }
    }

    return store;
  }

  /**
    * Checks that there is no empty cell in the board.
    *
    * @return true if board is full otherwise false
    **/
  public static boolean isAdraw() {
    //Checks that the board is not full
    for (int xRow = 0; xRow < ROW; xRow++) {
      for (int yCol = 0; yCol < COL; yCol++) {
        if (board[(xRow*3)+yCol] == ' ') {
          return false;
        }
      }
    }

    return true;
  }
}

/**
  * class randomPlay
  *
  * This class implements the ComputerMove interface
  * that allows the computer to chooses a random
  * available cell in the board.
  **/
class randomPlay implements ComputerMove {
  /** Dummy constructor **/
  randomPlay() { }

  /**
    * Chooses a move at random. Verifies that
    * chosen location was empty.
    *
    * @Param board - Current state of the board
    * @return cell index
    **/
  public int makeAMove(char[] board) {
    int move;
    boolean errFlag = false;

    do {
      move = (int)(Math.random() * 9);

      // check spot is empty
      errFlag = board[move] == ' ' ? true : false;

    } while (!errFlag); // loop until no errors

    return move;
  }
}
