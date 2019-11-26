package chaturanga;

import chaturanga.player.BlackPlayer1;
import chaturanga.player.Player1;
import chaturanga.player.WhitePlayer1;

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
        public Player1 choosePlayer(final WhitePlayer1 whitePlayer, final BlackPlayer1 blackPlayer) {
            return whitePlayer;
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
        public Player1 choosePlayer(final WhitePlayer1 whitePlayer, final BlackPlayer1 blackPlayer) {
            return blackPlayer;
        }

        @Override
        public String toString() {
            return "Black";
        }
    };

    public abstract boolean isWhite();
    public abstract boolean isBlack();

    public abstract Player1 choosePlayer(WhitePlayer1 whitePlayer, BlackPlayer1 blackPlayer);
}
