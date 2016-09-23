import java.util.*;

public class Player {
    
    // optimize game for X
    int gameWinner = Constants.CELL_X;
    int gameLoser = Constants.CELL_O;
    
    int alpha = -99999;
    int beta = 99999;


    // Returns how useful the state is to the player
    // Returns 1, 0 or -1
    // WIN/TIE/LOSE?
    public int gamma(final GameState gameState) {
        int stateWinner = gameState.getWinner();
        int output = -99;
        if (stateWinner == gameWinner) {
            output =  1; // correct winner, good state
        } else if(stateWinner == gameLoser) {
            output =  -1; // wrong winner, bad state
        } else {
            output = 0; // tie
        }
        return output;
    }

    // returns all possible states for a player
    public Vector<GameState> mu(final GameState gameState) {
        Vector<GameState> nextStates = new Vector<GameState>();
        
        // Gives the moves for the current player (whichever that is)
        gameState.findPossibleMoves(nextStates); 
        return nextStates;
    }

    // gameState : the current state we are analyzing
    // returns a heuristic value that approximates a utility function of the state
    public int miniMax(final GameState gameState) {
        System.out.println("Run: miniMax()");
        Vector<GameState> nextStates = mu(gameState);
        
        // If terminal state
        if (nextStates.size() == 0) {return gamma(gameState);}
        
        // Cont. searching
        int v;
        int bestPossible;
        if (gameWinner == gameState.getNextPlayer()) {
            bestPossible = -99999; // -infty
            for (GameState nextState : nextStates) {
                v = miniMax(nextState);
                bestPossible = Math.max(bestPossible, v);
            }
        } else {
            bestPossible = 99999; // infty
            for (GameState nextState : nextStates) {
                v = miniMax(nextState);
                bestPossible = Math.min(bestPossible, v);
            }
        }
        return bestPossible;
    }

    public int miniMaxWithAlphaBetaPruning(final GameState gameState, int depth) {
        return miniMaxWithAlphaBetaPruning(gameState, depth, -99999, 99999);
    }

    // gameState: the current state we are analyzing
    // alpha: the current best value achievable by A
    // beta: the current best value acheivable by B
    // returns the minimax value of the state
    public int miniMaxWithAlphaBetaPruning(final GameState gameState, int depth, int alpha, int beta) {
        Vector<GameState> nextStates = mu(gameState);
        
        // reached end of tree
        if (depth == 0 || mu(gameState).size() == 0) {return gamma(gameState);}
        
        // Cont. search
        int v = 0;
        if(gameWinner == gameState.getNextPlayer()) {
            v = -99999;
            for (GameState nextState : nextStates) {
                v = Math.max(v, miniMaxWithAlphaBetaPruning(nextState, depth-1));
                alpha = Math.max(alpha, v);
                if (beta <= alpha) {break;} // beta prune
            }
        } else {
            v = 99999;
            for (GameState nextState : nextStates) {
                v = Math.min(v, miniMaxWithAlphaBetaPruning(nextState, depth-1));
                beta = Math.min(beta, v);
                if (beta <= alpha) {break;} // alpha prune
            }
        }
        return v;
        
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
        System.out.println("Run: Player.play()");
        
        Vector<GameState> nextStates = mu(gameState);
            
        // Must play "pass" move if there are no other moves possible.
        if (nextStates.size() == 0) {return new GameState(gameState, new Move());}
        
        int outputMove = -99;
        if (gameState.getNextPlayer() == gameLoser) {
            Random random = new Random();
            outputMove = random.nextInt(nextStates.size());
        } else {
            /**
             * Here you should write your algorithms to get the best next move, i.e.
             * the best next state. This skeleton returns a random move instead.
             */

            int heuristicVal = miniMax(gameState);
            //int heuristicVal = miniMaxWithAlphaBetaPruning(gameState, DEPTH?!?!?);
            outputMove = heuristicVal;
            
            // should store outputMove = optimalMove;
        }
        
        return nextStates.elementAt(outputMove);
    }    
}
