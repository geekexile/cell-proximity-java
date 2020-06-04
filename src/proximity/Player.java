package proximity;

import java.util.Random;

public class Player {
	private String playerType = "";
	
	private int Score = 0;
	
	private int Armies = 0;
	
	private String playerColor = "";
	
	private int PlayerNum = 0;
	
	private Random random = new Random();
	
	private int[] Pieces;
	
	private static int turn = 0;
	
	public Player(String p, int i) {
		if (p.equals("Human") || p.equals("Computer")) {
			playerType = p;
			if (i == 0) {
				playerColor = "Red";
				PlayerNum = i;
			} else {
				playerColor = "Blue";
				PlayerNum = i;
			}
		}
	}
	
	public void setPieces(int[] p) {
		Pieces = p;
	}
	
	public int getThisPiece() {
		if (Pieces.length > 0) {
			return Pieces[turn];
		} else {
			return 0;
		}
	}
	
	public void setNextTurn() {
		if (Pieces.length > 0) {
			turn = (turn + 1) % Pieces.length;
		} else {
			turn = 0;
		}
	}
	
	public boolean isHuman() {
		if (playerType.equals("Human")) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isComputer() {
		if (playerType.equals("Computer")) {
			return true;
		} else {
			return false;
		}
	}
	
	public int getComputerMove(GameBoard board) {
		int move, idx, checkvalue, maxvalue = 0;
		int i;
		int[] idxlist = { -1, -1, -1 };
		int[] valuelist = { 0, 0, 0 };
		int bestidx = -1;
		GameSquare[] bd = board.getBoard();
		move = board.getRandomMove();
		
		for (idx = 0; idx < bd.length; idx++) {
			
			if (!board.isUsed(idx)) {
				
				checkvalue = getSquareValue(board, idx, 0, false);
				
				for (i = 0; i < valuelist.length; i++) {
					if (checkvalue > valuelist[i]) {
						valuelist[i] = checkvalue;
						idxlist[i] = idx;
						break;
					}
				}
				if (checkvalue > maxvalue) {
					maxvalue = checkvalue;
					move = idx;
					bestidx = idx;
				}
			}
		}
		int percentchance = (Math.abs(random.nextInt()) % 100);
		if (board.getDifficulty().equals("Hard")) {
			if (move < 0) {
				move = board.getRandomMove();
			}
		} else if (board.getDifficulty().equals("Medium")) {
			
			move = -1;
			
			if (hasZeroOrMore(idxlist)) {
				if (percentchance > 80) {
					move = this.SelectOneOf(idxlist);
					
				} else {
					move = bestidx;
					if (move < 0) {
						move = board.getRandomMove();
					}
				}
			} else {
				move = board.getRandomMove();
			}
		} else {
			move = -1;
			if (hasZeroOrMore(idxlist)) {
				if (percentchance > 50) {
					move = this.SelectOneOf(idxlist);
				} else {
					move = bestidx;
					if (move < 0) {
						move = board.getRandomMove();
					}
				}
			} else {
				move = board.getRandomMove();
			}
		}
		return move;
	}
	
	public int SelectOneOf(int[] i) {
		Toolset t = new Toolset();
		t.randomSortArray(i);
		for (int idx = 0; idx < i.length; idx++) {
			if (i[idx] >= 0) {
				return i[idx];
			}
		}
		return -1;
	}
	
	public boolean hasZeroOrMore(int[] a) {
		boolean state = false;
		int i;
		for (i = 0; i < a.length; i++) {
			if (a[i] >= 0) {
				state = true;
				break;
			}
		}
		return state;
	}
	
	public int getSquareValue(GameBoard board, int idx, int checkvalue,
			boolean reverse) {
		int value = 0;
		int[] shift = { -1, // left
				-1, // right
				-1, // up
				-1, // down
				-1, // upshift
				-1 // downshift
		};
		
		int Yloc = board.getYLoc(idx);
		int Xloc = board.getXLoc(idx);
		int BoardRowSize = board.getRowSize();
		int rowshift = 1;
		GameSquare[] bd = board.getBoard();
		
		if (((idx % BoardRowSize) + 1) > 0) {
			shift[0] = idx - 1;
		}
		if (((idx % BoardRowSize) + 1) < BoardRowSize) {
			shift[1] = idx + 1;
		}
		if ((idx - BoardRowSize) >= 0) {
			shift[2] = idx - BoardRowSize;
		}
		if ((idx + BoardRowSize) < bd.length) {
			shift[3] = idx + BoardRowSize;
		}
		
		if (Yloc % 2 != 0) {
			if (!board.isStartLeft()) {
				rowshift = 1;
			} else {
				rowshift = -1;
			}
			if ((idx + rowshift - BoardRowSize) >= 0) {
				shift[4] = idx + rowshift - BoardRowSize;
			}
			if ((idx + rowshift + BoardRowSize) < bd.length) {
				shift[5] = idx + rowshift + BoardRowSize;
			}
		} else if (Yloc % 2 == 0) {
			if (!board.isStartLeft()) {
				rowshift = -1;
			} else {
				rowshift = 1;
			}
			if ((idx + rowshift - BoardRowSize) >= 0) {
				shift[4] = idx + rowshift - BoardRowSize;
			}
			if ((idx + rowshift + BoardRowSize) < bd.length) {
				shift[5] = idx + rowshift + BoardRowSize;
			}
		}
		int i, s;
		
		for (i = 0; i <= 5; i++) {
			if (shift[i] >= 0 && Math.abs(Xloc - board.getXLoc(shift[i])) <= 1) {
				if (board.isUsed(shift[i])
						&& board.getPlayer(shift[i]).getPlayerNum() == getPlayerNum()) {
					s = board.getValue(shift[i]);
					if (s < 20) {
						value++;
					}
				}
				if (board.isUsed(shift[i])
						&& board.getPlayer(shift[i]).getPlayerNum() != getPlayerNum()) {
					s = board.getValue(shift[i]);
					if (s < board.getCurrentValue()) {
						value += s;
					}
				}
			}
		}
		return value;
	}
	
	public int getPlayerNum() {
		return PlayerNum;
	}
	
	public String getPlayerColor() {
		return playerColor;
	}
	
	public String getPlayerType() {
		return playerType;
	}
	
	public void setPlayerType(String s) {
		if (s.equals("Human") || s.equals("Computer")) {
			playerType = s;
		}
	}
	
	public int getScore() {
		return Score;
	}
	
	public int setScore(int s) {
		Score = s;
		return s;
	}
	
	public int addScore(int s) {
		Score += s;
		return s;
	}
	
	public int getArmies() {
		return Armies;
	}
	
	public int setArmies(int s) {
		Armies = s;
		return s;
	}
	
	public int addArmies(int s) {
		Armies += s;
		return Armies;
	}
	
}
