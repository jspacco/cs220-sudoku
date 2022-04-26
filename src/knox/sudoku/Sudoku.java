package knox.sudoku;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;

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
public class Sudoku {
	int[][] board = new int[9][9];
	
	LinkedList<String> movement = new LinkedList<>();
	
	//specify the row and col number to undo
	int undoRow;
	int undoCol;
	
	public int get(int row, int col) {
		// TODO: check for out of bounds
		return board[row][col];
	}
	
	public void set(int row, int col, int val) {
		// TODO: make sure val is legal
		if (isLegal(row,col, val)) {
			board[row][col] = val;
			String addVal = (row+"") +" "+ (col+"");
			//currRow = row;
			//currCol = col;
			if (movement.size() == 3) {
				movement.removeFirst();
				movement.addLast(addVal);
			} else if (movement.size() < 3) {
				movement.addLast(addVal);
			}
		}
			
	}
	
	public void undo () {
		//undo 3 steps
		if (movement.size() != 0) {
			String[] setVal = movement.getLast().split(" ");
			undoRow = Integer.parseInt(setVal[0]);
			undoCol = Integer.parseInt(setVal[1]);
			board[undoRow][undoCol] = 0;
			movement.removeLast();
		} else {
			JOptionPane.showMessageDialog(null, "Can't undo. Please enter a value");
		}
	}
	
	public void errorMessage (int row, int col, int val) {
		if (!isLegal(row, col, val))
			JOptionPane.showMessageDialog(null, val + " was used");
	}
	
	public boolean isLegal(int row, int col, int val) {
		// TODO: check if it's legal to put val at row, col
		if (getLegalValues(row, col).contains(val))
			return true;
		return false;
	}
	
	public Collection<Integer> getLegalValues(int row, int col) {
		// TODO: return only the legal values that can be stored at the given row, col
		Set<Integer> legalValue = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
		for (int i = 0; i<9; i++) {
			legalValue.remove(board[row][i]);
			legalValue.remove(board[i][col]);
		}
		int row3 = row/3 * 3;
		int col3 = col/3 * 3;
		for (int i = row3 ; i < row3+3; i++) {
			for (int j = col3; j < col3+3; j++)
				legalValue.remove(board[i][j]);
		}
		return legalValue;
	}
	
	public String printLegalValues (int row, int col) {
		String output = "";
		for (int val : getLegalValues(row, col)) {
			output = output + val + " ";
		}
		return output;
	}
	
/**

_ _ _ 3 _ 4 _ 8 9
1 _ 3 2 _ _ _ _ _
etc


0 0 0 3 0 4 0 8 9

 */
	public void load(File file) {
		try {
			Scanner scan = new Scanner(new FileInputStream(file));
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
	public String toFile() {
		String result = "";
		for (int r=0; r<9; r++) {
			for (int c=0; c<9; c++) {
				int val = get(r, c);
				result += val + " ";
			}
			result += "\n";
		}
		return result;
	}
	
	public String toString() {
		String result = "";
		for (int r=0; r<9; r++) {
			for (int c=0; c<9; c++) {
				int val = get(r, c);
				if (val == 0) {
					result += "_ ";
				} else {
					result += val + " ";
				}
			}
			result += "\n";
		}
		return result;
	}
	
	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku();
		sudoku.load("easy1.txt");
		System.out.println(sudoku);
		
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
		// TODO check that there are still open spots
		for (int i =0 ; i < 9; i++) {
			for (int j = 0 ; j < 9; j++) {
				if (board[i][j] == 0) return false;
			}
		}
		return true;
	}
	
	//if there is space but has no legal value, still return false
	public boolean checkWin () {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (getLegalValues(i, j).size() == 0) return false;
			}
		}
		return true;
	}

	public boolean isBlank(int row, int col) {
		return board[row][col] == 0;
	}

}
