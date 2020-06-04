package proximity;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.game.Sprite;
import java.io.IOException;

public class MyGameCanvas
  extends GameCanvas implements Runnable {

	private Image gameBoard;
	private Image blueMessageBox;
	private Image redMessageBox;
	private Image titleMessageBox;
	private Image HighScores;
	private Image NewGame;
	private Image Options;
	private Image OptionLabels;
	private Image PlayerLabels;
	private Image CriteriaLabels;
	private Image DifficultyLabels;
	private Image Instructions;
	private Image InstructionsBox;
	private Image Alphabet;
	private Image redimage;
	private Image blueimage;
	private Image highlightimage;
	private OptionStorage os = new OptionStorage();
	private GameBoard Board;
	private int count = 0;
	private String MenuLocation = "MainMenu";
	private String HSState = "";
	private String HSNameText = "";
	private String[] WinCriteriaList = {"Score", "Armies"};
	private String[] PlayerTypeList = {"Human", "Computer"};
	private String[] DifficultyList = {"Hard", "Medium", "Easy"};
	private HighScore hs = new HighScore();
	private String[] AlphabetList = {
			"A","B","C","D","E",
			"F","G","H","I","J",
			"K","L","M","N","O",
			"P","Q","R","S","T",
			"U","V","W","X","Y",
			"Z",".","-","_"
		};
	private int AlphabetPos;
	private int HSNameLength = 3;
	private int HSNamePos = 0;
	private String[] HSName = new String[HSNameLength];
	
	private int RedPlayerIDX = this.getIDX(PlayerTypeList, os.names[0]);
	private int BluePlayerIDX = this.getIDX(PlayerTypeList, os.names[1]);
	private int ScoringIDX = this.getIDX(WinCriteriaList, os.names[2]);
	private int DifficultyIDX = this.getIDX(DifficultyList, os.names[3]);
	private int OptionMenu = 0;
	private int OptionMenuCount = 4;
	private int MainMenu = 0;
	private int MainMenuCount = 4;
	public static final int GAME_WIDTH = 176;
	public static final int GAME_HEIGHT = 220;

	public MyGameCanvas() {
	  super(true);
	  setFullScreenMode( true ); 
	}

	public void start() {

	  try {
		  
		gameBoard = Image.createImage("/GameBoard.png");
		blueMessageBox = Image.createImage("/blueMessageBox.png");
		redMessageBox = Image.createImage("/redMessageBox.png");
		titleMessageBox = Image.createImage("/titleMessageBox.png");
		HighScores = Image.createImage("/HighScores.png");
		NewGame = Image.createImage("/NewGame.png");
		Options = Image.createImage("/Options.png");
		OptionLabels = Image.createImage("/OptionLabels.png");
		PlayerLabels = Image.createImage("/PlayerLabels.png");
		DifficultyLabels = Image.createImage("/DifficultyLabels.png");
		CriteriaLabels = Image.createImage("/CriteriaLabels.png");
		Instructions = Image.createImage("/Instructions.png");
		InstructionsBox = Image.createImage("/InstructionsBox.png");
		Alphabet = Image.createImage("/Alphabet.png");
		redimage = Image.createImage("/redpieces.png");
		blueimage = Image.createImage("/bluepieces.png");
		highlightimage = Image.createImage("/cellselection.png");
		
	  } catch(IOException ioex) {
		  ioex.printStackTrace();
		  System.err.println(ioex); 
	  }

	  Thread runner = new Thread(this);
	  runner.start();

	}

	public void initGame() {
		Board = new GameBoard();
	}
	
	public void setGame() {
		for (int i=0; i<Board.getPlayerCount(); i++) {
			Board.setPlayer(i, os.names[i]);
		}
		int nextentry = Board.getPlayerCount();
		Board.setCriteria(os.names[nextentry]);
		Board.setDifficulty(os.names[nextentry+1]);
	}
	
	public void run() {

	  // the graphics object for this canvas
	  Graphics g = getGraphics();
	  
	  while (true) { // infinite loop
		  initGame();
		  
		  while (Board.isNew() ) {
			  if (MenuLocation.equals("MainMenu")) {
				  checkMainMenuInput();
				  updateMainMenuScreen(g);
			  } else if (MenuLocation.equals("HighScores")) {
				  checkHSMenuInput();
				  updateHighScoreScreen(g);
			  } else if (MenuLocation.equals("Options")) {
				  checkOptionsMenuInput();
				  updateOptionsScreen(g);
			  } else if (MenuLocation.equals("Instructions")) {
				  checkInstructionsInput();
				  updateInstructionsScreen(g);
			  } else {
				  checkMainMenuInput();
				  updateMainMenuScreen(g);
			  }
			  
			  try {
				  Thread.sleep(100);
			  } catch(Exception e) {}
		  }
		  
		  setGame();
		  

		  while(!Board.isNew() && !Board.isVictory()) { 
			  
			  // check user's input
			  checkUserInput();

			  updateGameScreen(g);
		  		
			  try {
				  Thread.sleep(150);
			  } catch(Exception e) {}
		  }
		  
		  count = 0;
		  while (!Board.isHighScoreFinished()) {
			  checkWinnerInput();
			  displayWinner(g);
		  }
	  }
	}
	
	private int getIDX (String[] checkArray, String s) {
		int idx = 0, i;
		for (i=0;i<checkArray.length;i++) {
			if (checkArray[i].equals(s)) {
				idx = i;
			}
		}
		return idx;
	}
	
	private void checkOptionsMenuInput() {
		int keyState = getKeyStates();
	    
		if ((keyState & DOWN_PRESSED) != 0) {
			OptionMenu = (OptionMenu+1) % OptionMenuCount;
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if ((keyState & UP_PRESSED) != 0) {
			OptionMenu--;
			if (OptionMenu == -1 ) {
				OptionMenu = OptionMenuCount - 1;
			}
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if ((keyState & RIGHT_PRESSED) != 0) {
			switch (OptionMenu) {
				case 0:
					RedPlayerIDX = (RedPlayerIDX + 1) % PlayerTypeList.length;
					os.names[0] = PlayerTypeList[RedPlayerIDX];
					break;
				case 1:
					BluePlayerIDX = (BluePlayerIDX + 1) % PlayerTypeList.length;
					os.names[1] = PlayerTypeList[BluePlayerIDX];
					break;
				case 2:
					ScoringIDX = (ScoringIDX + 1) % WinCriteriaList.length;
					os.names[2] = WinCriteriaList[ScoringIDX];
					break;
				case 3:
					DifficultyIDX = (DifficultyIDX + 1) % DifficultyList.length;
					os.names[3] = DifficultyList[DifficultyIDX];
					break;
			}
			os.addOptions();
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if ((keyState & LEFT_PRESSED) != 0) {
			switch (OptionMenu) {
				case 0:
					RedPlayerIDX--;
					if (RedPlayerIDX == -1) {
						RedPlayerIDX = PlayerTypeList.length -1;
					}
					os.names[0] = PlayerTypeList[RedPlayerIDX];
					break;
				case 1:
					BluePlayerIDX--;
					if (BluePlayerIDX == -1) {
						BluePlayerIDX = PlayerTypeList.length -1;
					}
					os.names[1] = PlayerTypeList[BluePlayerIDX];
					break;
				case 2:
					ScoringIDX--;
					if (ScoringIDX == -1) {
						ScoringIDX = WinCriteriaList.length -1;
					}
					os.names[2] = WinCriteriaList[ScoringIDX];
					break;
				case 3:
					DifficultyIDX--;
					if (DifficultyIDX == -1) {
						DifficultyIDX = DifficultyList.length -1;
					}
					os.names[3] = DifficultyList[DifficultyIDX];
					break;
			}
			os.addOptions();
			try {
			  Thread.sleep(100);
			} catch(Exception e) {}
		} else if ((keyState & FIRE_PRESSED) != 0) {
			MenuLocation = "MainMenu";
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		}
	}

	private void updateOptionsScreen(Graphics g) {
		
		g.setClip(0,0,GAME_WIDTH,GAME_HEIGHT);
		g.setColor(0xffffff);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT); 
		buildGameScreen(g);

		g.drawImage(titleMessageBox, 0, 0, 0);
		
		Sprite optionsSprite = new Sprite(Options,55,19);
		optionsSprite.setFrame(1);
		optionsSprite.setPosition(62,50);
		optionsSprite.paint(g);
		g.setColor(0x000000);
		
		RedPlayerIDX = this.getIDX(PlayerTypeList, os.names[0]);
		BluePlayerIDX = this.getIDX(PlayerTypeList, os.names[1]);
		ScoringIDX = this.getIDX(WinCriteriaList, os.names[2]);
		DifficultyIDX = this.getIDX(DifficultyList, os.names[3]);
		
		Sprite RedPlayerLabel = new Sprite(PlayerLabels, 54, 12); 
		Sprite BluePlayerLabel = new Sprite(PlayerLabels, 54, 12);
		Sprite ScoringLabel = new Sprite(CriteriaLabels, 37, 12);
		Sprite DifficultyLabel = new Sprite(DifficultyLabels, 45, 12);
		
		g.drawImage(OptionLabels, 35, 80, Graphics.LEFT | Graphics.TOP);
		g.setFont(Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_SMALL));
		
		if (OptionMenu == 0) {
			RedPlayerLabel.setFrame(RedPlayerIDX * 2);
		} else {
			RedPlayerLabel.setFrame(RedPlayerIDX * 2 + 1);
		}
		RedPlayerLabel.setPosition(90,80);
		RedPlayerLabel.paint(g);
		
		if (OptionMenu == 1) {
			BluePlayerLabel.setFrame(BluePlayerIDX * 2);
		} else {
			BluePlayerLabel.setFrame(BluePlayerIDX * 2 + 1);	
		}
		BluePlayerLabel.setPosition(90,100);
		BluePlayerLabel.paint(g);
		
		if (OptionMenu == 2) {
			ScoringLabel.setFrame(ScoringIDX * 2);
		} else {
			ScoringLabel.setFrame(ScoringIDX * 2 + 1);
		}
		ScoringLabel.setPosition(90,120);
		ScoringLabel.paint(g);
		
		
		if (OptionMenu == 3) {
			DifficultyLabel.setFrame(DifficultyIDX * 2);
		} else {
			DifficultyLabel.setFrame(DifficultyIDX * 2 + 1);
		}
		DifficultyLabel.setPosition(90,140);
		DifficultyLabel.paint(g);
		
		g.drawString("Press OK", 88, 180, Graphics.HCENTER|Graphics.BOTTOM);
		flushGraphics();
	}
	
	private void checkHSMenuInput() {
		int keyState = getKeyStates();
	    
		if ((keyState & FIRE_PRESSED) != 0) {
			MenuLocation = "MainMenu";
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		}
	}
	
	private void checkInstructionsInput() {
		int keyState = getKeyStates();
	    
		if ((keyState & FIRE_PRESSED) != 0) {
			MenuLocation = "MainMenu";
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		}
	}

	private void updateHighScoreScreen(Graphics g) {
		
		g.setClip(0,0,GAME_WIDTH,GAME_HEIGHT);
		g.setColor(0xffffff);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
	
		buildGameScreen(g);
		g.drawImage(titleMessageBox, 0, 0, 0);
		
		Sprite hsSprite = new Sprite(HighScores,75,19);
		hsSprite.setFrame(1);
		hsSprite.setPosition(50,50);
		hsSprite.paint(g);
		
		int i;
		int c=0;
		for (i=0;i<hs.values.length;i++) {
			if (hs.values[i] > 0) {
				c++;
				g.setColor(0x000000); 
				g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
				g.drawString(""+c+". "+hs.names[i], 45, 82 + ((c-1)*20), Graphics.LEFT|Graphics.BOTTOM);				
				g.drawString(""+hs.values[i], 125, 82 + ((c-1)*20), Graphics.RIGHT|Graphics.BOTTOM);
			}
		}
		g.setColor(0x000000); 
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
		g.drawString("Press OK", 88, 180, Graphics.HCENTER|Graphics.BOTTOM);
		
		flushGraphics();
	}
	private void updateInstructionsScreen(Graphics g) {
		
		g.setClip(0,0,GAME_WIDTH,GAME_HEIGHT);
		g.setColor(0xffffff);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
	
		buildGameScreen(g);
		g.drawImage(InstructionsBox, 0, 0, 0);
		
		flushGraphics();
	}
	
	private void checkMainMenuInput() {
		int keyState = getKeyStates();
	    	
		if((keyState & DOWN_PRESSED) != 0) {
			MainMenu = (MainMenu + 1) % MainMenuCount;
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if((keyState & UP_PRESSED) != 0) {
			this.MainMenu--;
			if (this.MainMenu == -1 ) {
				this.MainMenu = MainMenuCount - 1;
			}
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if ((keyState & FIRE_PRESSED) != 0 && MainMenu == 0) {
			Board.Running();
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if ((keyState & FIRE_PRESSED) != 0 && MainMenu == 2) {
			MenuLocation = "Instructions";
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if ((keyState & FIRE_PRESSED) != 0 && MainMenu == 3) {
			MenuLocation = "HighScores";
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		} else if ((keyState & FIRE_PRESSED) != 0 && MainMenu == 1) {
			MenuLocation = "Options";
			try {
				  Thread.sleep(100);
				} catch(Exception e) {}
		}
	}

	private void updateMainMenuScreen(Graphics g) {
		int idx;
	
		g.setClip(0,0,GAME_WIDTH,GAME_HEIGHT);
		g.setColor(0xffffff);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
	
		buildGameScreen(g);
		g.drawImage(titleMessageBox, 0, 0, 0);
		
		Sprite[] menuSprite;
		menuSprite = new Sprite[MainMenuCount];
		menuSprite[0] = new Sprite(NewGame,71,19);
		menuSprite[1] = new Sprite(Options,55,19);
		menuSprite[2] = new Sprite(Instructions,75,19);
		menuSprite[3] = new Sprite(HighScores,75,19);
		
		int[] menuX = {50,50,53,50};
		int[] menuY = {75,95,115,135};
		for (idx=0;idx<MainMenuCount;idx++) {
			if (idx == MainMenu) {
				menuSprite[idx].setFrame(0);
			} else {
				menuSprite[idx].setFrame(1);
			}
			menuSprite[idx].setPosition(menuX[idx],menuY[idx]);
			menuSprite[idx].paint(g);
		}
		
		flushGraphics();
	}
	private void checkWinnerInput() {
		int keyState = getKeyStates();
		Player Winner = Board.getWinner();
		if (hs.isHighScore(Winner.getScore()) && HSNameLength >= 1 && Winner.isHuman()) {
			HSState = "Gather";
		} else {
			HSState = "NeedEnter";
		}
		
		if (count==0) {
			AlphabetPos = 0;
			HSName[0] = AlphabetList[AlphabetPos];
			count++;
		}
		if (HSState.equals("Gather")) {
			if (((keyState & RIGHT_PRESSED) != 0 || (keyState & FIRE_PRESSED) != 0) && HSNamePos < (HSNameLength - 1)) {
				HSName[HSNamePos] = AlphabetList[AlphabetPos];
				AlphabetPos = 0;
				HSNamePos++;
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			} else if ((keyState & FIRE_PRESSED) != 0 && HSNamePos == (HSNameLength - 1)) {
				HSName[HSNamePos] = AlphabetList[AlphabetPos];
				HSState = "Finished";
				HSName[HSNamePos] = AlphabetList[AlphabetPos];
				int i;
				for (i=0;i<HSNameLength;i++) {
					HSNameText += HSName[i];
				}
				hs.addHighScore(Winner.getScore(),HSNameText);
				hs = new HighScore();
				count=0;
				Board.FinishHighScore();
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			} else if ((keyState & LEFT_PRESSED) != 0 && HSNamePos > 0) {
				HSName[HSNamePos] = "";
				HSNamePos--;
				AlphabetPos = getIDX(AlphabetList, HSName[HSNamePos]);
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			} else if ((keyState & UP_PRESSED) != 0 && HSNamePos >= 0 && HSNamePos < HSNameLength) {
				AlphabetPos--;
				if (AlphabetPos == -1 ) {
					AlphabetPos = AlphabetList.length - 1;
				}
				HSName[HSNamePos] = AlphabetList[AlphabetPos];
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			} else if ((keyState & DOWN_PRESSED) != 0 && HSNamePos >= 0 && HSNamePos < HSNameLength) {
				AlphabetPos++;
				AlphabetPos = AlphabetPos % AlphabetList.length;
				HSName[HSNamePos] = AlphabetList[AlphabetPos];
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			}
			
		} else {
		
			if ((keyState & FIRE_PRESSED ) != 0 && HSState.equals("NeedEnter")) {
				count=0;
				Board.FinishHighScore();
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			} else if (HSState.equals("Finished")) {
				count=0;
				Board.FinishHighScore();
				try {
					Thread.sleep(100);
				} catch(Exception e) {}
			}
		}

		try {
			Thread.sleep(30);
		} catch(Exception e) {}		
	}
	private void displayWinner(Graphics g) {
		g.setClip(0,0,GAME_WIDTH,GAME_HEIGHT);
		g.setColor(0xffffff);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

		buildGameScreen(g);
		getBoardGraphic(g);
		getScore(g);
		Player Winner = Board.getWinner();
		if (Winner.getPlayerColor().equals("Red")) {
			g.drawImage(redMessageBox, 0, 0, 0);
		} else {
			g.drawImage(blueMessageBox, 0, 0, 0);
		}
		g.setColor(0x000000); 
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
		g.drawString("The Winner Is: ", 88, 60, Graphics.HCENTER|Graphics.BOTTOM);
		if (Winner.getPlayerColor().equals("Red")) {
			g.setColor(0xFF0000);	
		} else {
			g.setColor(0x0000FF);
		}
		g.drawString(Winner.getPlayerColor(), 88, 80, Graphics.HCENTER|Graphics.BOTTOM);
		g.setColor(0x000000); 
		g.drawString("Score: "+Winner.getScore(), 88, 100, Graphics.HCENTER|Graphics.BOTTOM);
		g.drawString("Armies: "+Winner.getArmies(), 88, 120, Graphics.HCENTER|Graphics.BOTTOM);
		
		if (hs.isHighScore(Winner.getScore()) && HSNameLength >= 1 && Winner.isHuman()) {
			g.drawString("Enter Initials:", 88, 140, Graphics.HCENTER|Graphics.BOTTOM);
			Sprite[] AlphaSprite;
			AlphaSprite = new Sprite[HSNamePos+1];
			
			int i;
			for (i=0;i<=HSNamePos;i++) {
			
				AlphaSprite[i] = new Sprite(Alphabet, 15, 16);
				
				AlphaSprite[i].setFrame(getIDX(AlphabetList, HSName[i]));
				AlphaSprite[i].setPosition(70 + (12 * i),145);
				AlphaSprite[i].paint(g);
			}
			
		} else if (Winner.isComputer()) {
			g.drawString("Computer Wins!", 88, 160, Graphics.HCENTER|Graphics.BOTTOM);
		} else {
		}
		
		g.drawString("Press OK", 88, 180, Graphics.HCENTER|Graphics.BOTTOM);
		flushGraphics();
		
	}
	
	private void checkUserInput() {

	  // get the state of keys
		if (Board.Players[Board.CurrentTurn()].isHuman()) {
			int keyState = getKeyStates();
	    
			if (count == 0) {
				Board.initCursor();
				count++;
			}
		
			if((keyState & LEFT_PRESSED) != 0) {
				Board.leftCursor();	
			} else if((keyState & RIGHT_PRESSED) != 0) { 
				Board.rightCursor();
			} else if((keyState & UP_PRESSED) != 0) {
				Board.upCursor();
			} else if((keyState & DOWN_PRESSED) != 0) {
				Board.downCursor();
			} else if ((keyState & FIRE_PRESSED) != 0) {
				if (Board.Play(Board.getCursor())) {
					int i;
					for (i=0;i<Board.getPlayerCount();i++) {
						Board.setScore(Board.Players[i]);
					}
					Board.NextTurn();
					count = 0;
				}
			}

		} else if (Board.Players[Board.CurrentTurn()].isComputer()) {
			Board.Play(Board.Players[Board.CurrentTurn()].getComputerMove(Board));
			Board.NextTurn();
			count = 0;
			try {
				Thread.sleep(1000);
			} catch(Exception e) {}
		} else {
			Board.NextTurn();
			count = 0;
		}
		

	}

	private void updateGameScreen(Graphics g) {

		// the next two lines clear the background
		g.setClip(0,0,GAME_WIDTH,GAME_HEIGHT);
		g.setColor(0xffffff);
		g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
	
	  
		// draws the couple image according to current
		// desired positions
		buildGameScreen(g);
	  
		getBoardGraphic(g);
		getScore(g);
 

		// this call paints off screen buffer to screen
		flushGraphics();
	}
	public void getBoardGraphic(Graphics boardgraphic) {
		int idx;
		Sprite highlightSprite;
		Sprite currentSprite;
		GameSquare[] board = Board.getBoard();
		int BoardSize = board.length;
		

		for (idx=0;idx<BoardSize;idx++) {
			Sprite pieceSprite;
			if (board[idx].getUsed()) {
				
				if (board[idx].getValue() > 0 && board[idx].getValue() <= 20) {
					if(board[idx].getPlayer().getPlayerColor().equals("Red") || board[idx].getPlayer().getPlayerColor().equals("Blue")) {
						if(board[idx].getPlayer().getPlayerColor().equals("Red")  ) {
							pieceSprite = new Sprite( redimage ,16,18);
						} else {
							pieceSprite = new Sprite( blueimage ,16,18);
						}
						pieceSprite.setFrame((board[idx].getValue()-1));
						pieceSprite.setPosition(Board.getXCoord(idx),Board.getYCoord(idx));
						pieceSprite.paint(boardgraphic);
					}
					
				}
			}
		}
		for (idx=0;idx<BoardSize;idx++) {
			if (board[idx].getHighlight()) {
				highlightSprite = new Sprite ( highlightimage, 18,20);
				highlightSprite.setFrame(0);
				highlightSprite.setPosition(Board.getXCoord(idx)-1,Board.getYCoord(idx)-1);
				highlightSprite.paint(boardgraphic);
			}
		}

	    
		
		if ( Board.Players[Board.CurrentTurn()].getPlayerColor().equals("Red")  ) {
			currentSprite = new Sprite( redimage ,16,18);
		} else {
			currentSprite = new Sprite( blueimage ,16,18);
		}
		currentSprite.setFrame(Board.getCurrentValue()-1);
		currentSprite.setPosition(80,200);
		currentSprite.paint(boardgraphic);

	}
	private void getScore (Graphics g) {
		int i;
		for (i=0;i<Board.getPlayerCount();i++) {
			Board.setScore(Board.Players[i]);
		}
		
		g.setFont(Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL));
		if (Board.getCriteria().equals("Score")) {
			g.drawString(java.lang.Integer.toString(Board.Players[0].getScore()), 80, 197, Graphics.RIGHT|Graphics.BOTTOM);
			g.drawString(java.lang.Integer.toString(Board.Players[1].getScore()), 165, 197, Graphics.RIGHT|Graphics.BOTTOM);
		} else {
			g.drawString(java.lang.Integer.toString(Board.Players[0].getArmies()), 80, 197, Graphics.RIGHT|Graphics.BOTTOM);
			g.drawString(java.lang.Integer.toString(Board.Players[1].getArmies()), 165, 197, Graphics.RIGHT|Graphics.BOTTOM);
		}
		
	}
	
	private void buildGameScreen(Graphics g) {
		
		  // set the drawing color to black
		  g.setColor(0x000000);
		  g.drawImage(gameBoard, 0, 0, 0);	
	}
	
}


