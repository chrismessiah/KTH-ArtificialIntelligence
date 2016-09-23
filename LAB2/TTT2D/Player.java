import java.util.*;

public class Player {
    
    int alpha = -99999;
    int beta = 99999;

    // gameState : the current state we are analyzing
    // returns a heuristic value that approximates a utility function of the state
    public int miniMax(final GameState gameState) {
        Vector<GameState> nextStates = mu(gameState);
        
        // If terminal state
        if (nextStates.size() == 0) { return gamma(gameState); }
        
        // Cont. searching
        int v;
        if (Constants.CELL_X == GameState.getCurrentPlayer()) {
            // Player X or A
            int bestPossible = -99999;
            for (Gamestate childState : nextStates) {
                v = miniMax(childState);
        if (gameWinner == gameState.getNextPlayer()) {
                bestPossible = Math.max(bestPossible, v);
            }
        } else {
            // Player O or B
            int bestPossible = 99999;
            for (Gamestate childState : nextStates) {
                v = miniMax(childState);
                bestPossible = Math.min(bestPossible, v);
            }
        }
        return bestPossible;
    }


    // Returns how useful the state is to the player
    // Returns 1, 0 or -1
    public int gamma(final GameState gameState) {
        return 1;
    }

    // returns all possible states for a player
    public Vector<GameState> mu(final GameState gameState) {
        Vector<GameState> nextStates = new Vector<GameState>();
        
        // Gives the moves for the current player (whichever that is)
        gameState.findPossibleMoves(nextStates); 
        return nextStates;
    }

    // gameState: the current state we are analyzing
    // alpha: the current best value achievable by A
    // beta: the current best value acheivable by B
    // returns the minimax value of the state
    public int alphaBeta(final GameState gameState, int depth) {
        int v = 0;
        if (depth == 0 || mu(gameState).size() == 0) {
            v = gamma(gameState);
        } else if(Constants.CELL_X == GameState.getCurrentPlayer()) {
            // Player X or A
            v = -99999;
            for (Gamestate childState : nextStates) {
                v = Math.max(v, alphaBeta(childState, depth-1));
                alpha = Math.max(alpha, v);
                if (beta <= alpha) {break;} // beta prune
            }
        } else {
            // Player O or B
            v = 99999;
            for (Gamestate childState : nextStates) {
                v = Math.min(v, alphaBeta(childState, depth-1));
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
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates);

        if (nextStates.size() == 0) {
            // Must play "pass" move if there are no other moves possible.
            return new GameState(gameState, new Move());
        }

        int num = minimax(gameState);

        /**
         * Here you should write your algorithms to get the best next move, i.e.
         * the best next state. This skeleton returns a random move instead.
         */


        Random random = new Random();
        



        // should return nextStates.elementAt(optimalInteger);
        return nextStates.elementAt(random.nextInt(nextStates.size()));
    }    
}
