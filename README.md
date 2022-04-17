TODO for this assignment
--

Disallow entering illegal values
== 
* So, if you try to enter a 7 when there is already a 7 in the same row, column, or 3x3 grid, it should give an error message instead
* You can use a `JOptionPane.showMessageDialog`
* This should use the Sudoku model class to check for this. Probably you should add a new method!

`Help => Hint`
== 
* Choosing this menu option should highlight any squares that can only contain one possible value

Victory!
==
* If you have won it should let you know and end the game!

Only Legal values
==
* Figure out some way to show only the legal values for a square. This can be a special keyboard
	input (like if you click on an empty square and then click 'h' it will show you the legal
	values somehow, either through a `JOptionPane.showMessageDialog` or even something like
	a `ListSelectionModel`)

Load/Save games
==
* Set up the load/save menu options so that they can load and save a game
* To save a game, write the board to a text file. Use a `JFileChooser` to decide what file to write to
* To load a game, use a `JFileChooser` to load a game from a text file