package chaturanga.player.ai;

import chaturanga.board.Board;
import chaturanga.board.Move;

public interface BoardEvaluator {
    int evaluate(Move blackMove, Move whiteMove, Board board, int depth);
}
