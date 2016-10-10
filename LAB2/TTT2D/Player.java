import java.util.*;

public class Player {
    
    // optimize game for X
    int gameWinner = Constants.CELL_X;
    int gameLoser = Constants.CELL_O;
    
    int alpha = -99999;
    int beta = 99999;
    
    public void evaluationFunction(GameState gameState) {
        
    }

    public int gamma(GameState gameState, int depth) {
        int output = -999999;
        
        if (gameState.isEOG()) {
            int stateWinner = gameState.getWinner();
            if (stateWinner == gameWinner) {
                output =  1000 - depth; // correct winner, good state
            } else if(stateWinner == gameLoser) {
                output =  depth - 1000; // wrong winner, bad state
            } else if(stateWinner == -1) {
                output = 0; // tie
            } else {
                gameState.shutDown("gamma(): is not tie or winner");
            }
        } else {
            int[] line = gameState.getBestLinesForWinning(gameWinner);
            output = gameState.calculateTempLineResult(gameWinner, line);
        }
        return output;
    }
    
    public int gammaWithoutDepth(GameState gameState) {
        return gamma(gameState, 0);
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
    // public int miniMax(final GameState gameState) {
    //     Vector<GameState> nextStates = mu(gameState);
    //     
    //     // If terminal state
    //     if (nextStates.size() == 0) {return gamma(gameState);}
    //     
    //     // Cont. searching
    //     int v;
    //     int bestPossible;
    //     if (gameWinner == gameState.getNextPlayer()) {
    //         bestPossible = -99999; // -infty
    //         for (GameState nextState : nextStates) {
    //             v = miniMax(nextState);
    //             bestPossible = Math.max(bestPossible, v);
    //         }
    //     } else {
    //         bestPossible = 99999; // infty
    //         for (GameState nextState : nextStates) {
    //             v = miniMax(nextState);
    //             bestPossible = Math.min(bestPossible, v);
    //         }
    //     }
    //     return bestPossible;
    // }


    // gameState: the current state we are analyzing
    // alpha: the current best value achievable by A
    // beta: the current best value acheivable by B
    // returns the minimax value of the state
    public int[] miniMaxWithAlphaBetaPruning(final GameState gameState, int depth, int alpha, int beta) {
        int output = -999999999;
        GameState bestState = new GameState();
        Vector<GameState> nextStates = mu(gameState);
        if (depth == 0 || nextStates.size() == 0) {return new int[]{gammaWithoutDepth(gameState),1};}
        
        if(gameWinner == gameState.getNextPlayer()) {
            for (GameState nextState : nextStates) {
                int result = miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta)[0];
                if (result > alpha) {
                    alpha = result;
                    bestState = nextState;
                }
                if (beta <= alpha) {break;} // beta prune
            }
            output = alpha;
        } else if( (gameLoser == gameState.getNextPlayer()) ) {
            for (GameState nextState : nextStates) {
                int result = miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta)[0];
                if (result < beta) {
                    beta = result;
                    bestState = nextState;
                }
                if (beta <= alpha) {break;} // alpha prune
            }
            output = beta;
        } else {gameState.shutDown("Error in miniMaxWithAlphaBetaPruning() player is not winner or loser");}
        int optimalMove = Integer.parseInt(gameState.getMove().toMessage().split("_")[1]);
        
        if (output == -999999999) {gameState.shutDown("Error in miniMaxWithAlphaBetaPruning() output is -999999999");}
        return new int[]{output,optimalMove};
        
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
        System.err.println("Run: Player.play()");
        GameState outputState = new GameState();
        Vector<GameState> nextStates = mu(gameState);
            
        // Must play "pass" move if there are no other moves possible.
        if (nextStates.size() == 0) {return new GameState(gameState, new Move());}
        int outputMove = -99;
        if (gameState.getNextPlayer() == gameLoser) {
            System.err.println("TURN: O");
            Random random = new Random();
            outputState = nextStates.elementAt(random.nextInt(nextStates.size()));
        } else {
            System.err.println("TURN: X");
            /**
             * Here you should write your algorithms to get the best next move, i.e.
             * the best next state. This skeleton returns a random move instead.
             */
            int heuristicOptimalMove = miniMaxWithAlphaBetaPruning(gameState, 2, -999999, 999999)[0];
            outputState = new GameState(gameState, new Move(heuristicOptimalMove, gameState.getNextPlayer()));
        }

        System.err.println("Printing outputState");
        System.err.println( outputState.toString() );
        return outputState;
    }    
}
