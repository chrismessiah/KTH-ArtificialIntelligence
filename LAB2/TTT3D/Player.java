import java.util.*;
import java.util.Collections;
import java.lang.Math;
import java.util.ArrayList;

public class Player {
    
    // *********** KATTIS-SCORES ***********
    //  NO eval                     depth 1         66p                 9 sec
    //  NO eval                     depth 2         91p                 34 sec
    //
    //  eval        method 1        depth 1         91p                 10 sec
    //  eval        method 1        depth 2         91p                 41 sec
    //
    //  eval        method 2        depth 1         92p                 10 sec
    //  eval        method 2        depth 2                             +50 sec
    // 
    
    // *********** KATTIS-SCORES for semi-depth ***********
    //  method 2        depth 1 (1/2)      depth 2(1/2)       96p       39 sec
    //  method 2        depth 1 (1/3)      depth 2(2/3)       96p       48 sec
    // 
    
    
    int minusInfty = -99999999;
    int plusInfty = 99999999;
    ArrayList<int[]> combinations;
    
    // optimize game for X
    int gameWinner = Constants.CELL_X;
    int gameLoser = Constants.CELL_O;
    
    // important variables 
    int depth = 2;
    int useMethod = 3;
    int divisor = 4; // the 1/x part that will use depth -1
    
    public Player() {
        combinations = new ArrayList<int[]>();
        // Search diagonals
        combinations.add(new int[]  {0, 0, 3, 3, 0, 3}  );
        combinations.add(new int[]  {0, 3, 3, 0, 0, 3}  );
        combinations.add(new int[]  {3, 0, 0, 3, 0, 3}  );
        combinations.add(new int[]  {3, 3, 0, 0, 0, 3}  );
        for (int i=0; i<4; i++) {
            combinations.add(new int[]  {0, 0, 3, 3, i, i}  ); // side 1
            combinations.add(new int[]  {0, 3, 3, 0, i, i}  ); // side 1
            
            combinations.add(new int[]  {0, i, 3, i, 0, 3}  ); // side 2
            combinations.add(new int[]  {3, i, 0, i, 0, 3}  ); // side 2
        
            combinations.add(new int[]  {i, 0, i, 3, 0, 3}  ); // side 3
            combinations.add(new int[]  {i, 3, i, 0, 0, 3}  ); // side 3
        }
        
        // Search rows and cols in each layer
        for (int i=0; i<4; i++) {
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
            
            // layer search
            combinations.add(new int[]  {0, i, 0, i, 0, 3}  );
            combinations.add(new int[]  {1, i, 1, i, 0, 3}  );
            combinations.add(new int[]  {2, i, 2, i, 0, 3}  );
            combinations.add(new int[]  {3, i, 3, i, 0, 3}  );
            
        }
    }
    
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
        int totalCells = playerCells + opponentCells;
        if (playerCells > 0 && opponentCells > 0) {return 0;} // nobody can win on this line
        else if(totalCells == 2) {result = 5;}
        else if(totalCells == 3) {result = 500;}
        if (opponentCells > 0) {result = result*-1;}
        
        
        return result;
    }
    
    public int getBestLineForWinning(int player, GameState gameState) {
        int score = 0;
        
        // ************** Method 1: Look for line to win on. **************
        if (useMethod == 1) {
            for (int i=0; i<combinations.size(); i++) {
                int[] c = combinations.get(i);
                score = Math.max(score, calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], c[4], c[5], gameState));
                if (score == 1000 || score == -1000) {break;}
            }
        }
        
        // ************** Method 2: Use sums instead. **************
        else if (useMethod == 2) {
            int temp = 0;
            for (int i=0; i<combinations.size(); i++) {
                int[] c = combinations.get(i);
                temp = calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], c[4], c[5], gameState);
                score += temp;
            }
        }
        
        // ************** Method 3: Amunds tip. **************
        else {
          int temp = 0;
          Boolean hasThreeInARow = false;
          for (int i=0; i<combinations.size(); i++) {
          int[] c = combinations.get(i);
              temp = calculateTempLineResult(gameWinner, c[0], c[1], c[2], c[3], c[4], c[5], gameState);
              score += temp;
              if (temp == 9) {
                  if (hasThreeInARow) {return 999999;}
                  hasThreeInARow = true;
              }
          }
        }
            
        return score;
    }
    
    public int gamma(GameState gameState, Boolean useEval) {
        return gamma(gameState, 0, useEval);
    }
    
    public int gamma(GameState gameState, int depth, Boolean useEval) {
        Move lastMove = gameState.getMove();
        if (lastMove.isXWin()) {
            return 9999999;
        } else if (lastMove.isOWin()) {
            return -9999999;
        }
        if (useEval) {return getBestLineForWinning(getLastPlayer(gameState), gameState);}
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
    public int miniMaxWithAlphaBetaPruning(final GameState gameState, int depth, int alpha, int beta, Boolean useEval) {
        int v = 0;
        Vector<GameState> nextStates = mu(gameState);
        
        if (depth == 0 || nextStates.size() == 0) { // terminal state
            v = gamma(gameState, useEval);
        }
        else {
            if(gameWinner == getLastPlayer(gameState)) {
                v = minusInfty;
                for (GameState nextState : nextStates) {
                    v = Math.max(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta, useEval));
                    alpha = Math.max(alpha, v);
                    if (beta <= alpha) {break;} // beta prune
                }
            } else {
                v = plusInfty;
                for (GameState nextState : nextStates) {
                    v = Math.min(v, miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta, useEval));
                    beta = Math.min(beta, v);
                    if (beta <= alpha) {break;} // alpha prune
                }
            }
        }
        return v;
        
    }
    
    public GameState optimalMoveFn(Vector<GameState> nextStates) {
        ArrayList<Integer> heuresticArray = new ArrayList<Integer>();
        int limit = (nextStates.size()/divisor);
        int maxHeurestic, bestStateIndex;
        
        
        
        for (int i=0; i<limit; i++) {
            heuresticArray.add(miniMaxWithAlphaBetaPruning(nextStates.get(i), depth-1, minusInfty, plusInfty, true));
        }
        for (int i=limit; i<nextStates.size(); i++) {
            heuresticArray.add(miniMaxWithAlphaBetaPruning(nextStates.get(i), depth, minusInfty, plusInfty, true));
        }    
        
        
        maxHeurestic = Collections.max(heuresticArray);
        bestStateIndex = heuresticArray.indexOf(maxHeurestic);
        GameState bestState = nextStates.get(bestStateIndex);
        return bestState;
    }
    
    public GameState randomMoveFn(Vector<GameState> nextStates) {
        Random random = new Random();
        return nextStates.elementAt(random.nextInt(nextStates.size()));
    }
    
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
