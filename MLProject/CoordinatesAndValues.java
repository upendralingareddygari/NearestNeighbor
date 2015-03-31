
/**
 * 
 * @author upendra
 *
 */
public class CoordinatesAndValues {

	private int x1Coordinate;
	private int x2Coordinate;
	private boolean value;
	
	public CoordinatesAndValues(int x1, int x2, boolean value) {
		this.x1Coordinate = x1;
		this.x2Coordinate = x2;
		this.value = value;
	}
	
	public int getX1Coordinate() {
		return x1Coordinate;
	}
	public void setX1Coordinate(int x1Coordinate) {
		this.x1Coordinate = x1Coordinate;
	}
	public int getX2Coordinate() {
		return x2Coordinate;
	}
	public void setX2Coordinate(int x2Coordinate) {
		this.x2Coordinate = x2Coordinate;
	}
	public boolean isValue() {
		return value;
	}
	public void setValue(boolean value) {
		this.value = value;
	}
	
}
