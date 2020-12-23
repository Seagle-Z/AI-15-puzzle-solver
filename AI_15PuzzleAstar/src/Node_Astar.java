import java.io.BufferedWriter;
import static java.lang.Math.abs;

public class Node_Astar {

    public final int BOARD_WIDTH = 4;
    public final int BOARD_HEIGHT = 4;
    public final int BOARD_SIZE = BOARD_WIDTH * BOARD_WIDTH;

    private int[][] board = new int[BOARD_WIDTH][BOARD_WIDTH];
    private int i0, j0;

    //private boolean[] childrenSearched;
    private int childrenSearched;

    public Node_Astar parent;
    public int level;
    public char dir;
    public boolean isRoot;

    private int misplacedTilesNum;
    private int ttlManhattanDistance;

    Node_Astar(int[][] inputBoard, char inputDir, int curLevel, Node_Astar itsParent) {

        i0 = -1; j0 = -1;

        for (int i = 0; i < BOARD_HEIGHT; i++)
        {
            for (int j = 0; j < BOARD_WIDTH; j++)
            {
                board[i][j] = inputBoard[i][j];

                if (board[i][j] == 0)
                {
                    i0 = i;
                    j0 = j;
                }
            }
        }

        dir = inputDir;
        if (dir == '0')
            isRoot = true;

        childrenSearched = 0;
        level = curLevel;

        move(dir);
        setParent(itsParent);
        calcMisplacedAndDistance();
    }

    public int getMisplacedTilesNum() {
        return misplacedTilesNum;
    }

    public int getTtlManhattanDistance() {
        return ttlManhattanDistance;
    }

    public void oneMoreChildSearched() {
        childrenSearched++;
    }

    public int getChildrenSearched() {
        return childrenSearched;
    }

    public int[][] getBoard() {
        return board;
    }

    public char getDir() {
        return dir;
    }

    public int getI0() {
        return i0;
    }

    private void setParent(Node_Astar itsParent)
    {
        parent = itsParent;
    }

    public Node_Astar getParent()
    {
        return parent;
    }

    public int getLevel()
    {
        return level;
    }

    // call this method for check if the puzzle solved
    public boolean checkMovable(char dir) {
        if (i0 == 0 && dir == 'U')
            return false;
        if (i0 == 3 && dir == 'D')
            return false;
        if (j0 == 0 && dir == 'L')
            return false;
        if (j0 == 3 && dir == 'R')
            return false;

        return true;
    }

    // move the 0 to dirction dir
    public boolean move(char dir) {
        // check if move available
        if (i0 == 0 && dir == 'U')
            return false;
        if (i0 == 3 && dir == 'D')
            return false;
        if (j0 == 0 && dir == 'L')
            return false;
        if (j0 == 3 && dir == 'R')
            return false;

        int toPosI = -1, toPosJ = -1;
        if (dir == 'U') {
            toPosI = i0 - 1;
            toPosJ = j0;
        }
        else if (dir == 'D') {
            toPosI = i0 + 1;
            toPosJ = j0;
        }
        else if (dir == 'L') {
            toPosI = i0;
            toPosJ = j0 - 1;
        }
        else if (dir == 'R') {
            toPosI = i0;
            toPosJ = j0 + 1;
        }
        else {
            return false;
        }

        // move the 0
        board[i0][j0] = board[toPosI][toPosJ];
        board[toPosI][toPosJ] = 0;
        i0 = toPosI;
        j0 = toPosJ;

        return true;
    }

    // check if the puzzle solved
    public boolean checkBoard() {

        int score = 0;

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == i * 4 + j + 1) {
                    score++;
                }
            }
        }

        if (score == 15)
            return true;
        else
            return false;
    }

    // print the board used for debut
    public void printBoard() {
        for (int i = 0; i < BOARD_WIDTH; i++)
        {
            for (int j = 0; j < BOARD_HEIGHT; j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.print("\n");
        }
        System.out.println();
    }

    // calculate misplaced tiles number and manhattan distance
    private void calcMisplacedAndDistance() {
        int countMisplaced = 0;
        int countDistance = 0;

        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != i * 4 + j + 1) {
                    // get misplaced tiles
                    countMisplaced += 1;

                    // get manhattan distance
                    int shouldI = board[i][j] / 4;
                    int shouldJ = board[i][j] % 4 - 1;

                    int disI = abs(shouldI - i);
                    int disJ = abs(shouldJ - j);

                    countDistance += disI + disJ;
                }
            }
        }

        misplacedTilesNum = countMisplaced;
        ttlManhattanDistance = countDistance;
    }

}







