package chaturanga.player;

import chaturanga.board.Board;
import chaturanga.board.Move;

public class MoveTransition {
    private final Board transitionBoard;
    private final Move move;
    private final MoveStatus moveStatus;
//kelas merepresentasikan ketika membuat pergerakan,transisi dari board dimana board belum bergerak ke board yg pionnya dah gerak dan membuat
    //board yang bergerak menyimpan informasi
    public MoveTransition(final Board transitionBoard, final Move move,
                          final MoveStatus moveStatus) {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getTransitionBoard() {
        return this.transitionBoard;
    }
}
