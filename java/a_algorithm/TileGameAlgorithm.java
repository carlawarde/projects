import java.util.Scanner;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
* Tile Game Algorithm
* @author Carla Warde(17204542), Vincent Kiely (17236282), Mats Rostad (17195179), Lena Stolz (17210577)
* @since 2019-04-12
*/
public class TileGameAlgorithm {
    private int boardLength;
    private Board startBoard;
    private Board endBoard;
    
    private ArrayList<Board> open = new ArrayList<Board>();
    private ArrayList<Board> close = new ArrayList<Board>();

	/**
	* The run method is called by the Driver class and runs until the puzzle is solved.
	*/
    public void run() {
        validateInput();
        
        boolean solutionFound = false;
        Board lastBoard = null;
		
		startBoard.setF(hFunc(startBoard));
        
        open.add(startBoard);
		
		long startTime = System.currentTimeMillis();
        
        while (!open.isEmpty()) {
            //Find path with lowest cost
            Board currentBoard = open.get(open.size() - 1);
            int lowestF = currentBoard.getF();
            for (int i = open.size() - 1; i >= 0; i--) {
				Board temp = open.get(i);
				int f = temp.getF();
                if (f < lowestF) {
					currentBoard = temp;
					lowestF = f;
				}
            }
            
            //Need to use deepEquals for multidimensional arrays
            if (Arrays.deepEquals(currentBoard.getMatrix(), endBoard.getMatrix())) {
                solutionFound = true;
                lastBoard = currentBoard;
                break;
            }
            
            open.remove(currentBoard);
            close.add(currentBoard);
            
            ArrayList<Tile> moveableTiles = checkMoves(currentBoard);
            for (int i = 0; i < moveableTiles.size(); i++) {
                //b will be the child of the currentBoard
                Board b = new Board(currentBoard.getMatrix());
                
                //Move 0 in the available direction
                b.moveTile(moveableTiles.get(i));

				//Increment current g value and set f
				int g = currentBoard.getG() + 1;
				b.setG(g);
                b.setF(g + hFunc(b));
                
                //Set parent board so we can trace the path back to start when a solution is found
                b.setParentBoard(currentBoard);

                //Check if they are in open or close already, to avoid duplication of data
				if (!containsBoard(b, close))
					if (!containsBoard(b, open)) open.add(b);
            }
        }

        if (solutionFound) {
			long endTime = System.currentTimeMillis();
			double seconds = (endTime - startTime) / 1000.0;
			printPath(lastBoard);
			System.out.printf("Execution Time [%.3f] second(s)\n", seconds);
        } else
            System.out.println("No solution found for the states provided");
    }
	
	private void printPath(Board lastBoard) {
		//Add them to a list because we don't want to print the path backwards
		ArrayList<Board> path = new ArrayList<Board>();
		
		//Trace and add boards until there is no more parent board (reached start)
		Board parent = lastBoard.getParentBoard();
		path.add(parent);
		
		while (parent.getParentBoard() != null) {
			parent = parent.getParentBoard();
			path.add(parent);
		}
		
		//Loop backwards and print
		for (int i = path.size() - 1; i >= 0; i--) {
			path.get(i).printBoard();
			
			System.out.println("----");
			System.out.println(" \\/ ");
		}
		lastBoard.printBoard();
	}
    
