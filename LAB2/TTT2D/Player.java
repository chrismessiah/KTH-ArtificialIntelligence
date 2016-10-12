import java.util.*;

public class Player {
    
    // optimize game for X
    int gameWinner = Constants.CELL_X;
    int gameLoser = Constants.CELL_O;

    public int gamma(GameState gameState, int depth) {
        // System.err.println("");
        // System.err.println("");
        // System.err.println("");
        // System.err.println("INSIDE GAMMA");
        // System.err.println(gameState.toString());
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
            int[] line = gameState.getBestLinesForWinning(gameState.getLastPlayer());
            output = gameState.calculateTempLineResult(gameState.getLastPlayer(), line);
            if (gameState.getLastPlayer() == gameLoser) {output = output*-1;}
        }
        System.err.println("Gamma output is: " + output + " Depth is: " + depth);
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
        // System.err.println("******* RUNNING: miniMaxWithAlphaBetaPruning() *******");
        int output = -999999999;
        int result = -999999999;
        GameState bestState = new GameState();
        Vector<GameState> nextStates = mu(gameState);
        if (depth == 0 || nextStates.size() == 0) {
            // System.err.println("******* RETURN depth: " + depth + "     nextStates.size(): " + nextStates.size() + " *******");
            return new int[]{gamma(gameState, depth),-1};
        }
        
        if(gameWinner == gameState.getLastPlayer()) {
            for (GameState nextState : nextStates) {
                // System.err.println("gameWinner");
                // System.err.println(nextState.toString());
                System.err.println("Calling minimax at depth: " + depth);
                result = miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta)[0];
                System.err.println("Result is: " + result + " Depth is: " + depth);
                if (result == 100) {
                    System.err.println("RESULT IS 100");
                    System.err.println(nextState.toString());
                    System.err.println("Depth: " + depth);
                }
                if (result > alpha) {
                    alpha = result;
                    //System.err.println("alpha is new: " + alpha);
                    bestState = nextState;
                }
                if (beta <= alpha) {break;} // beta prune
            }
            output = alpha;
        } else if( (gameLoser == gameState.getLastPlayer()) ) {
            for (GameState nextState : nextStates) {
                // System.err.println("gameLoser");
                // System.err.println(nextState.toString());
                result = miniMaxWithAlphaBetaPruning(nextState, depth-1, alpha, beta)[0];
                if (result < beta) {
                    beta = result;
                    //System.err.println("beta is new: " + beta);
                    bestState = nextState;
                }
                if (beta <= alpha) {break;} // alpha prune
            }
            output = beta;
        } else {gameState.shutDown("Error in miniMaxWithAlphaBetaPruning() player is not winner or loser");}
        // System.err.println("bestState.getMove().toMessage()");
        // System.err.println("bestState.getMove().toMessage()");
        // System.err.println(bestState.toString());
        // System.err.println("alpha: " + alpha);
        // System.err.println("beta: " + beta);
        // System.err.println("Output is: " + output);
        int optimalMove = Integer.parseInt(bestState.getMove().toMessage().split("_")[1]);
        
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
     public GameState optimalMoveFn(GameState gameState) {
         int heuristicOptimalMove = miniMaxWithAlphaBetaPruning(gameState, 2, -999999, 999999)[1];
         return new GameState(gameState, new Move(heuristicOptimalMove, gameState.getLastPlayer()));
     }
     
     public GameState randomMoveFn(Vector<GameState> nextStates) {
         if (nextStates.elementAt(0).getNextPlayer() != gameLoser) {nextStates.elementAt(0).shutDown("Error in randomMoveFn, not gameLoser");}
         Random random = new Random();
         return nextStates.elementAt(random.nextInt(nextStates.size()));
     }
     
    public GameState play(final GameState gameState, final Deadline deadline) {
        // System.err.println("Run: Player.play()");
        GameState outputState = new GameState();
        Vector<GameState> nextStates = mu(gameState);
        // System.err.println("nextStates.size()");
        // System.err.println(nextStates.size());
        
        if (nextStates.size() == 0) {return new GameState(gameState, new Move());} // // Must play "pass" move if there are no other moves possible.
        else if (gameState.getLastPlayer() == gameLoser) {
            System.err.println("TURN: O");
            outputState = randomMoveFn(nextStates);
        } else {
            System.err.println("TURN: X");
            outputState = optimalMoveFn(gameState);
        }
        // System.err.println("Printing outputState");
        // System.err.println("Printing outputState");
        // System.err.println("Printing outputState");
        // System.err.println(outputState.toString());
        return outputState;
    }    
}
