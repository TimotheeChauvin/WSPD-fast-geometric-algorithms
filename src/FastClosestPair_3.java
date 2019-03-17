import Jcg.geometry.*;
import java.util.*;

/**
 * Implementation of a fast algorithm for computing the closest pair, based on
 * WSP.
 */
public class FastClosestPair_3 {

	/**
	 * Compute the closest pair of a set of points in 3D space, using a WSPD
	 * 
	 * @param points the List of points
	 * @return a pair of closest points
	 */
	public Point_3[] findClosestPair(Point_3[] points) {
		if (points.length < 2)
			throw new Error("Error: too few points");
		List<Point_3> pointsList = Arrays.asList(points);

		long startTime = System.currentTimeMillis();
		Octree oc = new Octree(pointsList);
		System.out.println("Octree (ms): " + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		List<OctreeNode[]> wspd = WSPD.buildWSPD(oc, 1.001); // we need s > 1 and s small
		System.out.println("WSPD (ms): " + (System.currentTimeMillis() - startTime));
		System.out.println("Number of pairs in WSPD: " + wspd.size() + "; ");

		startTime = System.currentTimeMillis();

		double minPointsPairDistance;
		Point_3[] minPointsPair = new Point_3[2]; // the pair of closest points we will return

		// Initialize minimum with the first pair:
		OctreeNode[] firstPairArray = wspd.get(0);
		Point_3 p1 = firstPairArray[0].p; // representative of the first point set in the pair
		Point_3 p2 = firstPairArray[1].p; // representative of the second point set in the pair
		double distance_p1_p2 = WSPD.distance(p1, p2);
		minPointsPairDistance = distance_p1_p2;
		minPointsPair[0] = p1;
		minPointsPair[1] = p2;

		// Loop over the wspd pairs and update the minimum distance and closest pair
		for (OctreeNode[] pairArray : wspd) {
			// arePoints: whether or not the OctreeNodes are lonely points - they can't be empty
			boolean arePoints = (pairArray[0].children.isEmpty() && pairArray[1].children.isEmpty());
			p1 = pairArray[0].p; // representative of the first point set in the pair
			p2 = pairArray[1].p; // representative of the second point set in the pair
			distance_p1_p2 = WSPD.distance(p1, p2);
			if (arePoints && distance_p1_p2 < minPointsPairDistance) {
				minPointsPairDistance = distance_p1_p2;
				minPointsPair[0] = p1;
				minPointsPair[1] = p2;
			}
		}
		System.out.println("FastClosestPair computation (ms): " + (System.currentTimeMillis() - startTime));
		return minPointsPair;
	}
}