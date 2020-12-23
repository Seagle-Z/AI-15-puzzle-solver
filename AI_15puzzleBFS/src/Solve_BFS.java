import java.util.*;

public class Solve_BFS {

    public Solve_BFS() {

    }

    public static void main(String[] args) {

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

        Node_BFS root = new Node_BFS(tryBoard, '0', null);
        boolean puzzleSolved = root.checkBoard();

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

        if (puzzleSolved) {
            System.out.println("This puzzle already solved!");
            return ;
        }

        // queue for store nodes
        Queue<Node_BFS> queue = new LinkedList<>();
        queue.add(root);
        int numNodes = 1;

        // array list for store moves
        ArrayList<Node_BFS> solveStep = new ArrayList<>();

        // get memory befor the BFS
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // start count time for the BFS
        long start = System.currentTimeMillis();

        // while loop for BFS
        while (!puzzleSolved) {

            // check if the puzzle of head is solved
            if (((LinkedList<Node_BFS>) queue).getFirst().checkBoard()) {

                solveStep.add(((LinkedList<Node_BFS>) queue).getFirst());
                Node_BFS pointer = ((LinkedList<Node_BFS>) queue).getFirst().getParent();
                while (pointer.getParent() != null) {
                    solveStep.add(pointer);
                    pointer = pointer.getParent();
                }

                break;
            }
            else {
                queue.add(new Node_BFS(((LinkedList<Node_BFS>) queue).getFirst().getBoard(), 'U', ((LinkedList<Node_BFS>) queue).getFirst()));
                queue.add(new Node_BFS(((LinkedList<Node_BFS>) queue).getFirst().getBoard(), 'D', ((LinkedList<Node_BFS>) queue).getFirst()));
                queue.add(new Node_BFS(((LinkedList<Node_BFS>) queue).getFirst().getBoard(), 'L', ((LinkedList<Node_BFS>) queue).getFirst()));
                queue.add(new Node_BFS(((LinkedList<Node_BFS>) queue).getFirst().getBoard(), 'R', ((LinkedList<Node_BFS>) queue).getFirst()));
                queue.remove();
                numNodes += 3;
            }
        }
        // BFS finish, timer stop
        long elapsedTimeMillis = System.currentTimeMillis() - start;
        // get the memory useage after the BFS
        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        // get how many memory used
        long actualMemUsed = (afterUsedMem - beforeUsedMem) / 1024;

        // reverse the list order to make it in correct order
        Collections.reverse(solveStep);
        // print the results
        System.out.print("Moves: ");
        for (Node_BFS n : solveStep) {
            System.out.print(n.getDir());
        }
        System.out.print("\n");
        System.out.println("Number of Nodes expanded: " + numNodes);
        System.out.println("TIme Taken: " + elapsedTimeMillis + "ms");
        System.out.println("Memory Used: " + actualMemUsed + "kb");

    }

}
