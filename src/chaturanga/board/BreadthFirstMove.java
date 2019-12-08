package chaturanga.board;

import chaturanga.piece.Piece1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstMove {
    private Queue<NodePiece> currentPiece;
    static ArrayList<NodePiece> chainPiece = new ArrayList<NodePiece>();

     public static class NodePiece {
        public int destinationCoordinate;
        public boolean isVisited;
        public List<NodePiece> nextPiece;

        public NodePiece(int destinationCoordinate) {
            this.destinationCoordinate = destinationCoordinate;
            this.nextPiece = new ArrayList<>();
        }

        public void addNextMovedPiece(NodePiece nextMovedPiece) {
            this.nextPiece.add(nextMovedPiece);
        }

        public List<NodePiece> getNextMovedPiece() {
            return this.nextPiece;
        }

        public void setNextMovedPiece(List<NodePiece> nextPiece) {
            this.nextPiece = nextPiece;
        }
    }

    public BreadthFirstMove() {
        currentPiece = new LinkedList<NodePiece>();
    }

    public void seach(NodePiece nodePiece) {
        currentPiece.add(nodePiece);
        nodePiece.isVisited =true;
        while (!currentPiece.isEmpty()) {
            NodePiece element = currentPiece.remove();
            List<NodePiece>  nextMovedPiece= element.getNextMovedPiece();
            for (int i = 0; i < nextMovedPiece.size(); i++) {
                NodePiece nodePiece1 = nextMovedPiece.get(i);
                if (nodePiece1 != null && !nodePiece1.isVisited) {
                    currentPiece.add(nodePiece1);
                    nodePiece1.isVisited = true;
                }
            }
        }
    }
}
