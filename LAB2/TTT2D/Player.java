import java.util.*;
import java.util.Collections;
import java.lang.Math;
import java.util.ArrayList;

public class Player {
    
    // optimize game for X
    int gameWinner = Constants.CELL_X;
    int gameLoser = Constants.CELL_O;
    
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
            int[] line = gameState.getBestLinesForWinning(gameState.getLastPlayer());
            output = gameState.calculateTempLineResult(gameState.getLastPlayer(), line);
            if (gameState.getNextPlayer() == gameLoser) {output = output*-1;}
        }
        System.err.println("Gamma output is: " + output + " Depth is: " + depth);
        return output;
    }
    
    // returns all possible states for a player
    public Vector<GameState> mu(final GameState gameState) {
        Vector<GameState> nextStates = new Vector<GameState>();
        gameState.findPossibleMoves(nextStates); // Gives the moves for the current player (whichever that is)
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
    public int miniMaxWithAlphaBetaPruning(final GameState gameState, int depth, int alpha, int beta) {
        int v = 0;
        Vector<GameState> nextStates = mu(gameState);
        
        if (depth == 0 || nextStates.size() == 0) {v = gamma(gameState, depth);} // terminal state
        else {
            if(gameWinner == gameState.getLastPlayer()) {
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
            heuresticArray.add(miniMaxWithAlphaBetaPruning(nextStates.get(i), 2, -999999, 999999));
        }
        int maxHeurestic = Collections.max(heuresticArray);
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
        else if (gameState.getLastPlayer() == gameLoser) {
            System.err.println("TURN: O");
            outputState = randomMoveFn(nextStates);
        } else {
            System.err.println("TURN: X");
            outputState = optimalMoveFn(nextStates);
        }
        return outputState;
    }    
}
