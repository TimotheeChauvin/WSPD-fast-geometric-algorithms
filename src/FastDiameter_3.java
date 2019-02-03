import Jcg.geometry.*;
import java.util.*;

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
		List<Point_3> pointsList = Arrays.asList(points);
		
		long startTime = System.currentTimeMillis();
		Octree oc = new Octree(pointsList);
		System.out.println("Octree (ms): " + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		List<OctreeNode[]> wspd = WSPD.buildWSPD(oc, 2/this.epsilon);
		System.out.println("WSPD (ms): " + (System.currentTimeMillis() - startTime));
		System.out.print("Number of pairs in WSPD: " + wspd.size() + "; ");
		System.out.println("pairs/n^2 = " + (double) wspd.size()/(points.length*points.length));

		startTime = System.currentTimeMillis();
		double maxPointsPairDistance = 0.;
		Point_3[] maxPointsPair = new Point_3[2];
		for (OctreeNode[] pairArr:wspd) {
			double dist = WSPD.distance(pairArr[0], pairArr[1]);
			if (dist > maxPointsPairDistance) {
				maxPointsPairDistance = dist;
				maxPointsPair[0] = pairArr[0].p;
				maxPointsPair[1] = pairArr[1].p;
			}
		}

		System.out.println("FastDiameter computation (ms): " + (System.currentTimeMillis() - startTime));
		return maxPointsPair;
		
    }

}