package knox.sudoku;

public class Testing {

	public static void main(String[] args) {
		Sudoku sudoku = new Sudoku();
		sudoku.load("easy1.txt");
		System.out.println(sudoku);
		
		//checking examples
		System.out.println(sudoku.getLegalValues(0, 8));
		System.out.println(sudoku.getLegalValues(6, 8));
	}

}
