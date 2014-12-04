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
public class tictactoe
{
  // Null Unicode character
  private static final char NUL_CHAR = '\u0000'; 
         
  // Number of rows in board
  private static final int  ROW = 3;              

  // Number of columns in board
  private static final int  COL = 3;              

  // Scanner input
  private static Scanner scan = new Scanner(System.in); 

  // Tic Tac toe board
  private static char[] board;

  // How the chooses its play
  private static ComputerMove algorithm = null;

  /** Driver of the program **/
  public static void main(String[] args) 
  {
    // A winner or draw in a game
    boolean gameOver = false;
    
    // holding winner
    char winner = '-'; 
    
    //Input string reference
    String inputStr = null;
    
    // Repeat loop
    char choice;
    
    // Number of games
    int gameNum = 1;

    // Stores the state of the game at each turn
    boolean xWin, draw, oWin = false;
    
    // Initialize the algorithm
    if(args.length == 1) {
      try{
        algorithm = (ComputerMove)Class.forName(args[0]).newInstance();
      }
      catch(Exception e) {
        System.err.println("Error: Cannot instatiate algorithm.");
        System.err.println("\tMake sure class exists.");
        System.exit(1);
      }
    }
    else if (args.length == 0)
      algorithm = new randomPlay();
    else {
      System.err.print("Error: Incorrect number of parameters. ");
      System.err.println("Look at readme.txt for more information.");
      System.exit(1);
    }

    do{
      System.out.println("\nStart playing Tic-Tac-Toe game!\n");
    
      System.out.println("GAME #: " + gameNum);
      
      // Allocate memory for 2-D char array
      board = new char[9];           
      
      displayBoard();
      
      // Clear flag showing game end
      gameOver = false;                  
      
      do{
        // Player X chooses position
        makeAmove('X');     
        xWin = isAwin('X');

        draw = isAdraw();

        if(!xWin && !draw)
        {
          computerMakeAmove('O');
          oWin = isAwin('O');
        }

        if(!xWin || !oWin)
          draw = isAdraw();

        // Print current status of board
        displayBoard();                       
        
        if (xWin)                      
        { 
          // Flag to start next game
          gameOver = true;    
          
          System.out.println("X player won\n");
          
          // Store winner X
          winner = 'X';
          
          // end game
          continue;                          
        }
        else if(oWin)
        { 
          // Flag to start next game
          gameOver = true;
          
          System.out.println("O player won\n");
          
          // Store winner O
          winner = 'O';
          
          // end game
          continue;
        }
        else if (draw) 
        { 
          //Flag to start next game
          gameOver = true;
          
          System.out.println("A draw! No winner\n");
          
          // Store draw -
          winner = '-';
          
          // end game
          continue;
        }

      }while(!gameOver); // Loop until the game finishes
    
      System.out.print("Winner of game #" + gameNum + ": " + winner);

      //Prompts user if they want to replay
      System.out.print("\n\nWant to play another game (y/n)?  ");
      inputStr = scan.next();
      choice = inputStr.charAt(0);

      gameNum++;

    }while(choice != 'n' && choice != 'N'); // loop if player wants to play
    
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
  public static void makeAmove(char player) {
    int move = -1;
    boolean errFlag = false;

    do{
      scan = new Scanner(System.in); // since try closes it

      System.out.print("\nmove (1-9): ");
      
      try{
        // Stores coordinates to the selected cell
        move = scan.nextInt();
      }
      catch(Exception e) {
        System.err.println("Error: Integers only please");
        errFlag = false;
        continue;
      }

      // Checks the coordinates are in range
      if(move - 1 < 0 || move - 1 > 8) {
        System.err.println("\nError: Enter number inside of the bounds");
        errFlag = false;
      }
      else if(board[move-1] == ' ') { // check nothing is stored in the cell
        board[move-1] = player;
        errFlag = true;
      }
      else {
        System.err.print("\nError: Cell selected is already taken. ");
        System.err.println("Re-enter different cell.");
        errFlag = false;
      }
      
    } while(!errFlag);  // loop until no errors
  } 

  /** 
    * Chooses a move for the computer using the given (or default)
    * algorithm. Alters state of board to reflect move.
    *
    * @Param player - The character of the player making the move
    **/
  public static void computerMakeAmove(char player) {
    int move = algorithm.makeAMove(board);
    board[move] = player;
  } 

  /**
    * Prints the current state of the board to the 
    * stdout buffer.
    **/
  public static void displayBoard() { 

    // Replaces every null character with a space   
    for(int xRow = 0; xRow < ROW; xRow++) {
      for(int yCol = 0; yCol < COL; yCol++)
        if(board[(xRow*3)+yCol] == NUL_CHAR)
          board[(xRow*3)+yCol] = ' ';
    }
    
    // Prints the board 
    for(int xRow = 0; xRow < ROW; xRow++) {
      for(int yCol = 0; yCol < COL; yCol++) {
        System.out.print(" " + board[(xRow*3)+yCol] + (yCol == 2 ? "" : " |"));
      }

      System.out.println();

      if(xRow == 2)
        break;

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
  public static boolean isAwin(char ch) { 
    boolean store = false;
    
    // Horizontal victories
    for(int xRow = 0; xRow < ROW; xRow++) {   
      store = true;
      
      for(int yCol = 0; yCol < COL; yCol++) {
        // Checks store is true and that the location is the players
        store = store && (board[(xRow*3)+yCol] == ch);
        
        // Checks all the columns and that there is a victory
        if(yCol == COL - 1 && store)
          return store;       
        
        // If the there is no victory it goes to the next row
        if(!store)
          break;
      }
    } 
    
    // Vertical victories
    for(int yCol = 0; yCol < COL; yCol++) {   
      store = true;
      
      for(int xRow = 0; xRow < ROW; xRow++) {
        // Checks store is true and that the location is the players
        store = store && (board[(xRow*3)+yCol] == ch);
        
        // Checks all the rows and that there is a victory
        if(xRow == ROW - 1 && store)
          return store; 
          
        // If the there is no victory it goes to the next row   
        if(!store)
          break;
      }
    } 

    // Diagonal Victories
    store = true;
    for(int xRow = 0, yCol = 0; xRow < ROW && yCol < COL; xRow++, yCol++) {
      // Checks that the players character does not equal the cell      
      if(board[(xRow*3)+yCol] != ch) {
        store = false;
        break;
      }
    } 

    // If the players cells does equal to the cell it returns true
    if(store)
      return true;

    store = true;
    for(int xRow = 0, yCol = COL - 1; xRow < ROW && yCol >= 0; xRow++, yCol--)
    {
      // Checks that the players character does not equal the cell
      if(board[(xRow*3)+yCol] != ch) {
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
    for(int xRow = 0; xRow < ROW; xRow++) {
      for(int yCol = 0; yCol < COL; yCol++) {
        if(board[(xRow*3)+yCol] == ' ')
          return false;
      }
    }
    
    // full board
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
  /** Dummy constructo to make **/
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

    do{
      move = (int)(Math.random() * 9);
      
      // check spot is empty
      errFlag = board[move] == ' ' ? true : false;

    } while(!errFlag); // loop until no errors

    return move;
  }
}
