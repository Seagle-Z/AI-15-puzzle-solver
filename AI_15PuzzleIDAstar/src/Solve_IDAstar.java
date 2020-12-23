import java.util.*;

import static java.lang.Math.abs;

public class Solve_IDAstar {

    public Node_IDAstar solveNode;
    public boolean solved;

    public Solve_IDAstar() {
        solved = false;
    }

    public void setSolveNode(Node_IDAstar n) {
        solveNode = n;
    }

    public static void main(String[] args) {

        Solve_IDAstar solve = new Solve_IDAstar();

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
        Node_IDAstar root = new Node_IDAstar(tryBoard, '0', 0, null);
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

        // check if the puzzle already in priority
        if (solve.solved) {
            System.out.println("This puzzle already solved!");
            return ;
        }

        // numNodes for count number of nodes
        int numNodes = 1;

        // array list for store moves
        ArrayList<Node_IDAstar> solveStep = new ArrayList<>();

        // stack for store nodes
        Deque<Node_IDAstar> stack = new ArrayDeque<>();

        // add root into solve step
        solveStep.add(new Node_IDAstar(tryBoard, '0', 0, null));

        // get memory before the A*
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // start count time for the A*
        long start = System.currentTimeMillis();

        // while loop for Iterative deepening A*
        //int counter = 0;    // counter for stopping the loop if did not find solution in 10000 step
        int limitLevel = 1;     // the limit level start from 1
        while (!solve.solved) {

            int iterLimitLevel = limitLevel;

            Node_IDAstar curRoot = new Node_IDAstar(tryBoard, '0', 0, null);

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

                // check if the all four child searched
                if (stack.size() == iterLimitLevel + 1 || stack.peek().getChildrenSearched() >= 4) {
                    char fromDir = stack.peek().dir;
                    int fromDirInt;
                    if (fromDir == 'U')
                        fromDirInt = 1;
                    else if (fromDir == 'D')
                        fromDirInt = 2;
                    else if (fromDir == 'L')
                        fromDirInt = 3;
                    else
                        fromDirInt = 4;

                    stack.pop();
//                    stack.peek().dirSearched[fromDir] = 1;
//                    stack.peek().getChildrenSearched();

                }
                else if (stack.peek().getChildrenSearched() == -1){

                    Node_IDAstar newU;
                    Node_IDAstar newD;
                    Node_IDAstar newL;
                    Node_IDAstar newR;
                    if (stack.peek().checkMovable('U')) {
                        newU = new Node_IDAstar(stack.peek().getBoard(), 'U', stack.size(), stack.peek());
                    }
                    else {
                        newU = null;
                    }
                    if (stack.peek().checkMovable('D')) {
                        newD = new Node_IDAstar(stack.peek().getBoard(), 'D', stack.size(), stack.peek());
                    }
                    else {
                        newD = null;
                    }
                    if (stack.peek().checkMovable('L')) {
                        newL = new Node_IDAstar(stack.peek().getBoard(), 'L', stack.size(), stack.peek());
                    }
                    else {
                        newL = null;
                    }
                    if (stack.peek().checkMovable('R')) {
                        newR = new Node_IDAstar(stack.peek().getBoard(), 'R', stack.size(), stack.peek());
                    }
                    else {
                        newR = null;
                    }

                    DirOrder[] dirOrders = new DirOrder[4];
                    dirOrders[0] = new DirOrder(1);
                    if (newU != null) {
                        if (newD != null && newU.getTtlManhattanDistance() > newD.getTtlManhattanDistance())
                            dirOrders[0].priority++;
                        if (newL != null && newU.getTtlManhattanDistance() > newL.getTtlManhattanDistance())
                            dirOrders[0].priority++;
                        if (newR != null && newU.getTtlManhattanDistance() > newR.getTtlManhattanDistance())
                            dirOrders[0].priority++;
                    }
                    dirOrders[1] = new DirOrder(2);
                    if (newD != null) {
                        if (newU != null && newD.getTtlManhattanDistance() > newU.getTtlManhattanDistance())
                            dirOrders[1].priority++;
                        if (newL != null && newD.getTtlManhattanDistance() > newL.getTtlManhattanDistance())
                            dirOrders[1].priority++;
                        if (newR != null && newD.getTtlManhattanDistance() > newR.getTtlManhattanDistance())
                            dirOrders[1].priority++;
                    }
                    dirOrders[2] = new DirOrder(3);
                    if (newL != null) {
                        if (newD != null && newL.getTtlManhattanDistance() > newD.getTtlManhattanDistance())
                            dirOrders[2].priority++;
                        if (newU != null && newL.getTtlManhattanDistance() > newU.getTtlManhattanDistance())
                            dirOrders[2].priority++;
                        if (newR != null && newL.getTtlManhattanDistance() > newR.getTtlManhattanDistance())
                            dirOrders[2].priority++;
                    }
                    dirOrders[3] = new DirOrder(4);
                    if (newR != null) {
                        if (newD != null && newR.getTtlManhattanDistance() > newD.getTtlManhattanDistance())
                            dirOrders[3].priority++;
                        if (newL != null && newR.getTtlManhattanDistance() > newL.getTtlManhattanDistance())
                            dirOrders[3].priority++;
                        if (newU != null && newR.getTtlManhattanDistance() > newU.getTtlManhattanDistance())
                            dirOrders[3].priority++;
                    }

                    // sort the search priority
                    for (int i = 0; i < 4; i++) {
                        int cur = i;
                        for (int j = i; j < 4; j++) {
                            if (dirOrders[i].priority < dirOrders[j].priority) {
                                cur = j;
                            }
                        }
                        if (cur != i) {
                            int temP = dirOrders[i].priority;
                            int temD = dirOrders[i].dir;
                            dirOrders[i].priority = dirOrders[cur].priority;
                            dirOrders[i].dir = dirOrders[cur].dir;
                            dirOrders[cur].priority = temP;
                            dirOrders[cur].dir = temD;
                        }
                    }
                    int[] searchDirOrder = new int[4];
                    for (int i = 0; i < 4; i++) {
                        searchDirOrder[i] = dirOrders[i].dir;
                    }

                    stack.peek().setThisSearchPriority(searchDirOrder);
                }
                else if (stack.peek().getChildrenSearched() < 4) {
                    char goToDir = 'U';
                    if (stack.peek().getNextSearchChild() == 1)
                        goToDir = 'U';
                    else if (stack.peek().getNextSearchChild() == 2)
                        goToDir = 'D';
                    else if (stack.peek().getNextSearchChild() == 3)
                        goToDir = 'L';
                    else if (stack.peek().getNextSearchChild() == 4)
                        goToDir = 'R';

                    stack.push(new Node_IDAstar(stack.peek().getBoard(), goToDir, stack.size() - 1, stack.peek()));
                }
                else {
                    stack.pop();
                }
            }

            limitLevel++;
        }

        // search finish, timer stop
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        // get the memory useage after the A*
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // get how many memory used
        long actualMemUsed = (afterUsedMem - beforeUsedMem) / 1024;

        // reverse the list priority to make it in correct priority
        Collections.reverse(solveStep);
        // print the results
        System.out.print("Moves: ");
//        if (counter <= 10000) {
            for (int i = 0; i < solveStep.size() - 1; i++) {
                System.out.print(solveStep.get(i).getDir());
            }
//        }
//        else {
//            System.out.print("Can not solve this puzzle in 20000 steps with \nonly Number of misplaced tiles and Manhattan Distance\n two heuristics, give up...");
//        }
        System.out.print("\n");
        System.out.println("Number of Nodes expanded: " + numNodes);
        System.out.println("Time Taken: " + elapsedTimeMillis + "ms");
        System.out.println("Memory Used: " + actualMemUsed + "kb");

    }

}

class DirOrder{
    public int priority;
    public int dir;

    DirOrder(int d) {
        dir = d;
        priority = 0;
    }
}
