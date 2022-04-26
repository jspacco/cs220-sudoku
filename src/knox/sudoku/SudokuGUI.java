package knox.sudoku;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;


/**
 * 
 * This is the GUI (Graphical User Interface) for Sudoku.
 * 
 * It extends JFrame, which means that it is a subclass of JFrame.
 * The JFrame is the main class, and owns the JMenuBar, which is 
 * the menu bar at the top of the screen with the File and Help 
 * and other menus.
 * 
 * 
 * One of the most important instance variables is a JCanvas, which is 
 * kind of like the canvas that we will paint all of the grid squared onto.
 * 
 * @author jaimespacco
 *
 */
public class SudokuGUI extends JFrame {
	
	private Sudoku sudoku;
    
	private static final long serialVersionUID = 1L;
	
	// Sudoku boards have 9 rows and 9 columns
    private int numRows = 9;
    private int numCols = 9;
    
    // the current row and column we are potentially putting values into
    private int currentRow = -1;
    private int currentCol = -1;

    //row and col for hint
    private int hintRow = -1;
    private int hintCol = -1;
    
    // figuring out how big to make each button
    // honestly not sure how much detail is needed here with margins
	protected final int MARGIN_SIZE = 5;
    protected final int DOUBLE_MARGIN_SIZE = MARGIN_SIZE*2;
    protected int squareSize = 90;
    private int width = DOUBLE_MARGIN_SIZE + squareSize * numCols;    		
    private int height = DOUBLE_MARGIN_SIZE + squareSize * numRows;  
    
    // for lots of fun, too much fun really, try "Wingdings"
    private static Font FONT = new Font("Verdana", Font.BOLD, 40);
    private static Color FONT_COLOR = Color.BLACK;
    private static Color BACKGROUND_COLOR = Color.GRAY;
    
    // the canvas is a panel that gets drawn on
    private JPanel panel;

    // this is the menu bar at the top that owns all of the buttons
    private JMenuBar menuBar;
    
    // 2D array of buttons; each sudoku square is a button
    private JButton[][] buttons = new JButton[numRows][numCols];
    
    // create a file
    public final JFileChooser fc = new JFileChooser(new File("."));
    
    private class MyKeyListener extends KeyAdapter {
    	public final int row;
    	public final int col;
    	public final Sudoku sudoku;
    	
    	MyKeyListener(int row, int col, Sudoku sudoku){
    		this.sudoku = sudoku;
    		this.row = row;
    		this.col = col;
    	}
    	
    	public void keyTyped(KeyEvent e) {
			char key = e.getKeyChar();
			//System.out.println(key);
			if (key == 'h') {
				JOptionPane.showMessageDialog(null, "Possible values: " + sudoku.printLegalValues(row, col));
			}
			if (Character.isDigit(key)) {
				// use ascii values to convert chars to ints
				int digit = key - '0';
				System.out.println(key);
				if (currentRow == row && currentCol == col) {
					sudoku.errorMessage(row , col, digit);
					sudoku.set(row, col, digit);
				}
				update();
			}
		}
    }
    
    private class ButtonListener implements ActionListener {
    	public final int row;
    	public final int col;
    	public final Sudoku sudoku;
    	ButtonListener(int row, int col, Sudoku sudoku){
    		this.sudoku = sudoku;
    		this.row = row;
    		this.col = col;
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.printf("row %d, col %d, %s\n", row, col, e);
			JButton button = (JButton)e.getSource();
			hintRow = -1;
			hintCol = -1;
			
			if (row == currentRow && col == currentCol) {
				currentRow = -1;
				currentCol = -1;
			} else if (sudoku.isBlank(row, col)) {
				// we can try to enter a value in a
				currentRow = row;
				currentCol = col;
				
				// TODO: figure out some way that users can enter values
				// A simple way to do this is to take keyboard input
				// or you can cycle through possible legal values with each click
				// or pop up a selector with only the legal values
				
			} else {
				// TODO: error dialog letting the user know that they cannot enter values
				// where a value has already been placed
				JOptionPane.showMessageDialog(null, "Can't enter a value here");
			}
			
			update();
		}
    }
    
    /**
     * Put text into the given JButton
     * 
     * @param row
     * @param col
     * @param text
     */
    private void setText(int row, int col, String text) {
    	buttons[row][col].setText(text);
    }
    
