import java.util.*;
import java.util.Collections;
import java.lang.Math;
import java.util.ArrayList;

public class Player {
    int minusInfty = -99999999;
    int plusInfty = 99999999;
    
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
    
    public int calculateTempLineResult(int player, int[] line, GameState gameState, int linePriority) {
        return calculateTempLineResult(player, line[0], line[1], line[2], line[3], gameState, linePriority);
    }
    
    public int calculateTempLineResult(int player, int row1, int col1, int row2, int col2, GameState gameState, int linePriority) {
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
        
        
        return result;
    }
    
    public int getBestLineForWinning(int player, GameState gameState) {
        int[][] combinations = new int[][] {
            {0, 0, 3, 3, 3},   // D1 - 3 in priority
            {0, 3, 3, 0, 3},   // D2 - 3 in priority
            
            {0, 0, 3, 0, 2},   // C1
            {0, 1, 3, 1, 2},   // C2
            {0, 2, 3, 2, 2},   // C3
            {0, 3, 3, 3, 2},   // C4
            
            {0, 0, 0, 3, 2},   // R1
            {1, 0, 1, 3, 2},   // R2
            {2, 0, 2, 3, 2},   // R3
            {3, 0, 3, 3, 2}    // R4
        };
        
        // Method 1: Look for line to win on.
        // KATTIS-SCORE: 48p with 4 depth
        // int score = minusInfty;
        // for (int[] c : combinations) {
        //     score = Math.max(score, calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], gameState, c[4]));
        //     if (score == 1000) {break;}
        // }
        
        // Method 2: Use sums instead.
        // KATTIS-SCORE: 96p with 10 depth
        int score = 0;
        for (int[] c : combinations) {
            score += calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], gameState, c[4]);
        }
        
        // Method 3: Like 1 but also check if opponent has won
        // KATTIS-SCORE: 29p with 6 depth or more
        // int score = minusInfty, temp = 0;
        // for (int[] c : combinations) {
        //     temp = calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], gameState, c[4]);
        //     if (temp == -1000) {return temp;}
        //     score = Math.max(temp, score);
        // }
        
        // Method 4: Use min instead of max
        // KATTIS-SCORE: 23p with 6 depth or more
        // int score = minusInfty, temp = 0;
        // for (int[] c : combinations) {
        //     temp = calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], gameState, c[4]);
        //     if (temp == -1000) {return temp;}
        //     score = Math.min(temp, score);
        // }
        
        // Method 5: Look only for victory
        // KATTIS-SCORE: 29p with 6 depth or more
        // int score = minusInfty, temp = 0;
        // for (int[] c : combinations) {
        //     temp = calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], gameState, c[4]);
        //     if (temp == -1000 || temp == 1000) {return temp;}
        // }
        // score = 0;
        
        
        
        return score;
    }
    
    public int gamma(GameState gameState) {
        return gamma(gameState, 0);
    }
    
    public int gamma(GameState gameState, int depth) {
        return getBestLineForWinning(getLastPlayer(gameState), gameState);
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
            //if(gameWinner == gameState.getNextPlayer()) {
                v = minusInfty;
                for (GameState nextState : nextStates) {
                    v = Math.max(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta));
                    alpha = Math.max(alpha, v);
                    if (beta <= alpha) {break;} // beta prune
                }
            } else {
                v = plusInfty;
                for (GameState nextState : nextStates) {
                    v = Math.min(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta));
                    beta = Math.min(beta, v);
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
        System.err.println("maxHeurestic is: " + maxHeurestic);
        int bestStateIndex = heuresticArray.indexOf(maxHeurestic);
        GameState bestState = nextStates.get(bestStateIndex);
        //System.err.println(bestState.toString(bestState.getNextPlayer()));
        return bestState;
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
            outputState = randomMoveFn(nextStates);
        } else {
            outputState = optimalMoveFn(nextStates);
        }
        return outputState;
    }    
}
