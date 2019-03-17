import Jcg.geometry.*;

/**
 * Implementation of a quadratic time algorithm for computing a farthest pair of points (giving the diameter),
 * based on exhaustive search.
 *
 * @author Code by Luca Castelli Aleardi (INF421 2018, Ecole Polytechnique)
 *
 */
public class SlowDiameter_3 {
	
	/**
	 * Compute a farthest pair of points realizing the diameter of a set of points in 3D space
	 * 
	 * @param points the set of points
	 * @return a pair of farthest points
	 */
    public Point_3[] findFarthestPair(Point_3[] points) {
		if(points.length<2) throw new Error("Error: too few points");
		
		System.out.print("Computing farthest pair: slow computation...");
		Point_3[] result=new Point_3[] {points[0], points[1]};
		Point_3[] pair;
		double distance=0.;
		for(int i=0;i<points.length;i++) {
			for(int j=i+1;j<points.length;j++) {
				double pairDistance=points[i].distanceFrom(points[j]).doubleValue();
				pair=new Point_3[] {points[i], points[j]};
				if(pairDistance>distance) {
					result=pair;
					distance=pairDistance;
				}
			}
		}
		System.out.println("done");

		return result;			
    }

}