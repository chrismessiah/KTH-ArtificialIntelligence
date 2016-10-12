import java.util.*;
import java.util.Collections;
import java.lang.Math;
import java.util.ArrayList;

public class Player {
    int minusInfty = -999999;
    int plusInfty = 999999;
    
    // optimize game for X
    int gameWinner = Constants.CELL_X;
    int gameLoser = Constants.CELL_O;
    
    // important variables 
    int depth = 4;
    
    public int getLastPlayer(int player) {
        return (player == Constants.CELL_X) ? Constants.CELL_O : Constants.CELL_X;
    }
    
    public int getLastPlayer(GameState gameState) {
        return getLastPlayer(gameState.getNextPlayer());
    }
    
    public void shutDown(String errorMessage) {
        System.err.println(errorMessage);
        System.exit(0);
    }
    
    public int calculateTempLineResult(int player, int[] line, GameState gameState) {
        return calculateTempLineResult(player, line[0], line[1], line[2], line[3], gameState);
    }
    
    public int calculateTempLineResult(int player, int row1, int col1, int row2, int col2, GameState gameState) {
       int dRow = (row2 - row1) / (gameState.BOARD_SIZE - 1);
       int dCol = (col2 - col1) / (gameState.BOARD_SIZE - 1);
       int opponent = (player == Constants.CELL_X) ? Constants.CELL_O : Constants.CELL_X;
       int playerCells = 0, opponentCells = 0, result = 0, cellContent = 0;

       for (int i = 0; i < gameState.BOARD_SIZE; ++i) {
         cellContent = gameState.at(row1 + dRow * i, col1 + dCol * i);
         if (cellContent == player) {playerCells++;}
         if (cellContent == opponent) {opponentCells++;}
       }
       
       if (playerCells > 0 && opponentCells > 0) {result = 0;} // nobody can win on this line
       else if(playerCells == 1) {result = 1;}
       else if(playerCells == 2) {result = 10;}
       else if(playerCells == 3) {result = 100;}
       else if(playerCells == 4) {result = 1000;}
       else if(opponentCells == 1) {result = -1;}
       else if(opponentCells == 2) {result = -10;}
       else if(opponentCells == 3) {result = -100;}
       else if(opponentCells == 4) {result = -1000;}
    
       // 1000 equals 4 in-a-row and should trigger gamma-fn as EOG
       else if(opponentCells == 4 || playerCells == 4){shutDown("Error in calculateTempLineResult(), should have triggered EOG");}
       return result;
    }
    
    public int[] getBestLineForWinning(int player, GameState gameState) {
        int[] importantLine = new int[4];
        int bestValue = -99999;
        int tempResult;
        int[][] combinations = new int[][] {
            {0, 0, 3, 3},   // D1
            {0, 3, 3, 0},   // D2
            
            {0, 0, 3, 0},   // C1
            {0, 1, 3, 1},   // C2
            {0, 2, 3, 2},   // C3
            {0, 3, 3, 3},   // C4
            
            {0, 0, 0, 3},   // R1
            {1, 0, 1, 3},   // R2
            {2, 0, 2, 3},   // R3
            {3, 0, 3, 3}    // R4
        };
        for (int[] c : combinations) {
            tempResult = calculateTempLineResult(player, c[0], c[1], c[2], c[3], gameState);
            if (bestValue < tempResult) {
                bestValue = tempResult;
                importantLine = new int[] {c[0], c[1], c[2], c[3]};
            }
            
            if(bestValue == 100) {break;}
        }
        //System.err.println("importantLine ROW: " + importantLine[0] + "-" + importantLine[2] + "      COLUMN: " + importantLine[1] + "-" + importantLine[3] + "     bestValue: " + bestValue);
        return importantLine;
    }
    
    public int gamma(GameState gameState) {
        return gamma(gameState, 0);
    }
    
    public int gamma(GameState gameState, int depth) {
        int[] line = getBestLineForWinning(getLastPlayer(gameState), gameState);
        int output = calculateTempLineResult(getLastPlayer(gameState), line, gameState);
        //if (gameState.getNextPlayer() == gameLoser) {output = output*-1;}
        
        // check if opponent 
        return output;
    }
    
    // returns all possible states for a player
    public Vector<GameState> mu(final GameState gameState) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates); // Gives the moves for the current player (whichever that is)
        return nextStates;
    }
        
    // gameState: the current state we are analyzing
    // alpha: the current best value achievable by A
    // beta: the current best value acheivable by B
    // returns the minimax value of the state
    public int miniMaxWithAlphaBetaPruning(final GameState gameState, int depth, int alpha, int beta) {
        int v = 0;
        Vector<GameState> nextStates = mu(gameState);
        
        if (depth == 0 || nextStates.size() == 0) { // terminal state
            v = gamma(gameState);
            //v = gamma(gameState, depth); // trying to compensate with depth
        }
        else {
            if(gameWinner == getLastPlayer(gameState)) {
                v = -999999;
                for (GameState nextState : nextStates) {
                    v = Math.max(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta));
                    alpha = Math.max(alpha, v);
                    if (beta <= alpha) {break;} // beta prune
                }
            } else {
                v = 999999;
                for (GameState nextState : nextStates) {
                    v = Math.min(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta));
                    beta = Math.min(alpha, v);
                    if (beta <= alpha) {break;} // alpha prune
                }
            }
        }
        return v;
        
    }
    
    public GameState optimalMoveFn(Vector<GameState> nextStates) {
        ArrayList<Integer> heuresticArray = new ArrayList<Integer>();
        for (int i=0; i<nextStates.size(); i++) {
            heuresticArray.add(miniMaxWithAlphaBetaPruning(nextStates.get(i), depth, minusInfty, plusInfty));
        }
        int maxHeurestic = Collections.max(heuresticArray);
        //System.err.println("maxHeurestic is: " + maxHeurestic);
        int bestStateIndex = heuresticArray.indexOf(maxHeurestic);
        return nextStates.get(bestStateIndex);
    }
    
    public GameState randomMoveFn(Vector<GameState> nextStates) {
        Random random = new Random();
        return nextStates.elementAt(random.nextInt(nextStates.size()));
    }
    
    /**
    * Performs a move
    *
    * @param gameState
    *            the current state of the board
    * @param deadline
    *            time before which we must have returned
    * @return the next state the board is in after our move
    */
    public GameState play(final GameState gameState, final Deadline deadline) {
        GameState outputState = new GameState();
        Vector<GameState> nextStates = mu(gameState);
        if (nextStates.size() == 0) {return new GameState(gameState, new Move());} // // Must play "pass" move if there are no other moves possible.
        else if (getLastPlayer(gameState) == gameLoser) {
            //System.err.println("TURN: O");
            outputState = randomMoveFn(nextStates);
        } else {
            //System.err.println("TURN: X");
            outputState = optimalMoveFn(nextStates);
        }
        return outputState;
    }    
}
