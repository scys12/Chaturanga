package chaturanga.player.ai;

import chaturanga.board.Board;
import chaturanga.board.Move;
import chaturanga.player.MoveTransition;

/*
heuristic :
        semakin panjang jaraknya,
        kalau kasus(a ke d sama jaraknya b ke c) berarti pilih yang tertinggal
        nah sekaligus ngecek juga apakah udah tertinggal jauh, batas ketinggalan 4 baris
        nah dia juga harus gerak ke ujung sisinya
*/

public class MiniMax implements MoveStrategy {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;
    public MiniMax(final int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }

    @Override
    public String toString() {
        return "MiniMax";
    }

    @Override
    public Move execute(Board board) {
        final long startTime = System.currentTimeMillis();
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + "thinking with depth = " + this.searchDepth);
        int numMoves = board.currentPlayer().getLegalMoves().size();
        System.out.println(board.currentPlayer() + "thinking with depth = " + numMoves);
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.currentPlayer().getAlliance().isWhite() ? min(move,move ,moveTransition.getTransitionBoard(), this.searchDepth - 1,Integer.MIN_VALUE,Integer.MAX_VALUE) : max(move,move,moveTransition.getTransitionBoard(), this.searchDepth - 1,Integer.MIN_VALUE,Integer.MAX_VALUE);
                if (board.currentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.currentPlayer().getAlliance().isBlack() && currentValue<=lowestSeenValue) {
                    System.out.println("pr");
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        final long executionTime = System.currentTimeMillis() - startTime;
        return bestMove;
    }

    public int min(final Move blackMove,final Move whiteMove, final Board board, final int depth, int alpha, int beta) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(blackMove,whiteMove,board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove((move));
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = max(blackMove, whiteMove,moveTransition.getTransitionBoard(), depth - 1,alpha,beta);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
                if (beta >= lowestSeenValue) {
                    beta=lowestSeenValue;
                }
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final Move blackMove, final Move whiteMove,final Board board, final int depth, int alpha, int beta) {
        if (depth == 0 || isEndGameScenario(board)) {
            return this.boardEvaluator.evaluate(blackMove, whiteMove,board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (final Move move : board.currentPlayer().getLegalMoves()) {
            final MoveTransition moveTransition = board.currentPlayer().makeMove((move));
            if (moveTransition.getMoveStatus().isDone()) {
                final int currentValue = min(blackMove, move, moveTransition.getTransitionBoard(), depth - 1, alpha,beta);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
                if (alpha <= highestSeenValue) {
                    alpha = highestSeenValue;
                }
                if (alpha >= beta) {
                    break;
                }
            }
        }
        return highestSeenValue;
    }

    private static boolean isEndGameScenario(final Board board){
        return board.currentPlayer().isInCheckMate();
    }
}
