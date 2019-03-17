/**
 * Implementation of a quadratic time algorithm for computing the closest pair,
 * based on exhaustive search.
 *
 * @author Code by Luca Castelli Aleardi (INF421 2018, Ecole Polytechnique)
 *
 */
public class SlowClosestPair_3 {
	
	/**
	 * Compute in the naive way the closest pair of a set of points in 3D space
	 * 
	 * @param points the set of points
	 * @return a pair of closest points
	 */
    public Point_3[] findClosestPair(Point_3[] points) {
		if(points.length<2) throw new Error("Error: too few points");
		
		System.out.print("Computing closest pair: slow computation...");
		Point_3[] result=new Point_3[] {points[0], points[1]};
		Point_3[] pair;
		double distance=result[0].distanceFrom(result[1]);
		for(int i=0;i<points.length;i++) {
			for(int j=i+1;j<points.length;j++) {
				double pairDistance=points[i].distanceFrom(points[j]);
				pair=new Point_3[] {points[i], points[j]};
				if(pairDistance<distance) {
					result=pair;
					distance=pairDistance;
				}
			}
		}
		System.out.println("done");

		return result;			
    }

}