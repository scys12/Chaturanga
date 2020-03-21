package chaturanga.player.ai;

import chaturanga.board.Board;
import chaturanga.board.Move;

public interface MoveStrategy {
    Move execute(Board board);
}
