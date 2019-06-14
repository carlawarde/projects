import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;

/**
* Tile Game Algorithm
* @author Carla Warde(17204542), Vincent Kiely (17236282), Mats Rostad (17195179), Lena Stolz (17210577)
* @since 2019-04-12
*/
public class Board {
    private int rowLength;
    private int f;
	private int g;
    private int[][] matrix;
    private Board parentBoard;
    
    public Board(int[][] values) {
        this.rowLength = values.length;
        this.matrix = copyArray(values);
    }
    
    public void setRowLength(int r) {
        this.rowLength = r;
    }
    public int getRowLength() {
        return this.rowLength;
    }
    
    public int getF() {
        return this.f;
    }
    public void setF(int f) {
        this.f = f;
    }
	
    public int getG() {
        return this.g;
    }
    public void setG(int g) {
        this.g = g;
    }
    
    public void setParentBoard(Board parentBoard) {
        this.parentBoard = parentBoard;
    }
    public Board getParentBoard() {
        return parentBoard;
    }
    
    public int getValue(int r, int c) {
        return matrix[r][c];
    }
    
    public int[][] getMatrix() {
        return matrix;
    }
    
    public Tile getTile(int v) {
        for(int i = 0; i < rowLength; i++) {
            for(int j = 0; j < rowLength; j++) {
                if(matrix[i][j] == v) {
                    return new Tile(i, j, v);
                }
            }
        }
        return null;
    }
    
    public void moveTile(Tile t) {
        int row = t.getRow(), col = t.getCol(), value = t.getValue();

        Tile t0 = this.getTile(0);
        int zeroRow = t0.getRow(), zeroCol = t0.getCol();

        matrix[zeroRow][zeroCol] = value;
        matrix[row][col] = 0;
    }
    
    public void printBoard() {
        for( int i = 0; i < rowLength; i++) {
            for(int j = 0; j < rowLength; j++) {
				//System.out.printf("%2X ", matrix[i][j]); for hex representation
                System.out.printf("%2d ", matrix[i][j]);
            }
            System.out.println();
        }
    }
    
    /**
    *   Performs a deep copy of an array
    *   @param
    *       int[][] original array
    *   @return
    *       int[][] deep copy of array
    **/
    private int[][] copyArray(int[][] ori) {
        int[][] newA = new int[rowLength][rowLength];
        for (int i = 0; i < rowLength; i++) {
            for (int j = 0; j < rowLength; j++) {
                newA[i][j] = ori[i][j];
            }
        }
        return newA;
    }
}
