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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class SudokuBoard extends JFrame {
	
	private Sudoku sudoku;
    
	private static final long serialVersionUID = 1L;
	
	protected final int MARGIN_SIZE = 5;
    protected final int DOUBLE_MARGIN_SIZE = MARGIN_SIZE*2;
    protected int squareSize = 90;
    private int numRows = 9;
    private int numCols = 9;
    
    private int width = DOUBLE_MARGIN_SIZE + squareSize * numCols;    		
    private int height = DOUBLE_MARGIN_SIZE + squareSize * numRows;    		
    
    private JPanel canvas;
    private JMenuBar menuBar;
    private JButton[][] buttons = new JButton[numRows][numCols];
    
    private class ButtonListener implements ActionListener {
    	public final int row;
    	public final int col;
    	ButtonListener(int row, int col){
    		this.row = row;
    		this.col = col;
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			System.out.printf("row %d, col %d\n", row, col);
			JButton button = (JButton)e.getSource();
			// TODO: cycle through possible values, maybe show button differently as highlight
			
			
			update();
		}
    }
    
    private void setText(int row, int col, String text) {
    	buttons[row][col].setText(text);
    }
    
    private void update() {
    	for (int row=0; row<numRows; row++) {
    		for (int col=0; col<numCols; col++) {
    			int val = sudoku.get(row, col);
    			if (val == 0) {
    				setText(row, col, "");
    			} else {
    				setText(row, col, val+"");
    			}
    		}
    	}
    }
    
	
    private void createMenuBar() {
    	menuBar = new JMenuBar();
        JMenu menu = new JMenu("Actions");
        menuBar.add(menu);
        
        addToMenu(menu, "New Game", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sudoku.load("easy1.txt");
                repaint();
            }
        });
        this.setJMenuBar(menuBar);
    }
    
    private void addToMenu(JMenu menu, String title, ActionListener listener) {
    	JMenuItem menuItem = new JMenuItem(title);
    	menu.add(menuItem);
    	menuItem.addActionListener(listener);
    }
    
    private void createKeyboardHandlers() {
    	this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				System.out.println(e.getKeyChar());
			}
		});
    }
    
    public SudokuBoard() {
        sudoku = new Sudoku();
        sudoku.load("easy1.txt");
        
        setTitle("Sudoku!");

        this.setSize(width, height);
        
        canvas = new JPanel();
        canvas.setLayout(new GridLayout(9, 9));
        canvas.setPreferredSize(new Dimension(width, height));
        
        for (int r=0; r<numRows; r++) {
        	for (int c=0; c<numCols; c++) {
        		JButton b = new JButton();
        		b.setPreferredSize(new Dimension(squareSize, squareSize));
        		b.addActionListener(new ButtonListener(r, c));
        		b.setFont(new Font("Verdana", Font.BOLD, 40));
        		buttons[r][c] = b;
        		canvas.add(b);
        		// TODO thicker borders in some places
        		b.setBorder(BorderFactory.createMatteBorder(3,3,1,1, Color.black));
        	}
        }
        
        this.getContentPane().add(canvas, BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(width, height));
        this.setResizable(false);
        this.pack();
        this.setLocation(100,100);
        this.setFocusable(true);
        
        createMenuBar();
        createKeyboardHandlers();
        
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        
        update();
        repaint();
    }
    
    public static void main(String[] args) {
        SudokuBoard b = new SudokuBoard();
        b.setVisible(true);
    }

}
