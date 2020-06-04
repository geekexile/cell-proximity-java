package proximity;

import java.util.Random;
import java.lang.Math;

public class GameBoard {
	private boolean Victory = false;

	private boolean HighScoreStep = false;

	private int Cursor = 0;

	private boolean New = true;

	private int PlayerTurn = 0;

	private int CurrentValue = 0;

	private Random random = new Random();

	private String WinCriteria = "Score";

	private String Difficulty = "Hard";

	public Player[] Players;

	private String[] defaultPlayers = { "Human", "Computer" };

	private static final int MAX_PLAYERS = 2;

	private static final int BOARD_UPPER_X = 14;

	private static final int BOARD_UPPER_Y = 19;

	private static final boolean START_LEFT = false;

	private static final int X_JUMP = 14;

	private static final int Y_JUMP = 13;

	private static final int COLUMN_SIZE = 12;

	private static final int ROW_SIZE = 10;

	private static final int PIECE_SHIFT = 7;

	private static final int BOARD_SIZE = ROW_SIZE * COLUMN_SIZE;

	private static final int MAX_PIECE_SCORE = 20;

	private GameSquare[] board;

	private Player Winner;

	public GameBoard() {
		int idx;
		board = new GameSquare[BOARD_SIZE];

		for (idx = 0; idx < BOARD_SIZE; idx++) {
			board[idx] = new GameSquare();
		}

		Players = new Player[MAX_PLAYERS];

		for (int i = 0; i < Players.length; i++) {
			Players[i] = new Player(defaultPlayers[i], i);
		}

		initRounds();
		CurrentValue = Players[PlayerTurn].getThisPiece();
		Players[PlayerTurn].setNextTurn();

	}

	public boolean isStartLeft() {
		return START_LEFT;
	}

	public int getRowSize() {
		return ROW_SIZE;
	}

	private void initRounds() {
		int[] rounds = new int[board.length / MAX_PLAYERS];
		for (int i = 0; i < (board.length / MAX_PLAYERS); i++) {
			rounds[i] = (Math.abs(random.nextInt()) % MAX_PIECE_SCORE) + 1;
		}
		Toolset t = new Toolset();
		for (int i = 0; i < MAX_PLAYERS; i++) {
			Players[i].setPieces(t.randomSortArray(rounds));
		}
	}

	public int getPlayerCount() {
		return Players.length;
	}

	public int getRandomMove() {
		int move = 0;
		int[] moves = new int[BOARD_SIZE];
		for (int idx = 0; idx < moves.length; idx++) {
			if (isUsed(idx)) {
				moves[idx] = -1;
			} else {
				moves[idx] = idx;
			}
		}
		Toolset t = new Toolset();
		moves = t.randomSortArray(moves);
		for (int i = 0; i < moves.length; i++) {
			if (moves[i] >= 0) {
				return moves[i];
			}
		}
		return move;
	}

	public void FinishHighScore() {
		HighScoreStep = true;
	}

	public boolean isHighScoreFinished() {
		return HighScoreStep;
	}

	public void setPlayer(int i, String s) {
		if (s.equals("Human") || s.equals("Computer")) {
			this.Players[i].setPlayerType(s);
		}
	}

	public void setCriteria(String s) {
		if (s.equals("Armies") || s.equals("Score")) {
			WinCriteria = s;
		}
	}

	public void setDifficulty(String s) {
		if (s.equals("Hard") || s.equals("Medium") || s.equals("Easy")) {
			Difficulty = s;
		}
	}

	public String getDifficulty() {
		return Difficulty;
	}

	public String getCriteria() {
		return WinCriteria;
	}

	public int getCursor() {
		return Cursor;
	}

	public void setCursor(int idx) {
		if (idx >= 0 && idx < board.length) {
			if (Cursor >= 0 && Cursor < board.length) {
				board[Cursor].unHighlight();
			}
			Cursor = idx;
			board[Cursor].Highlight();
		}
	}

	public Player getWinner() {
		// Winner = Players[0];
		return Winner;
	}

	public boolean isUsed(int idx) {
		if (idx >= 0 && idx < board.length) {
			return board[idx].getUsed();
		}
		return true;
	}

	public Player getPlayer(int idx) {
		return board[idx].getPlayer();
	}

	public int getValue(int idx) {
		return board[idx].getValue();
	}

	public void scoreNeighbors(Player p, int idx) {
		int[] shift = { -1, // left
				-1, // right
				-1, // up
				-1, // down
				-1, // upshift
				-1 // downshift
		};

		int Yloc = getYLoc(idx);
		int Xloc = getXLoc(idx);
		int rowshift = 1;

		if (((idx % ROW_SIZE) + 1) > 0) {
			shift[0] = idx - 1;
		}
		if (((idx % ROW_SIZE) + 1) < ROW_SIZE) {
			shift[1] = idx + 1;
		}
		if ((idx - ROW_SIZE) >= 0) {
			shift[2] = idx - ROW_SIZE;
		}
		if ((idx + ROW_SIZE) < board.length) {
			shift[3] = idx + ROW_SIZE;
		}

		if (Yloc % 2 != 0) {
			if (!START_LEFT) {
				rowshift = 1;
			} else {
				rowshift = -1;
			}
			if ((idx + rowshift - ROW_SIZE) >= 0) {
				shift[4] = idx + rowshift - ROW_SIZE;
			}
			if ((idx + rowshift + ROW_SIZE) < board.length) {
				shift[5] = idx + rowshift + ROW_SIZE;
			}
		} else if (Yloc % 2 == 0) {
			if (!START_LEFT) {
				rowshift = -1;
			} else {
				rowshift = 1;
			}
			if ((idx + rowshift - ROW_SIZE) >= 0) {
				shift[4] = idx + rowshift - ROW_SIZE;
			}
			if ((idx + rowshift + ROW_SIZE) < board.length) {
				shift[5] = idx + rowshift + ROW_SIZE;
			}
		}
		int i, s;
		for (i = 0; i <= 5; i++) {
			if (shift[i] >= 0 && Math.abs(Xloc - getXLoc(shift[i])) <= 1) {
				if (board[shift[i]].getUsed()
						&& board[shift[i]].getPlayer().getPlayerNum() == p
								.getPlayerNum()) {
					s = board[shift[i]].getValue();
					if (s < 20) {
						board[shift[i]].setValue(s + 1);
					}
				}
				if (board[shift[i]].getUsed()
						&& board[shift[i]].getPlayer().getPlayerNum() != p
								.getPlayerNum()) {
					s = board[shift[i]].getValue();
					if (s < CurrentValue) {
						board[shift[i]].setPlayer(p);
					}
				}
			}
		}
	}

