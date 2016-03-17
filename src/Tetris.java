/**
 * Name     : Tetris.java
 * Synopsis : Tetris
 * Date     : 2010. 3. 10
 * E-mail   : chobocho1004 at gmail.com
 * 
 * Update   : 2016. 3. 17
 */  
 
import java.awt.* ;
import java.awt.event.* ;
import java.awt.image.* ;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class Tetris extends JFrame implements ActionListener, Runnable {
    
    JLabel Jlbl_Statusbar;
	
    final int BOARD_X = 400;
    final int BOARD_Y = 565;
    final int BOARD_WIDTH = 10;
    final int BOARD_HEIGHT = 20;
    final int EMPTY = 0;
    final int REMOVE = 255;
	
	
	
	public Tetris() {
		Jlbl_Statusbar = new JLabel(" 0");
		add(Jlbl_Statusbar, BorderLayout.SOUTH);
 
		
        setSize(400, 700);
        setTitle("ChoboTetris");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JLabel getStatusBar() {
       return Jlbl_Statusbar;
    }

	public void init ()
	{

 	    addKeyListener (new TKeyAdapter()) ;
        addMouseListener (new TMouseAdapter());
		
		initGame () ;     
		loadImage() ;     
		playDisplay () ;  

	}

	
    public static void main(String[] args) {

        Tetris tetris = new Tetris();
        tetris.setLocationRelativeTo(null);
        tetris.setVisible(true);
		tetris.init();

    } 
	
	class TMouseAdapter extends MouseAdapter {
			public void mousePressed (MouseEvent e) 
   	{
   	  int score = 0;
   	  int metric = 0;
   	  
	   	xMouse = e.getX () ;
	   	yMouse = e.getY () ;
	 
	   	if (yMouse < 225 && xMouse >= (BOARD_X - 78) && xMouse <= BOARD_X) {
	   		if (gameState == 1) {
		   		 gameState = 2;
	   			 repaint();
	   		 	 return;
   			}
   			else if (gameState >= 3) {
		   		 initVar () ;     
		   		 gameState = 1;
	   			 repaint();
	   		 	 return;
   			}
   		
   		}
	   	if (yMouse < 36 || gameState !=2)	return;
	   	
	
	}
		
	}
	
	class TKeyAdapter extends KeyAdapter {
	
	    public void keyPressed (KeyEvent e) 
	    {	int key = e.getKeyCode () ;
		
		if (gameState == 1) {
			if ( key == 'P' || key == 'p' ) {
			 	 gameState = 2;
	   			 repaint();
	   		 	 return;
   			}
		}
		
		else if (gameState == 2) 
		{
			switch (key) 
			{
				 case KeyEvent.VK_UP :
				  m_block.rotate();
				  if ( (m_block.checkRotate() != 0) || (checkBoard(4) != 0) ) {
				    m_block.rotate();
				    m_block.rotate();
				    m_block.rotate();
				  }
			 		break ;
			 		
			 	 case KeyEvent.VK_DOWN :
			 	  if ( (m_block.checkDown() == 0) && (checkBoard(3) == 0) )
			 	  {
			 	      m_block.moveDown();  
			 	  }
			 		break ;
			 	
			 	case KeyEvent.VK_LEFT :
			 	  if ( (m_block.checkLeft() == 0) && (checkBoard(1) == 0) )
			 	  {
			 	      m_block.moveLeft();  
			 	  }
			 	  break;
			 	  
			 	case KeyEvent.VK_RIGHT :
			 	  if ( (m_block.checkRight() == 0) && (checkBoard(2) == 0) )
			 	  {
			 	      m_block.moveRight();  
			 	  }
			 		break ;  
			 		
			 	case KeyEvent.VK_ENTER :
			 	case KeyEvent.VK_SPACE :
			 	  while ( (m_block.checkDown() == 0) && (checkBoard(3) == 0) )
			 	  {
			 	      m_block.moveDown();  
			 	  }
			 	  setBoard();
			 	  m_score += removeBoard();
			 	  m_block.setBlock(m_next_block);
				  m_next_block.new_block();
			 		break 
			 		
			 		;
			 	default:
			 	  break;
			}	
			
			repaint();
		}	
	
	    }	
	}  

    public void actionPerformed(ActionEvent e) {
  
    }
 			public void start ()
	{
		if (gameThread == null) {
			gameThread = new Thread(this);
			gameThread.start();
		}
	}
	//////////////////////////////////////////////
	
//////////////////////////////////////////////////////////////////////
// Name     : playDisplay()
// Synopsis : Display Game Screen
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
	public void playDisplay ()
	{
		font = new java.awt.Font("TimesRoman",1,30);	
		playScrGC.setFont(font);
		drawBoard();
		repaint();
	}
	
	//////////////////////////////////////////////////////////////////////
// Name     : initGame();
// Synopsis : init variables of game
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
	public void initGame ()
	{
		width  = BOARD_X;	                // width of board
		height = BOARD_Y;                 // hegith of board
		gameThread = null;
		m_Board    = new int[BOARD_WIDTH][BOARD_HEIGHT];  
		
		for (int i = 0; i < BOARD_WIDTH; i++)
		{
		  m_Board[i] = new int[BOARD_HEIGHT];
		}
		
		numImage   = new Image [number_of_number]; 
	  fruitImage = new Image [number_of_fruit];
		
		initVar();
		resize (width, height) ;
	
		playScrImage = createImage (width, height) ;
		playScrGC = playScrImage.getGraphics () ;
	}

//////////////////////////////////////////////////////////////////////
// Name     : initVar();
// Synopsis : 
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   :
//////////////////////////////////////////////////////////////////////
	public void initVar() 
	{
		int count = 0;
		
		gameState = 0;                
		m_iTime = 0 ;                 
		xMouse = 0 ;                  
		yMouse = 0 ;
    checked = 0;
    m_block = new Block();
    m_block.new_block();
    m_next_block = new Block();
    m_next_block.new_block();
    

		for (int i = 0 ; i < BOARD_HEIGHT; i++) {
			for (int j = 0; j < BOARD_WIDTH; j++) {
				m_Board[j][i] = EMPTY;
			}
		}
				
  }
	

//////////////////////////////////////////////////////////////////////
// Name     : loadImage()
// Synopsis : 
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
	public void loadImage ()
	{
		
		playScrGC.setColor (Color.black) ;
		playScrGC.fillRect (0, 0, width, height) ;	
		
		init_image ("..\\res\\number.gif",           10, sizeOfNumber, sizeOfNumber+10, numImage);
		init_image ("..\\res\\fruit.gif",number_of_fruit, size_of_fruit, size_of_fruit, fruitImage);
	
		gameState = 1; 
	}

	
//////////////////////////////////////////////////////////////////////
// Name     : init_image
// Synopsis : 
// Input    : String, int, int, int, Image[]
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
	public void init_image(String fileName, int numOfImage, int xSize, int ySize, Image[] images) {
		
        Toolkit tk = Toolkit.getDefaultToolkit();
        tempImage = tk.createImage(fileName);
	
		tracker = new MediaTracker (this) ;
		tracker.addImage (tempImage, 0) ;
		producer = tempImage.getSource () ;
				
		for (int i = 0 ; i < numOfImage ; i++)
		{
				filter = new CropImageFilter (xSize * i, 0, xSize, ySize) ;	
				images[i] = createImage (new FilteredImageSource(producer, filter)) ;
				tracker.addImage(images[i], 1) ;
		}
					
		try {
			tracker.waitForAll() ;
		}
		catch (InterruptedException ie) {} 
			
		while ((tracker.statusAll(true) & MediaTracker.COMPLETE) == 0) { }  

	}

	
   	
		public int removeBoard()
		{
			int count = 0;
			int score = 0;
			int x, y;
	
			for(y = 19; y >= 0; y-- )
			{
			    count = 0;
          for(x = 0; x < 10; x++) 
          {
              if ( m_Board[x][y] != EMPTY )
					    {
						    count++;
					    }
			    }
			    
			    if (count == 10)
			    {
          for(x = 0; x < 10; x++) 
          {
              m_Board[x][y] = REMOVE;
			    }
			       score += 10;
     
			    }
			}
			
			arrangeBoard();
			
			return score;
		}
		
		
		public int checkBoard( int dir )
		{
		    int i = 0;
		    int count = 0;
		    switch (dir)
		    {
		        case 1: // Left
		            for (i = 0; i < 4; i++)
		            {
		              if ( m_Board[m_block.x - 1 + m_block.m_block[i][0]][m_block.y + m_block.m_block[i][1]] != EMPTY)
		              {
		                count++;
		              }
		            }
		            break;

		        case 2: // Right
		            for (i = 0; i < 4; i++)
		            {
		              if ( m_Board[m_block.x + 1 + m_block.m_block[i][0]][m_block.y + m_block.m_block[i][1]] != EMPTY)
		              {
		                count++;
		              }
		            }
		            break;
		        case 3: // Down
		            for (i = 0; i < 4; i++)
		            {
		              if ( m_Board[m_block.x + m_block.m_block[i][0]][m_block.y + 1 + m_block.m_block[i][1]] != EMPTY)
		              {
		                count++;
		              }
		            }
                break;
		        case 4: // Rotate
		            for (i = 0; i < 4; i++)
		            {
		              if ( m_Board[m_block.x + m_block.m_block[i][0]][m_block.y + m_block.m_block[i][1]] != EMPTY)
		              {
		                count++;
		              }
		            }
                break;
                

		        default:
		           break;  
		    }
		    return count; 
		}
		
		public void setBoard()
		{
		    int i = 0;
		    
		    for (i = 0; i < 4; i++)
		    {
		        m_Board[m_block.x + m_block.m_block[i][0]][m_block.y + m_block.m_block[i][1]] = m_block.color;
		    }
		}
		
		public void arrangeBoard()
		{
		  
		  int x, y, m;

      for( y = BOARD_HEIGHT - 1; y >= 0; y--) 
			{
			    if (m_Board[0][y] == REMOVE) {
		    			for( x = 0; x < BOARD_WIDTH; x++ )
              {
                 for ( m = y; m > 0; m--) {
                     m_Board[x][m] = m_Board[x][m-1];  
                 }
                 m_Board[x][0] = EMPTY;
              }    
              y++;
					}
          
			}
		
		}   	


//////////////////////////////////////////////////////////////////////
// Name     : drawBoard()
// Synopsis : 
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
	public void drawBoard ()
	{
		int i, j;
		int xVal = 0, yVal = 35;
    int temp_score[] = new int[7];
    int score = m_score;
    int timebar_x = 2;
    int timebar_size = 176;
	int startY = 50;
	int startX = 10;
  
 	
 	  for (i = 1; i <= 6; i++)
	  {
	    temp_score[i] = score % 10;
	    score /= 10;
	  }
	
		for (i = 6; i > 0; i--)
		{ 
		      playScrGC.drawImage (numImage[temp_score[i]], BOARD_X - (sizeOfNumber * i) - startX, startY + 1, this);	
		} 
		
		// Draw board
		for (i = 0; i < BOARD_HEIGHT; i++)
			for (j = 0; j < BOARD_WIDTH; j++) {
					playScrGC.drawImage (fruitImage[m_Board[j][i]], j*size_of_fruit + startX, i* size_of_fruit + startY, this) ;	
			}

		for (i = 0; i < 4; i++)
			for (j = 0; j < 4; j++) {
					playScrGC.drawImage (fruitImage[EMPTY], i*size_of_fruit + 275 + startX, j*size_of_fruit+200+startY, this) ;	
			}

   for (i = 0; i < 4; i++)
   {
     playScrGC.drawImage (fruitImage[m_next_block.color], (1 + m_next_block.m_block[i][0])*size_of_fruit + 275+startX, (1 + m_next_block.m_block[i][1])* size_of_fruit + 200+startY, this) ;
   } 		
 		
 		
//		playScrGC.setColor (Color.black) ;
//		playScrGC.fillRect (300, 200, 0, 100, 100) ;

   // Draw block
   for (i = 0; i < 4; i++)
   {
     playScrGC.drawImage (fruitImage[m_block.color], (m_block.x + m_block.m_block[i][0])*size_of_fruit+startX, (m_block.y + m_block.m_block[i][1])* size_of_fruit+startY, this) ;
   }
   
   Jlbl_Statusbar.setText("paused");
   
 	}

	
//////////////////////////////////////////////////////////////////////
// Name     : gameOver()
// Synopsis :
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
   	public void gameOver() {
	   	gameState = 3;
	}

//////////////////////////////////////////////////////////////////////
// Name     : paint
// Synopsis : 
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
	public void paint (Graphics g)
	{

		if (gameState >= 1) drawBoard();
		if (gameState == 3) {
			playScrGC.setColor (Color.black) ;
			playScrGC.drawString ("Game Over", 72, 202);
			playScrGC.setColor (Color.yellow) ;
			playScrGC.drawString ("Game Over", 75, 200);
		}
		else if (gameState == 4) {
			playScrGC.setColor (Color.black) ;
			playScrGC.drawString ("You Win", 82, 202);
			playScrGC.setColor (Color.blue) ;
			playScrGC.drawString ("You Win", 85, 200);
		}
	
		g.drawImage (playScrImage, 0, 0, null);
	}

	//////////////////////////////////////////////////////////////////////
// Name     : run
// Synopsis : 
// Input    : void
// Return   : void
// Date     : 2003. 1. 7
// Update   : 2003. 1. 11
//////////////////////////////////////////////////////////////////////
	public void run () {
		while (gameThread != null) {
			try {
				gameThread.sleep(1000);
			}
			catch (InterruptedException ie) { }
			if (gameState == 2) {
				  if ( (m_block.checkDown() != 0) || (checkBoard(3) != 0) )
			 	  {
			 	    
			 	      setBoard();  
			 	      m_score += removeBoard();
			 	      
 			 	      if (m_block.y == 1)
			 	      {
			 	          gameOver();
			 	      }
              else {
			 	          m_block.setBlock(m_next_block);
				          m_next_block.new_block();
				      }
			 	  }
			 	  else if ( (m_block.checkDown() == 0) && (checkBoard(3) == 0) ) {
			 	      m_block.moveDown();
			 	  }
			}
			repaint () ;
		}
	}

	
	public void stop ()
	{
	
	}
	
	public void destory ()
	{
		// ToDo : Program Terminated
	}
	
	public void update (Graphics g)
	{
		paint(g) ;
	}
	
	public boolean imageUpdate (Image img, int infoflags, int x, int y, int width, int height)
	{
		if (infoflags == ALLBITS)
		{
			gameLoad = true ;
			return false ;
		}
		else 
			return true ;
	}
		
/////////////////////////////////////////////////////////////////		
	Thread	  gameThread ;
	
	Font      font ;
	Image 	  tempImage;               
	Image     numImage[];               
	
	Image     fruitImage[];

	Image  	  playScrImage;
	Graphics  playScrGC;		        
	
	
	int       width, height ;
	boolean   gameLoad ;
	
	MediaTracker  tracker ;
	ImageFilter   filter ;
	ImageProducer producer ;

	int[][] m_Board;
  int     gameState = 0;      		
  int     xMouse, yMouse ; 			
  int     m_iTime;                
  int     m_score;
    
  int     checked;
  
  Block   m_block, m_next_block;       
         
                                                     
  final int sizeOfSmile   = 25;
  final int sizeOfNumber  = 13;
  final int number_of_number = 10;
    
  final int size_of_fruit = 25;
  final int number_of_fruit = 7;
}


class Block {

  public int    x;
  public int    y;
  public int    type; 
  public int    color; 
  final  int BOARD_WIDTH = 10;
  final  int BOARD_HEIGHT = 20;   
   
  public static final int  m_blocks[][][] = { 
                      { {0,0}, {-1, 0}, { 0,-1}, {-1, 1} },
                      { {0,0}, { 0, 1}, {-1,-1}, {-1, 0} },
                      { {0,0}, { 0, 1}, { 0,-1}, { 1, 0} }, 
                      { {0,0}, { 0,-1}, { 0, 1}, { 0, 2} }, 
                      { {0,0}, {-1, 0}, {-1, 1}, { 0, 1} },
                      { {0,0}, { 0,-1}, { 0, 1}, { 1,-1} }, 
                      { {0,0}, { 0,-1}, { 0, 1}, { 1, 1} } };   

  public int[][] m_block;
  
  public void new_block ()
  {
     int temp;
     
     x = 5;
     y = 1;
     
     type = (int)(Math.random()*7);
     color = (int)(Math.random()*6)+1;
      
     for (int i = 0; i < 4; i++)
		 {
		    for (int j = 0; j < 2; j++)
		    {
            temp = m_blocks[type][i][j];   
		        m_block[i][j] = temp;
		    }
		 }
  }
  
  
  public void setBlock(Block next)
  {
    int temp;
    x = 5;
    y = 1;
    type = next.type;
    color = next.color;
    
    for (int i = 0; i < 4; i++)
		 {
		    for (int j = 0; j < 2; j++)
		    {
            temp = m_blocks[type][i][j];   
		        m_block[i][j] = temp;
		    }
		 }
  }
  
  public int rotate()
  {
       int temp = 0;
       int i = 0;
        
       for (i = 0; i < 4; i++)
       {
          temp = m_block[i][0];
          m_block[i][0] = -m_block[i][1];
          m_block[i][1] = temp;
       }
       
       return 1;
  }
  
  
  
  public int checkDown()
  {
    int i = 0;
    int check = 0;
    
    for (i = 0; i < 4; i++)
    {
      if ( y + m_block[i][1] >= (BOARD_HEIGHT-1) ) 
          check++;
    }
    
   
    return check;
  }
  
  public int checkRotate()
  {   
    int i = 0;
    int check = 0;
    
    for (i = 0; i < 4; i++)
    {
      if ( x + m_block[i][0] < 0 ) 
          check++;
      
      if ( x + m_block[i][0] >= (BOARD_WIDTH-1) ) 
          check++;
          
      if ( y + m_block[i][1] >= (BOARD_HEIGHT-1) ) 
          check++;              
    }
    
    return check;
  
  }
  
  public int checkRight()
  {
    int i = 0;
    int check = 0;
    
    for (i = 0; i < 4; i++)
    {
      if ( x + m_block[i][0] >= (BOARD_WIDTH-1) ) 
          check++;
      
    }
    
    return check;
  }
  
  public int checkLeft()
  {
    int i = 0;
    int check = 0;
    
    for (i = 0; i < 4; i++)
    {
      if ( x + m_block[i][0] <= 0 ) 
          check++;
    }
    
    return check;
  }
  
  public void moveLeft()
  {
       x--; 
  }
  
  public void moveRight()
  {
       x++;
  }
  
  public void moveDown()
  {
       y++;
  }
  
  public Block() {
     x = 5;
     y = 3;
     type = (int)(Math.random()*7);
     color = (int)(Math.random()*6)+1;
      
     m_block = new int[4][2];
     
     for (int i = 0; i < 4; i++)
		 {
		    m_block[i] = new int[2];
		 }
		
   
  }
}
