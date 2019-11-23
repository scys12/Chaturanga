package chaturanga.player;

import chaturanga.board.Board1;
import chaturanga.board.Move1;

public class MoveTransition1 {
    private final Board1 transitionBoard;
    private final Move1 move;
    private final MoveStatus1 moveStatus;

    public MoveTransition1(final Board1 transitionBoard, final Move1 move,
                           final MoveStatus1 moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus1 getMoveStatus() {
        return this.moveStatus;
    }
}
