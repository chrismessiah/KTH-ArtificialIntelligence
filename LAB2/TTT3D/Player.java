import java.util.*;
import java.util.Collections;
import java.lang.Math;
import java.util.ArrayList;

public class Player {
    
    // *********** KATTIS-SCORES ***********
    //  NO eval                     depth 1         66p                 9 sec
    //  NO eval                     depth 2         91p                 34 sec
    //
    //  eval        method 1        depth 1         76p                 10 sec
    //  eval        method 1        depth 2                            +50 sec
    //
    //  eval        method 2        depth 1         89p                 10 sec
    //  eval        method 2        depth 2                             +50 sec
    // 
    
    
    int minusInfty = -99999999;
    int plusInfty = 99999999;
    
    // optimize game for X
    int gameWinner = Constants.CELL_X;
    int gameLoser = Constants.CELL_O;
    
    // important variables 
    int depth = 2;
    
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
        return calculateTempLineResult(player, line[0], line[1], line[2], line[3], line[4], line[5], gameState);
    }
    
    public int calculateTempLineResult(int player, int row1, int col1, int row2, int col2, int layer1, int layer2, GameState gameState) {
        int dRow = (row2 - row1) / (gameState.BOARD_SIZE - 1);
        int dCol = (col2 - col1) / (gameState.BOARD_SIZE - 1);
        int dLayer = (layer2 - layer1) / (gameState.BOARD_SIZE - 1);
        int opponent = (player == Constants.CELL_X) ? Constants.CELL_O : Constants.CELL_X;
        int playerCells = 0, opponentCells = 0, result = 0, cellContent = 0;
        
        for (int i = 0; i < gameState.BOARD_SIZE; ++i) {
            cellContent = gameState.at(row1 + dRow * i, col1 + dCol * i, layer1 + dLayer * i);
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
        ArrayList<int[]> combinations = new ArrayList<int[]>();
        
        // Search rows and cols in each layer
        for (int i=0; i<gameState.BOARD_SIZE; i++) {
            // column search
            combinations.add(new int[]  {0, 0, 3, 0, i, i}  );
            combinations.add(new int[]  {0, 1, 3, 1, i, i}  );
            combinations.add(new int[]  {0, 2, 3, 2, i, i}  );
            combinations.add(new int[]  {0, 3, 3, 3, i, i}  );
            // row search
            combinations.add(new int[]  {0, 0, 0, 3, i, i}  );
            combinations.add(new int[]  {1, 0, 1, 3, i, i}  );
            combinations.add(new int[]  {2, 0, 2, 3, i, i}  );
            combinations.add(new int[]  {3, 0, 3, 3, i, i}  );
        }
        // Search combinations going between layers 
        for (int i=0; i<gameState.BOARD_SIZE; i++) {
            combinations.add(new int[]  {i, i, i, i, 0, 3}  );
        }
        
        // Search diagonals
        combinations.add(new int[]  {0, 0, 3, 3, 0, 3}  );
        combinations.add(new int[]  {0, 3, 3, 0, 0, 3}  );
        combinations.add(new int[]  {3, 0, 0, 3, 0, 3}  );
        combinations.add(new int[]  {3, 3, 0, 0, 0, 3}  );
        
        // ************** Method 1: Look for line to win on. **************
        // int score = minusInfty;
        // for (int i=0; i<combinations.size(); i++) {
        // int[] c = combinations.get(i);
        //     score = Math.max(score, calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], c[4], c[5], gameState));
        //     if (score == 1000 || score == -1000) {break;}
        // }
        
        // ************** Method 2: Use sums instead. **************
        int score = 0, temp = 0;
        for (int i=0; i<combinations.size(); i++) {
        int[] c = combinations.get(i);
            temp = calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], c[4], c[5], gameState);
            score += temp;
            if (temp == 1000 || temp == -1000) {return temp;}
            if (i > 15) {break;}
        } 
        
        
        return score;
    }
    
    public int gamma(GameState gameState, Boolean makeEvaluation) {
        return gamma(gameState, 0, makeEvaluation);
    }
    
    public int gamma(GameState gameState, int depth, Boolean makeEvaluation) {
        // without evaluation: 91p
        Move lastMove = gameState.getMove();
        if (lastMove.isXWin()) {
            return 1000;
        } else if (lastMove.isOWin()) {
            return -1000;
        }
        if (makeEvaluation) {
            return getBestLineForWinning(getLastPlayer(gameState), gameState);
        }
        return 0;
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
    public int miniMaxWithAlphaBetaPruning(final GameState gameState, int depth, int alpha, int beta, Boolean makeEvaluation) {
        int v = 0;
        Vector<GameState> nextStates = mu(gameState);
        
        if (depth == 0 || nextStates.size() == 0) { // terminal state
            v = gamma(gameState, makeEvaluation);
        }
        else {
            if(gameWinner == getLastPlayer(gameState)) {
                v = minusInfty;
                for (GameState nextState : nextStates) {
                    v = Math.max(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta, makeEvaluation));
                    alpha = Math.max(alpha, v);
                    if (beta <= alpha) {break;} // beta prune
                }
            } else {
                v = plusInfty;
                for (GameState nextState : nextStates) {
                    v = Math.min(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta, makeEvaluation));
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
            heuresticArray.add(miniMaxWithAlphaBetaPruning(nextStates.get(i), depth, minusInfty, plusInfty, true));
        }
        int maxHeurestic = Collections.max(heuresticArray);
        int bestStateIndex = heuresticArray.indexOf(maxHeurestic);
        GameState bestState = nextStates.get(bestStateIndex);
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
