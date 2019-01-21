import Jcg.geometry.*;

/**
 * Implementation of a fast algorithm for computing an approximation of the diameter of a 3D point cloud,
 * based on WSP.
 *
 * @author Code by Luca Castelli Aleardi (INF421 2018, Ecole Polytechnique)
 *
 */
public class FastDiameter_3 implements Diameter_3 {
	
	/** approximation factor (for approximating the diameter) **/
	public double epsilon;
	
	public FastDiameter_3(double epsilon) {
		this.epsilon=epsilon;
	}
	
	/**
	 * Compute a farthest pair of points realizing an (1-epsilon)-approximation of the diameter of a set of points in 3D space
	 * 
	 * @param points the set of points
	 * @return a pair of farthest points
	 */
    public Point_3[] findFarthestPair(Point_3[] points) {
		if(points.length<2) throw new Error("Error: too few points");
		
		throw new Error("To be completed");
    }

}