    /**
     * This is a private helper method that updates the GUI/view
     * to match any changes to the model
     */
    private void update() {
    	for (int row=0; row<numRows; row++) {
    		for (int col=0; col<numCols; col++) {
    			if (hintRow == row && hintCol == col) {
    				buttons[row][col].setBackground(Color.orange);
    				setText(row, col, "");
    			} else if (row == currentRow && col == currentCol && sudoku.isBlank(row, col)) {
    				// draw this grid square special!
    				// this is the grid square we are trying to enter value into
    				buttons[row][col].setForeground(Color.RED);
    				// I can't figure out how to change the background color of a grid square, ugh
    				// Maybe I should have used JLabel instead of JButton?
    				buttons[row][col].setBackground(Color.CYAN);
    				setText(row, col, "_");
    			} else {
    				buttons[row][col].setForeground(FONT_COLOR);
    				buttons[row][col].setBackground(BACKGROUND_COLOR);
	    			int val = sudoku.get(row, col);
	    			if (val == 0) {
	    				setText(row, col, "");
	    			} else {
	    				setText(row, col, val+"");
	    			}
    			}
    		}
    	}
    	repaint();
    }
    
	
    private void createMenuBar() {
    	menuBar = new JMenuBar();
        
    	//
    	// File menu
    	//
    	JMenu file = new JMenu("File");
        menuBar.add(file);
        
        // anonymous inner class
        // basically, immediately create a class that implements actionlistener
        // and then give it the given actionPerformed method.
        addToMenu(file, "New Game", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sudoku.load("easy1.txt");
                update();
            }
        });
        
        addToMenu(file, "Save", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// TODO: save the current game to a file!
            	// https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
            	int returnVal = fc.showSaveDialog(null);
            	String savedGame = sudoku.toFile();
            	
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                	File file = fc.getSelectedFile();
                    Util.writeToFile(file, savedGame);
                    JOptionPane.showMessageDialog(null,
                 		    "File " + file.getName() + " has been saved to "+ file.getAbsolutePath());
                }
                update();
            }
        });
        
        addToMenu(file, "Load", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	// https://docs.oracle.com/javase/tutorial/uiswing/components/filechooser.html
            	int returnVal = fc.showOpenDialog(SudokuGUI.this);
            	 if (returnVal == JFileChooser.APPROVE_OPTION) {
                     File fil = fc.getSelectedFile();
                     sudoku.load(fil);
                     JOptionPane.showMessageDialog(null,
                 		    "File " + fil.getName() + " has been loaded from "+ fil.getAbsolutePath());
                 }
                update();
            }
        });
        
        //
        // Help menu
        //
        JMenu help = new JMenu("Help");
        menuBar.add(help);
        
        addToMenu(help, "Hint", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//Highlight the most constrained square which is the square where the fewest possible values can go
				for (int i=0; i<9; i++) {
					for (int j=0; j<9; j++) {
						if (sudoku.isBlank(i, j) && sudoku.getLegalValues(i,j).size() == 1) {
							hintRow = i;
							hintCol = j;
							update();
							return;
						}
					}
				}
			}
		});
        
        //Show the legal values
        addToMenu(help, "Legal Values", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Press h key to show the legal values");
			}
        	
        });
        
        //Undo the latest step
        addToMenu(help, "Undo", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sudoku.undo();
				update();
			}
        	
        });
        
        //Let the player know if they can win or not
        addToMenu(help, "Winning chance", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!sudoku.checkWin()) {
					JOptionPane.showMessageDialog(null, "Can't win. There is a position where no legal value can be placed");
				} else {
					JOptionPane.showMessageDialog(null, "You can win the game");
				}
			}
 
        });
        
        this.setJMenuBar(menuBar);
    }
    
    
    /**
     * Private helper method to put 
     * 
     * @param menu
     * @param title
     * @param listener
     */
    private void addToMenu(JMenu menu, String title, ActionListener listener) {
    	JMenuItem menuItem = new JMenuItem(title);
    	menu.add(menuItem);
    	menuItem.addActionListener(listener);
    }
    
    private void createMouseHandler() {
    	MouseAdapter a = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.printf("%s\n", e.getButton());
			}
    		
    	};
        this.addMouseMotionListener(a);
        this.addMouseListener(a);
    }
    
    
    private void createKeyboardHandlers() {
    	for (int r=0; r<buttons.length; r++) {
    		for (int c=0; c<buttons[r].length; c++) {
    			buttons[r][c].addKeyListener(new MyKeyListener(r, c, sudoku));
    		}
    	}
    }
    
    public SudokuGUI() {
        sudoku = new Sudoku();
        // load a puzzle from a text file
        // right now we only have 1 puzzle, but we could always add more!
        sudoku.load("easy1.txt");
        
        setTitle("Sudoku!");

        this.setSize(width, height);
        
        // the JPanel where everything gets painted
        panel = new JPanel();
        // set up a 9x9 grid layout, since sudoku boards are 9x9
        panel.setLayout(new GridLayout(9, 9));
        // set the preferred size
        // If we don't do this, often the window will be minimized
        // This is a weird quirk of Java GUIs
        panel.setPreferredSize(new Dimension(width, height));
        
        // This sets up 81 JButtons (9 rows * 9 columns)
        for (int r=0; r<numRows; r++) {
        	for (int c=0; c<numCols; c++) {
        		JButton b = new JButton();
        		b.setPreferredSize(new Dimension(squareSize, squareSize));
        		
        		b.setFont(FONT);
        		b.setForeground(FONT_COLOR);
        		b.setBackground(BACKGROUND_COLOR);
        		b.setOpaque(true);
        		buttons[r][c] = b;
        		// add the button to the canvas
        		// the layout manager (the 9x9 GridLayout from a few lines earlier)
        		// will make sure we get a 9x9 grid of these buttons
        		panel.add(b);

        		// thicker borders in some places
        		// sudoku boards use 3x3 sub-grids
        		int top = 1;
        		int left = 1;
        		int right = 1;
        		int bottom = 1;
        		if (r % 3 == 2) {
        			bottom = 5;
        		}
        		if (c % 3 == 2) {
        			right = 5;
        		}
        		if (r == 0) {
        			top = 5;
        		}
        		if (c == 9) {
        			bottom = 5;
        		}
        		b.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.black));
        		
        		//
        		// button handlers!
        		//
        		// check the ButtonListener class to see what this does
        		//
        		b.addActionListener(new ButtonListener(r, c, sudoku));
        	}
        }
        
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(width, height));
        this.setResizable(false);
        this.pack();
        this.setLocation(100,100);
        this.setFocusable(true);
        
        createMenuBar();
        createKeyboardHandlers();
        createMouseHandler();
        
        // close the GUI application when people click the X to close the window
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        update();
        repaint();
    }
    
    public static void main(String[] args) {
        SudokuGUI g = new SudokuGUI();
        g.setVisible(true);
    }

}
