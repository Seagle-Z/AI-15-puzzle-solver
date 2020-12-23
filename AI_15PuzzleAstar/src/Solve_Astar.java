import com.sun.javafx.scene.NodeHelper;

import javax.xml.soap.Node;
import java.util.*;

import static java.lang.Math.abs;

public class Solve_Astar {

    public Node_Astar solveNode;
    public boolean solved;

    public Solve_Astar() {
        solved = false;
    }

    public void setSolveNode(Node_Astar n) {
        solveNode = n;
    }

    public static void main(String[] args) {

        Solve_Astar solve = new Solve_Astar();

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
        Node_Astar root = new Node_Astar(tryBoard, '0', 0, null);
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

        // array list for store moves
        ArrayList<Node_Astar> solveStep = new ArrayList<>();

        // add root into solve step
        solveStep.add(new Node_Astar(tryBoard, '0', 0, null));

        // get memory before the A*
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // start count time for the A*
        long start = System.currentTimeMillis();

        // while loop for A*
        int counter = 0;    // counter for stopping the loop if did not find solution in 10000 step
        while (!solve.solved) {

            // check if the puzzle solved
            if (solveStep.get(solveStep.size() - 1).checkBoard()) {
                solve.solved = true;
                break;
            }

            // array list for hold next available steps
            ArrayList<Node_Astar> nextAvailable = new ArrayList<>();
            int lastStepIndex = solveStep.size() - 1;

            // compute the heuristics of next 4 way if available
            if (solveStep.get(lastStepIndex).getDir() != 'D' && solveStep.get(lastStepIndex).checkMovable('U')) {
                Node_Astar theNew = new Node_Astar(solveStep.get(lastStepIndex).getBoard(), 'U', solveStep.size(), solveStep.get(lastStepIndex));
                boolean existed = false;
                for (Node_Astar n : solveStep) {
                    if (theNew.getBoard() == n.getBoard()) {
                        existed = true;
                    }
                }
                if (!existed) {
                    nextAvailable.add(theNew);
                    numNodes++;
                }
            }
            if (solveStep.get(lastStepIndex).getDir() != 'U' && solveStep.get(lastStepIndex).checkMovable('D')) {
                Node_Astar theNew = new Node_Astar(solveStep.get(lastStepIndex).getBoard(), 'D', solveStep.size(), solveStep.get(lastStepIndex));
                boolean existed = false;
                for (Node_Astar n : solveStep) {
                    if (theNew.getBoard() == n.getBoard()) {
                        existed = true;
                    }
                }
                if (!existed) {
                    nextAvailable.add(theNew);
                    numNodes++;
                }
            }
            if (solveStep.get(lastStepIndex).getDir() != 'R' && solveStep.get(lastStepIndex).checkMovable('L')) {
                Node_Astar theNew = new Node_Astar(solveStep.get(lastStepIndex).getBoard(), 'L', solveStep.size(), solveStep.get(lastStepIndex));
                boolean existed = false;
                for (Node_Astar n : solveStep) {
                    if (theNew.getBoard() == n.getBoard()) {
                        existed = true;
                    }
                }
                if (!existed) {
                    nextAvailable.add(theNew);
                    numNodes++;
                }
            }
            if (solveStep.get(lastStepIndex).getDir() != 'L' && solveStep.get(lastStepIndex).checkMovable('R')) {
                Node_Astar theNew = new Node_Astar(solveStep.get(lastStepIndex).getBoard(), 'R', solveStep.size(), solveStep.get(lastStepIndex));
                boolean existed = false;
                for (Node_Astar n : solveStep) {
                    if (theNew.getBoard() == n.getBoard()) {
                        existed = true;
                    }
                }
                if (!existed) {
                    nextAvailable.add(theNew);
                    numNodes++;
                }
            }

            // find the best next step depends on misplaced tiles number and manhattan distance
            Node_Astar bestNext = nextAvailable.get(0);
            for (int i = 1; i < nextAvailable.size(); i++) {
                boolean existed = false;
                for (Node_Astar s : solveStep) {
                    int score = 0;
                    for (int a = 0; a < 4; a++) {
                        for (int b = 0; b < 4; b++) {
                            if (s.getBoard()[a][b] == nextAvailable.get(i).getBoard()[a][b]) {
                                score++;
                            }
                        }
                    }
                    if (score == 16) {
                        existed = true;
                        break;
                    }
                }
                if (!existed && nextAvailable.get(i).getTtlManhattanDistance() < bestNext.getTtlManhattanDistance()) {
                    bestNext = nextAvailable.get(i);
                }
                else if (!existed && nextAvailable.get(i).getTtlManhattanDistance() == bestNext.getTtlManhattanDistance() && nextAvailable.get(i).getMisplacedTilesNum() < bestNext.getMisplacedTilesNum()) {
                    bestNext = nextAvailable.get(i);
                }
            }

            // store the A* best selection into solve step
            solveStep.add(new Node_Astar(solveStep.get(lastStepIndex).getBoard(), bestNext.getDir(), solveStep.size(), solveStep.get(lastStepIndex)));

            //solveStep.get(solveStep.size() - 1).printBoard();
            counter++;
            if (counter > 10000)
                break;
        }

        // search finish, timer stop
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        // get the memory useage after the A*
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // get how many memory used
        long actualMemUsed = (afterUsedMem - beforeUsedMem) / 1024;

        // reverse the list order to make it in correct order
        Collections.reverse(solveStep);
        // print the results
        System.out.print("Moves: ");
        if (counter <= 10000) {
            for (int i = 0; i < solveStep.size() - 1; i++) {
                System.out.print(solveStep.get(i).getDir());
            }
        }
        else {
            System.out.print("Can not solve this puzzle in 20000 steps with \nonly Number of misplaced tiles and Manhattan Distance\n two heuristics, give up...");
        }
        System.out.print("\n");
        System.out.println("Number of Nodes expanded: " + numNodes);
        System.out.println("Time Taken: " + elapsedTimeMillis + "ms");
        System.out.println("Memory Used: " + actualMemUsed + "kb");

    }

}
