package knox.sudoku;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;

/**
 * 
 * This is the MODEL class. This class knows all about the
 * underlying state of the Sudoku game. We can VIEW the data
 * stored in this class in a variety of ways, for example,
 * using a simple toString() method, or using a more complex
 * GUI (Graphical User Interface) such as the SudokuGUI 
 * class that is included.
 * 
 * @author jaimespacco
 *
 */

public class Sudoku extends TimerTask{
	private int[][] board = new int[9][9];
	
	
	    public static int i = 0;
	    public void run()
	    {
	        System.out.println("Timer ran " + ++i);
	        if(i==1000) { // timer of 1000 to solve the sudoku.
	        	System.exit(0);
	        }
	    }
	
	public int get(int row, int col) {
		// TODO: check for out of bounds
		return board[row][col];
	}
	
	public void set(int row, int col, int val) {
		// TODO: make sure val is legal
		board[row][col] = val;
	}
	
	public boolean isLegal(int row, int col, int val) {
		return getLegalValues(row, col).contains(val);
	}
	
	public Collection<Integer> getLegalValues(int row, int col) {
		Set<Integer> result = new HashSet<> (Arrays.asList(1,2,3,4,5,6,7,8,9));
		for(int i=0;i<9;i++) {
			result.remove(board [row][i]);
			result.remove(board [i][col]);
		}
		
		int rstart = row/3 *3;
		int cstart = col/3 *3;
		for(int r = rstart;r<rstart+3;r++) {
			for(int c = cstart;c<cstart+3;c++) {
				result.remove(board[r][c]);
			}
		}
		return result;
	}
	
/**

_ _ _ 3 _ 4 _ 8 9
1 _ 3 2 _ _ _ _ _
etc


0 0 0 3 0 4 0 8 9

 */
	public void load(File file) {
		try {
			Scanner scan = new Scanner(file);
			// read the file
			for (int r=0; r<9; r++) {
				for (int c=0; c<9; c++) {
					int val = scan.nextInt();
					board[r][c] = val;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void load(String filename) {
		load(new File(filename));
	}
	
	/**
	 * Return which 3x3 grid this row is contained in.
	 * 
	 * @param row
	 * @return
	 */
	public int get3x3row(int row) {
		return row / 3;
	}
	
	/**
	 * Convert this Sudoku board into a String
	 */
	public String toString() {
		String result = "";
		for (int r=0; r<9; r++) {
			for (int c=0; c<9; c++) {
				int val = get(r, c);
				if (val == 0) {
					result += "_ ";
				} else {
					result += val + "_";
				}
			}
			result += "\n";
		}
		return result;
	}
	
	public String toFileString() {
		String result = "";
		for (int r=0; r<9; r++) {
			for (int c=0; c<9; c++) {
				int val = get(r, c);
				result += val + "_";
				}
			}
			result += "\n";
		
		return result;
	}
	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku();
		Timer timer = new Timer();
        TimerTask task = sudoku;
         
        timer.schedule(task, 2000, 5000);
		
		Scanner scan = new Scanner(System.in);
		while (!sudoku.gameOver()) {
			System.out.println("enter value r, c, v :");
			int r = scan.nextInt();
			int c = scan.nextInt();
			int v = scan.nextInt();
			sudoku.set(r, c, v);

			System.out.println(sudoku);
		}
	}

	public boolean gameOver() {
		for(int[] row: board) {
			for(int val: row) {
				if (val==0) return false;
			}
		}
		return true;
	}
	
	public boolean didIWin() {
		if(!gameOver())
			return false;
		for(int r=0;r<9;r++) {
			for(int c=0;c<9;c++) {
				if(!isLegal(r,c, board[r][c]))
					return false;
			}
		}
		return true;
	}

	public boolean isBlank(int row, int col) {
		return board[row][col] == 0;
	}
	
	

}