	private boolean containsBoard(Board board, ArrayList<Board> list) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (Arrays.deepEquals(board.getMatrix(), list.get(i).getMatrix()))
				return true;
		}
		return false;
	}
	
    private boolean hasDuplicates(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = i + 1; j < array.length; j++)
                if (array[i] == array[j]) return true;
        }
        return false;
    }
    
    private int[][] arrayToMatrix(int[] inputArray, int size) {
        int[][] matrix = new int[size][size];
        for (int i = 0; i < inputArray.length; i++)
            matrix[i / size][i % size] = inputArray[i];
        return matrix;
    }
    
	/**
	* First the user is asked whether they want an 8 tile or 15 tile game. Then it requests user input for the start and goal board states and validates them.
	**/
    private void validateInput() {
        String[] options = new String[] {"8 Puzzle", "15 Puzzle"};
        int type = JOptionPane.showOptionDialog(null, "Choose Puzzle", "Puzzle", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        String seqPattern;
        int puzzleSize;
        
        if (type == 0) {
            puzzleSize = 9;
            seqPattern = "^[0-8]( [0-8]){8}$";
        } else {
            puzzleSize = 16;
            seqPattern = "^([0-9]|1[0-5])( ([0-9]|1[0-5])){15}$";
        }
        
        int[] startState = new int[puzzleSize];
        int[] finalState = new int[puzzleSize];
        
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Enter a sequence of " + puzzleSize + " unique numbers in the range [0, " + (puzzleSize - 1) + "],\nspace separated", "Start State", JOptionPane.INFORMATION_MESSAGE);
            
            if (!input.matches(seqPattern)) {
                JOptionPane.showMessageDialog(null, "The input does not match the sequence of numbers expected");
                continue;
            }
            
            String[] elements = input.split(" ");
            
            for (int i = 0; i < elements.length; i++)
                startState[i] = Integer.parseInt(elements[i]);
            
            if (hasDuplicates(startState)) {
                JOptionPane.showMessageDialog(null, "There are duplicate numbers in the sequence");
                continue;
            }
            
            while (true) {
                input = JOptionPane.showInputDialog(null, "Enter a sequence of the same pattern for the final state", "Final State", JOptionPane.INFORMATION_MESSAGE);
                
                if (!input.matches(seqPattern)) {
                    JOptionPane.showMessageDialog(null, "The input does not match the sequence of numbers expected");
                    continue;
                }
                
                elements = input.split(" ");
                
                for (int i = 0; i < elements.length; i++)
                    finalState[i] = Integer.parseInt(elements[i]);
                
                if (hasDuplicates(finalState)) {
                    JOptionPane.showMessageDialog(null, "There are duplicate numbers in the sequence");
                    continue;
                }
                
                if (Arrays.equals(startState, finalState)) {
                    JOptionPane.showMessageDialog(null, "The final state you provided is the same as the start state");
                    continue;
                }
                
                break;
            }
            
            break;
        }
        
        int[][] startMatrix = arrayToMatrix(startState, type + 3);
        startBoard = new Board(startMatrix);
        int[][] endMatrix = arrayToMatrix(finalState, type + 3);
        endBoard = new Board(endMatrix);
        
        boardLength = startBoard.getRowLength();
    }
    
    /** 
    *   This method checks which 'tiles' can be moved to the 'empty' space on the board
    *   @param
    *       Board state the 'board' state you're checking for possible moves
    *   @return
    *       ArrayList<Tile> returns an array list of the Tiles that can be moved
    **/
    private ArrayList<Tile> checkMoves(Board state) {
        int value = 0;
        //char c = 'a';
        
        ArrayList<Tile> moveableTiles = new ArrayList<Tile>();
        Tile t;
        
        int zeroRow = state.getTile(0).getRow();
        int zeroCol = state.getTile(0).getCol();
        
        if (zeroRow > 0) {
            value = state.getValue(zeroRow - 1, zeroCol);
            //System.out.println(c +") "+value+" to the South");
            //c++;
            t = new Tile(zeroRow - 1, zeroCol, value);
            moveableTiles.add(t);
        }
        if (zeroCol > 0) {
            value = state.getValue(zeroRow, zeroCol - 1);
            //System.out.println(c+") "+value+" to the East");
            //c++;
            t = new Tile(zeroRow, zeroCol - 1, value);
            moveableTiles.add(t);
        }
        if (zeroRow < (boardLength - 1)) {
            value = state.getValue(zeroRow + 1, zeroCol);
            //System.out.println(c+") "+value+" to the North");
            //c++;
            t = new Tile(zeroRow + 1, zeroCol, value);
            moveableTiles.add(t);
        }
        if (zeroCol < (boardLength - 1)) {
            value = state.getValue(zeroRow, zeroCol + 1);
            //System.out.println(c+") "+value+" to the West");
            //c++;
            t = new Tile(zeroRow, zeroCol + 1, value);
            moveableTiles.add(t);
        }
        
        return moveableTiles;
    }
    
    /**
    *   Counts the offset of each 'tile' from its goal state and adds them
    *   @param
    *       Board state
    *   @return
    *       int h is the h value of the board
    **/
    private int hFunc(Board state) {
        int rowDiff = 0, colDiff = 0, h = 0, value = 0;
        
        for (int i = 0; i < boardLength; i++) {
            for (int j = 0; j < boardLength; j++) {
                value = state.getValue(i, j);
                Tile endBoardTile = endBoard.getTile(value);
                rowDiff = Math.abs(endBoardTile.getRow() - i);
                colDiff = Math.abs(endBoardTile.getCol() - j);
                h += rowDiff + colDiff;
            }
        }
        
        return h;
    }
}