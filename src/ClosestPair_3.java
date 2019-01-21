import Jcg.geometry.*;

/**
 * Interface for closest pair algorithms
 *
 * @author Code by Luca Castelli Aleardi (INF421, Ecole Polytechnique)
 * @version dec 2018
 */
public interface ClosestPair_3 {
	
	/**
	 * Compute the closest pair of a set of points in 3D space
	 * 
	 * @param points the set of points
	 * @return the pair of closest points
	 */
    public Point_3[] findClosestPair(Point_3[] points);
       
}