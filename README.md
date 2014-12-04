Tic Tac Toe 
==============

## Summary
  In order to use your own custom algorithm, you must create a class
that implements the ComputerMove interface. Then, as long as the compiled
class is in the same directory as tictactoe you should be able to call
tictactoe with the classname as the parameter. 

For example, I have included SequentialPlay (which chooses the next available
cell as its move) which implements ComputerMove. So, to begin a game with the 
SequentialPlay as the algorithm you would:

javac tictactoe.java SequentialPlay.java
java tictactoe SequentialPlay
.
.
.
## Usage

  - compile 
    
      javac tictactoe.java

  - run 
    - using default algorithm 

        java tictactoe

    - with custom algorithm 

        java tictactoe custom_class


