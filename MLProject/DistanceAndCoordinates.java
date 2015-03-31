
/**
 * 
 * @author upendra
 *
 */
public class DistanceAndCoordinates implements Comparable<Object> {
	
	private double distance;
	private int index;
	
	public DistanceAndCoordinates(double distance, int index) {
		this.distance = distance;
		this.index = index;
	}
	
	public double getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int compareTo(Object arg0) {
		DistanceAndCoordinates distanceAndCoordinates = (DistanceAndCoordinates) arg0;
        return (this.distance < distanceAndCoordinates.getDistance()) ? -1: (this.distance > distanceAndCoordinates.getDistance() ) ? 1:0 ;
	}
}
