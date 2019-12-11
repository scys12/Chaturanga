package chaturanga.player;

public enum MoveStatus {
    DONE {
        @Override
        public boolean isDone() {
            return true;
        }

        @Override
        public boolean isInCheckMate() {
            return false;
        }
    },
    ILLEGAL_MOVE {
        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public boolean isInCheckMate() {
            return false;
        }
    },
    LEAVES_PLAYER_IN_CHECK {
        @Override
        public boolean isDone() {
            return false;
        }

        @Override
        public boolean isInCheckMate() {
            return true;
        }
    };

    public abstract boolean isDone();

    public abstract boolean isInCheckMate();
}
