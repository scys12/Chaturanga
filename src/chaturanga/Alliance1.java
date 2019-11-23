package chaturanga;

public enum Alliance1 {
    WHITE {
        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }

        @Override
        public String toString() {
            return "White";
        }
    }, BLACK {
        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }

        @Override
        public String toString() {
            return "Black";
        }
    };

    public abstract boolean isWhite();
    public abstract boolean isBlack();
}
