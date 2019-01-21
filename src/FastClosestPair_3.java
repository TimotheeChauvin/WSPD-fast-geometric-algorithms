import Jcg.geometry.*;

/**
 * Implementation of a fast algorithm for computing the closest pair,
 * based on WSP.
 *
 * @author Code by Luca Castelli Aleardi (INF421 2018, Ecole Polytechnique)
 *
 */
public class FastClosestPair_3 implements ClosestPair_3 {
	
	/**
	 * Compute the closest pair of a set of points in 3D space
	 * 
	 * @param points the set of points
	 * @return a pair of closest points
	 */
    public Point_3[] findClosestPair(Point_3[] points) {
		if(points.length<2) throw new Error("Error: too few points");
		
		throw new Error("To be completed");
    }

}