package knox.sudoku;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
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
    
    private int hintRow = -1;
    private int hintCol = -1;
    
    private boolean showLegalValues = false; 

    
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
			if (Character.isDigit(key)) {
				// use ascii values to convert chars to ints
				int digit = key - '0';
				System.out.println(key);
				if (currentRow == row && currentCol == col) {
					if(!sudoku.isLegal(row,  col,  digit)) {
						JOptionPane.showMessageDialog(null, 
								String.format("%d cannot go in row %d and col %ed", digit, row, col));
					}else {
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
				// or pop up a selector with only the legal valuess
				
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
    			if(hintRow == row && hintCol == col){
    				buttons[row][col].setBackground(Color.pink);
    				setText(row, col, "");
    			
    		}else if(row == currentRow && col == currentCol && sudoku.isBlank(row, col)) {
    				// draw this grid square special!
    				// this is the grid square we are trying to enter value into
    				buttons[row][col].setForeground(Color.RED);
    				// I can't figure out how to change the background color of a grid square, ugh
    				// Maybe I should have used JLabel instead of JButton?
    				buttons[row][col].setBackground(Color.CYAN);
    				setText(row, col, "_");
    				if(showLegalValues) {
    					Collection<Integer> legals = sudoku.getLegalValues(row, col);
    					JOptionPane.showMessageDialog(null, legals.toString());
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
            	String board = sudoku.toFileString(); 
            	
            	JFileChooser jfc = new JFileChooser(new File("."));
            	
            	int returnValue = jfc.showSaveDialog(null);
            	
            	if(returnValue == JFileChooser.APPROVE_OPTION) {
            		File selectedFile = jfc.getSelectedFile();
            		Util.writeToFile(selectedFile, board);
            		JOptionPane.showMessageDialog(null,
            				"Saved game to file " +selectedFile.getAbsolutePath());
            		System.out.println(selectedFile.getAbsolutePath());
            	}
             
                update();
            }

        });
        
        addToMenu(file, "Load", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	JFileChooser jfc = new JFileChooser(new File("."));
            	
            	int returnValue = jfc.showOpenDialog(null);
            	
            	if(returnValue == JFileChooser.APPROVE_OPTION) {
            		File selectedFile = jfc.getSelectedFile();
            		sudoku.load(selectedFile);
            		JOptionPane.showMessageDialog(null,
            				"Loaded game to file " +selectedFile.getAbsolutePath());
            		System.out.println(selectedFile.getAbsolutePath());
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
				for(int r=0; r<9; r++) {
					for(int c=0; c<9; c++) {
						if(sudoku.isBlank(r, c) && sudoku.getLegalValues(r, c).size() ==1) {
							hintRow = r;
							hintCol = c; 
							update();
							return;
						}
					}
				}
				JOptionPane.showMessageDialog(null, "Give the user a hint! Highlight the most constrained square\n" + 
						"which is the square where the fewest posssible values can go");
			}
		});
        
        JMenuItem menuItem = new JCheckBoxMenuItem("Show Legals");
        help.add(menuItem);
        menuItem.addItemListener(new ItemListener() {
        	@Override
        	public void itemStateChanged(ItemEvent e) {
        		showLegalValues = !showLegalValues;
        	}
        });
        
        JMenuItem menuItem1 = new JCheckBoxMenuItem("Sudoku Joke");
        help.add(menuItem1);
        menuItem1.addItemListener(new ItemListener() {
        	@Override
        	public void itemStateChanged(ItemEvent e) {
        	JOptionPane.showMessageDialog(null, "What do you call a fake Sudoku?");
        	}
        	});
        
        JMenuItem menuItem2 = new JCheckBoxMenuItem("Joke Solution");
        help.add(menuItem2);
        menuItem2.addItemListener(new ItemListener() {
        	@Override
        	public void itemStateChanged(ItemEvent e) {
        	JOptionPane.showMessageDialog(null, "A pseudo-ku");
        	}
        });
        
        
        this.setJMenuBar(menuBar);
    }
    
    
    private void setJMenuBar(JMenuBar menuBar) {
		// TODO Auto-generated method stub
		
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
    
    
    private void addMouseListener(MouseAdapter a) {
		// TODO Auto-generated method stub
		
	}

	private void addMouseMotionListener(MouseAdapter a) {
		// TODO Auto-generated method stub
		
	}

	private void createKeyboardHandlers() {
    	for (int r=0; r<buttons.length; r++) {
    		for (int c=0; c<buttons[r].length; c++) {
    			buttons[r][c].addKeyListener(new MyKeyListener(r, c, sudoku));
    		}
    	}
    }
    
    public void SudokuGUI() {
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
    
    private void setDefaultCloseOperation(int exitOnClose) {
		// TODO Auto-generated method stub
		
	}

	private void setFocusable(boolean b) {
		// TODO Auto-generated method stub
		
	}

	private void setLocation(int i, int j) {
		// TODO Auto-generated method stub
		
	}

	private void pack() {
		// TODO Auto-generated method stub
		
	}

	private void setResizable(boolean b) {
		// TODO Auto-generated method stub
		
	}

	private void setPreferredSize(Dimension dimension) {
		// TODO Auto-generated method stub
		
	}

	private Container getContentPane() {
		// TODO Auto-generated method stub
		return null;
	}

	private void setSize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
        SudokuGUI g = new SudokuGUI();
        g.setVisible(true);
    }

}
 }
