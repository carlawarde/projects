/**
* Tile Game Algorithm
* @author Carla Warde, Vincent Kiely, Mats Rostad, Lena Stolz
* @since 2019-04-12
*/
public class Tile {
	private int row;
	private int col;
	private int value;
	
	Tile(int r, int c, int v) {
		setRow(r);
		setCol(c);
		setValue(v);
	}
	
	public int getRow() {
		return this.row;
	}
	public void setRow(int r) {
		this.row = r;
	}
	
	public int getCol() {
		return this.col;
	}
	public void setCol(int c) {
		this.col = c;
	}
	
	public int getValue() {
		return this.value;
	}
	public void setValue(int v) {
		this.value = v;
	}
	
	public String toString() {
		return getValue() + "";
	}
}