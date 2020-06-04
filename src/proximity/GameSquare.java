package proximity;

public class GameSquare {
	private boolean Highlight = false;
	private Player Player;
	private boolean Used = false;
	private int Value = 0;
		
	public GameSquare() {
	}
	
	public boolean setHighlight (boolean b) {
		Highlight = b;
		return Highlight;
	}
	public boolean getHighlight () {
		return Highlight;
	}
	public Player setPlayer (Player p) {
		Player = p;
		return Player;
	}
	public Player getPlayer () {
		return Player;
	}
	public boolean setUsed (boolean u) {
		Used = u;
		return Used;
	}
	public boolean getUsed () {
		return Used;
	}
	public void unHighlight() {
		Highlight = false;
	}
	public void Highlight() {
		Highlight = true;
	}
	public int setValue(int v) {
		Value = v;
		return Value;
	}
	public int getValue() {
		return Value;
	}
	public boolean Play (Player p, int v) {
		Player = p;
		Value = v;
		Used = true;
		Highlight = false;
		return true;
	}
}