	public int getCurrentValue() {
		return CurrentValue;
	}

	public void initCursor() {
		int pos = ((ROW_SIZE) * (COLUMN_SIZE / 2)) + ((ROW_SIZE - 1) / 2);
		setCursor(pos);
	}
	public int getPos(int x, int y) {
		int pos = (((y-1) * ROW_SIZE) + (x-1));
		if (pos >= 0 && pos < board.length) {
			return pos;
		} else {
			return -1;
		}
	}

	public void leftCursor() {
		if (Cursor >= 0 && Cursor < board.length) {
			if (((Cursor % ROW_SIZE)) > 0) {
				int pos = (Cursor - 1);
				setCursor(pos);
			}
		} else {
			initCursor();
		}
	}

	public void rightCursor() {
		if (Cursor >= 0 && Cursor < board.length) {
			if (((Cursor % ROW_SIZE) + 1) < ROW_SIZE) {
				int pos = (Cursor + 1);
				setCursor(pos);
			}
		} else {
			initCursor();
		}
	}

	public void upCursor() {
		if (Cursor >= 0 && Cursor <= board.length) {
			if ((Cursor - ROW_SIZE) >= 0) {
				int pos = (Cursor - ROW_SIZE);
				setCursor(pos);
			}
		} else {
			initCursor();
		}
	}

	public void downCursor() {
		if (Cursor >= 0 && Cursor < board.length) {
			if ((Cursor + ROW_SIZE) < board.length) {
				int pos = (Cursor + ROW_SIZE);
				setCursor(pos);
			}
		} else {
			initCursor();
		}
	}

	public boolean isNew() {
		return New;
	}

	public boolean isVictory() {
		int idx;
		boolean test;
		test = true;

		for (idx = 0; idx < board.length; idx++) {
			if (!board[idx].getUsed()) {
				test = false;
			}
		}

		if (test) {
			Player highest;
			highest = Players[0];
			for (idx = 0; idx <= Players.length - 1; idx++) {

				if (WinCriteria.equals("Score")) {
					if (highest.getScore() < Players[idx].getScore()) {
						highest = Players[idx];
					}
				} else if (WinCriteria.equals("Armies")) {
					if (highest.getArmies() < Players[idx].getArmies()) {
						highest = Players[idx];
					}
				}
			}
			Winner = highest;
		}
		Victory = test;
		return Victory;
	}

	public void Running() {
		New = false;
	}

	public void setVictory() {
		Victory = true;
	}

	public void setScore(Player p) {
		int idx;
		p.setScore(0);
		p.setArmies(0);
		for (idx = 0; idx < board.length; idx++) {
			if (board[idx].getUsed()) {
				if (board[idx].getPlayer().getPlayerNum() == p.getPlayerNum()) {
					p.addScore(board[idx].getValue());
					p.addArmies(1);
				}
			}
		}
	}

	public int CurrentTurn() {
		return PlayerTurn;
	}

	public int NextTurn() {
		PlayerTurn = ((PlayerTurn + 1) % MAX_PLAYERS);
		CurrentValue = Players[PlayerTurn].getThisPiece();
		Players[PlayerTurn].setNextTurn();
		return PlayerTurn;
	}

	public int getXLoc(int idx) {
		int Xloc = (idx % ROW_SIZE) + 1;
		return Xloc;
	}

	public int getYLoc(int idx) {
		int Yloc = (idx / ROW_SIZE) + 1;
		return Yloc;
	}

	public int getXCoord(int idx) {
		int Xloc = getXLoc(idx);
		int Yloc = getYLoc(idx);
		int shift;
		shift = 0;

		if (Yloc % 2 != 0 && !START_LEFT) {
			shift = PIECE_SHIFT;
		} else if (Yloc % 2 == 0 && START_LEFT) {
			shift = PIECE_SHIFT;
		}

		Xloc = ((Xloc - 1) * X_JUMP) + BOARD_UPPER_X + shift;
		return Xloc;
	}

	public int getYCoord(int idx) {
		int Yloc = getYLoc(idx);
		Yloc = ((Yloc - 1) * Y_JUMP) + BOARD_UPPER_Y;
		return Yloc;
	}

	public boolean Play(int loc) {
		if (board[loc].getUsed() == false) {
			if (loc >= 0 && loc < board.length) {
				board[loc].Play(Players[PlayerTurn], CurrentValue);
				scoreNeighbors(Players[PlayerTurn], loc);
				return true;
			}
		}
		return false;
	}

	public GameSquare[] getBoard() {
		return board;
	}

}