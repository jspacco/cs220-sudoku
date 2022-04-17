TODO
==
* Disallow entering illegal values. So, if you try to enter a 7 when there is already
	a 7 in the same row, column, or 3x3 grid, it should give an error message instead
	* You can use a `JOptionPane.showMessageDialog`
* `Help => Hint` menu option should highlight any squares that can only contain one possible value
* If you have won it should let you know and end the game!
* Figure out some way to show only the legal values for a square. This can be a special keyboard
	input (like if you click on an empty square and then click 'h' it will show you the legal
	values somehow, either through a `JOptionPane.showMessageDialog` or even something like
	a `ListSelectionModel`)