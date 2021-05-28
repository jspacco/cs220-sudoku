package knox.sudoku;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;


public class SudokuBoard extends JFrame {
	
	private Sudoku sudoku;
    
	private static final long serialVersionUID = 1L;
	
	protected final int MARGIN_SIZE = 5;
    protected final int DOUBLE_MARGIN_SIZE = MARGIN_SIZE*2;
    protected int squareSize = 90;
    private int numRows = 9;
    private int numCols = 9;
    
    private boolean highlight = false;
    private int highlightRow = 0;
    private int highlightCol = 0;
    
    private int width = DOUBLE_MARGIN_SIZE + squareSize * numCols;    		
    private int height = DOUBLE_MARGIN_SIZE + squareSize * numRows;    		
    
    private JPanel canvas;
    private JMenuBar menuBar;
    
	private void drawGrid(Graphics2D g) {
		g.setColor(Color.BLACK);
		Font font = new Font("Verdana", Font.BOLD, 40);
		g.setFont(font);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
            	int x = c * squareSize + MARGIN_SIZE;
            	int y = r * squareSize + MARGIN_SIZE;
            	
            	String num = sudoku.get(r, c) + "";
            	if (num.equals("0")) {
            		num = "";
            	}

            	g.drawString(num,
                		x + squareSize/3, 
                		y + 2*squareSize/3);

                g.drawRect(x, y, squareSize, squareSize);
                
                // TODO: 3x3 grid squares
                if (r % 3 == 0 && c % 3 == 0) {
                	g.setStroke(new BasicStroke(3.0f));
                	g.drawRect(x, y, squareSize * 3, squareSize * 3);
                	g.setStroke(new BasicStroke(1.0f));
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
    
    private int getRow(int y) {
    	return (y - MARGIN_SIZE) / squareSize;
    }
    
    private int getCol(int x) {
    	return (x - MARGIN_SIZE) / squareSize;
    }
    
    private void createMouseHandlers() {
    	this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				int row = getRow(e.getY());
				int col = getCol(e.getX()); 
				System.out.printf("row=%d, col=%d\n", row, col);
				if (!sudoku.isBlank(row, col)) {
					highlight = true;
					highlightRow = row;
					highlightCol = col;
				}
			}
    		
		});
    }
    
    public SudokuBoard() {
        sudoku = new Sudoku();
        
        setTitle("Sudoku!");

        JFrame frame = this;
        
        canvas = new JPanel() {
        	 /* (non-Javadoc)
             * @see javax.swing.JComponent#getMinimumSize()
             */
            public Dimension getMinimumSize() {
                return new Dimension(width, height);
            }
            
            /* (non-Javadoc)
             * @see javax.swing.JComponent#getMaximumSize()
             */
            public Dimension getMaximumSize() {
                return new Dimension(width, height);
            }
            
            /* (non-Javadoc)
             * @see javax.swing.JComponent#getPreferredSize()
             */
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
            
            /* (non-Javadoc)
             * @see java.awt.Component#isFocusable()
             */
            public boolean isFocusable() {
                return true;
            }

			@Override
        	public void paint(Graphics graphics) {
        		Graphics2D g = (Graphics2D)graphics;

        		drawGrid(g);

        		//frame.setPreferredSize(new Dimension(numRows*squareSize + MARGIN_SIZE, numCols*squareSize + MARGIN_SIZE));
        		setPreferredSize(new Dimension((numCols+2)*squareSize + 2*MARGIN_SIZE, (numRows+2)*squareSize + 2*MARGIN_SIZE));
        		frame.pack();
        	}
        };
        
        this.getContentPane().add(canvas, BorderLayout.CENTER);
        this.setResizable(false);
        this.pack();
        this.setLocation(100,100);
        this.setFocusable(true);
        
        createMenuBar();
        createKeyboardHandlers();
        createMouseHandlers();
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        
        repaint();
    }
    
    public static void main(String[] args) {
        SudokuBoard b = new SudokuBoard();
        b.setVisible(true);
    }

}
