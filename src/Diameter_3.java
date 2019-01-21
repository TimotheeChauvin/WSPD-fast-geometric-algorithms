import Jcg.geometry.*;

/**
 * Interface for diameter computation
 *
 * @author Code by Luca Castelli Aleardi (INF421, Ecole Polytechnique)
 * @version dec 2018
 */
public interface Diameter_3 {
	
	/**
	 * Compute the farthest pair of points realizing the diameter of a set of points in 3D space
	 * 
	 * @param points the set of points
	 * @return a pair of farthest points
	 */
    public Point_3[] findFarthestPair(Point_3[] points);
       
}