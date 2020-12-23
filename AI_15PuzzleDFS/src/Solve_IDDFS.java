import java.util.*;

public class Solve_IDDFS {

    public Node_IDDFS solveNode;
    public boolean solved;

    public Solve_IDDFS() {
        solved = false;
    }

    public void setSolveNode(Node_IDDFS n) {
        solveNode = n;
    }

    public static void main(String[] args) {

        Solve_IDDFS solve = new Solve_IDDFS();

        // declare the puzzle
        int[][] tryBoard = new int[4][4];

        // input puzzle board
        Scanner in = new Scanner(System.in);
        System.out.print("> ");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tryBoard[i][j] = in.nextInt();
            }
        }

        // declare the root which is the initial puzzle board
        Node_IDDFS root = new Node_IDDFS(tryBoard, '0', 0, null);
        solve.solved = root.checkBoard();

        // check solvable
        int[] tiles = new int[16];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i * 4 + j] = tryBoard[i][j];
            }
        }
        int inversion = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = i + 1; j < 16; j++) {
                if (tiles[i] > tiles[j] && tiles[i] != 0 && tiles[j] != 0) {
                    inversion++;
                }
            }
        }
        int pos0 = root.getI0();
        if (pos0 % 2 == 0 && inversion % 2 == 0) {
            System.out.println("Solution cannot be found \n");
            return;
        }
        else if (pos0 % 2 == 1 && inversion % 2 == 1) {
            System.out.println("Solution cannot be found \n");
            return;
        }

        // check if the puzzle already in order
        if (solve.solved) {
            System.out.println("This puzzle already solved!");
            return ;
        }

        // numNodes for count number of nodes
        int numNodes = 1;

        // stack for store nodes
        Deque<Node_IDDFS> stack = new ArrayDeque<>();

        // the limit number of level start from 1
        int limitLevel = 1;

        // array list for store moves
        ArrayList<Node_IDDFS> solveStep = new ArrayList<>();

        // get memory before the DFS
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // start count time for the DFS
        long start = System.currentTimeMillis();

        // while loop for iterative deepening DFS
        while (!solve.solved) {

            int iterLimitLevel = limitLevel;

            Node_IDDFS curRoot = new Node_IDDFS(tryBoard, '0', 0, null);

            stack.push(curRoot);
            while (!stack.isEmpty()) {

                // check if the solution found
                if (stack.peek().checkBoard()) {

                    // if the solution found, record the solution
                    while (stack.size() > 1) {
                        solveStep.add(stack.pop());
                    }
                    solve.solved = true;

                    break;
                }

                // use stack to present the DFS
                if (stack.size() == iterLimitLevel + 1) {
                    stack.pop();
                }
                else if (stack.peek().getChildrenSearched() == 4) {
                    stack.pop();
                }
                else if (stack.peek().getChildrenSearched() == 0) {
                    stack.peek().oneMoreChildSearched();
                    if (stack.peek().checkMovable('U')) {
                        stack.push(new Node_IDDFS(stack.peek().getBoard(), 'U', stack.size() - 1, stack.peek()));
                        numNodes++;
                    }
                }
                else if (stack.peek().getChildrenSearched() == 1) {
                    stack.peek().oneMoreChildSearched();
                    if (stack.peek().checkMovable('D')) {
                        stack.push(new Node_IDDFS(stack.peek().getBoard(), 'D', stack.size() - 1, stack.peek()));
                        numNodes++;
                    }
                }
                else if (stack.peek().getChildrenSearched() == 2) {
                    stack.peek().oneMoreChildSearched();
                    if (stack.peek().checkMovable('L')) {
                        stack.push(new Node_IDDFS(stack.peek().getBoard(), 'L', stack.size() - 1, stack.peek()));
                        numNodes++;
                    }
                }
                else if (stack.peek().getChildrenSearched() == 3) {
                    stack.peek().oneMoreChildSearched();
                    if (stack.peek().checkMovable('R')) {
                        stack.push(new Node_IDDFS(stack.peek().getBoard(), 'R', stack.size() - 1, stack.peek()));
                        numNodes++;
                    }
                }
                else {
                    stack.pop();
                }
            }

            // increase the search level limit
            limitLevel++;
        }

        // DFS finish, timer stop
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        // get the memory useage after the DFS
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // get how many memory used
        long actualMemUsed = (afterUsedMem - beforeUsedMem) / 1024;

        // reverse the list order to make it in correct order
        Collections.reverse(solveStep);
        // print the results
        System.out.print("Moves: ");
        for (Node_IDDFS n : solveStep) {
            System.out.print(n.getDir());
        }
        System.out.print("\n");
        System.out.println("Number of Nodes expanded: " + numNodes);
        System.out.println("TIme Taken: " + elapsedTimeMillis + "ms");
        System.out.println("Memory Used: " + actualMemUsed + "kb");

    }

}
