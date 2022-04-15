package knox.sudoku;

import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * static utility methods
 * 
 * @author jaimespacco
 *
 */
public class Util {
	// private constructor ensures that nobody can create an instance of Util
	private Util() {}
	
	/**
	 * Write the given text to the file with the given name
	 * 
	 * @param filename
	 * @param text
	 */
	public static void writeToFile(String filename, String text) {
		try {
			PrintStream out = new PrintStream(new File(filename));
			out.print(text);
			out.flush();
			out.close();
		} catch (Exception e) {
			// lazy way to convert all static (checked) exceptions into 
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Read the contents of a file with the given name
	 * 
	 * @param filename
	 * @return
	 */
	public static String readFromFile(String filename) {
		try {
			StringBuilder result = new StringBuilder();
			Scanner scanner = new Scanner(new FileInputStream(filename));
			while (scanner.hasNextLine()) {
				result.append(scanner.nextLine());
				result.append("\n");
			}
			scanner.close();
			return result.toString();
		} catch (Exception e) {
			// lazy way to convert all static (checked) exceptions into 
			throw new RuntimeException(e);
		}
	}
}
