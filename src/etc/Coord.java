package etc;

public class Coord {
	public final int x;
	public final int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	// normalize for map display
	public Coord getNormalized() {
		return new Coord(x / 15, y / 15);
	}
}
