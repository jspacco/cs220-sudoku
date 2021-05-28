package knox.sudoku;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

public class Sudoku {
	int[][] board = new int[9][9];
	
	public int get(int row, int col) {
		// TODO: check for out of bounds
		return board[row][col];
	}
	
	public void set(int row, int col, int val) {
		// TODO: make sure val is legal
		board[row][col] = val;
	}
	
	public boolean isLegal(int row, int col, int val) {
		// TODO: check if it's legal to put val at row, col
		return true;
	}
	
/**

_ _ _ 3 _ 4 _ 8 9
1 _ 3 2 _ _ _ _ _
etc


0 0 0 3 0 4 0 8 9

 */
	public void load(String filename) {
		try {
			Scanner scan = new Scanner(new FileInputStream(filename));
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
	
	public int get3x3row(int row) {
		return row / 3;
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

	private boolean gameOver() {
		// TODO check that there are still open spots
		return false;
	}

	public boolean isBlank(int row, int col) {
		return board[row][col] > 0;
	}

}
