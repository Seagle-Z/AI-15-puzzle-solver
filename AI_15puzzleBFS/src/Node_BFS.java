public class Node_BFS {

    public final int BOARD_WIDTH = 4;
    public final int BOARD_HEIGHT = 4;
    public final int BOARD_SIZE = BOARD_WIDTH * BOARD_WIDTH;

    private int[][] board = new int[BOARD_WIDTH][BOARD_WIDTH];
    private int i0, j0;

    public Node_BFS parent;
    public int level;
    public char dir;
    public boolean isRoot;

    Node_BFS(int[][] inputBoard, char inputDir, Node_BFS itsParent) {

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

        move(dir);
        setParent(itsParent);
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

    private void setParent(Node_BFS itsParent)
    {
        parent = itsParent;
    }

    public Node_BFS getParent()
    {
        return parent;
    }

    public int getLevel()
    {
        return level;
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
}

