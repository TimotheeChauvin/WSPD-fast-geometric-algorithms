import java.io.File;
import java.util.Scanner;
import Jcg.geometry.*;

/**
 * Compute the closest and farthest pairs in the point cloud given by the argument
 * filename, with an without a WSPD
 */
public class PointCloudResults {
	Point_3[] points;
	public String filename;

	public PointCloudResults(String filename) throws Exception {
		this.points = readFile(filename);
	}

	public static Point_3[] readFile(String filename) throws Exception {
		File file = new File(filename);
		Scanner sc = new Scanner(file);
		sc.nextLine();  // ignore first line: "OFF"
		int nPoints = sc.nextInt();
		Point_3[] pointArray = new Point_3[nPoints];
		sc.nextInt();  // ignore number of faces
		sc.nextInt();  // ignore number of edges
		for (int i = 0; i < nPoints; i++) {
			pointArray[i] = new Point_3(
				sc.nextDouble(),
				sc.nextDouble(),
				sc.nextDouble());
		}
		sc.close();
		return pointArray;
	}

	public Point_3[] slowClosestPair() {
		SlowClosestPair slow = new SlowClosestPair();
		return slow.findClosestPair(this.points);
	}

	public Point_3[] fastClosestPair() {
		FastClosestPair fast = new FastClosestPair();
		return fast.findClosestPair(this.points);
	}

	public Point_3[] slowDiameter() {
		SlowDiameter slow = new SlowDiameter();
		return slow.findFarthestPair(this.points);
	}

	public Point_3[] fastDiameter(double epsilon) {
		FastDiameter fast = new FastDiameter(epsilon);
		return fast.findFarthestPair(this.points);
	}

	public static void printResults(Point_3[] pair, double time) {
		System.out.println("total time (ms): " + time);
		System.out.println("pair: " + pair[0] + " ; " + pair[1]);
		System.out.println("distance: " + WSPD.distance(pair[0], pair[1]));
		System.out.println();
	}

	public static void main(String args[]) throws Exception {
		
		PointCloudResults pcr = new PointCloudResults(args[0]); // args[0] is the filename
		
		long startTime;
		Point_3[] pair;  // the found closest or farthest pair

		// slowClosest
		System.out.println("SlowClosest");
		startTime = System.currentTimeMillis();
		pair = pcr.slowClosestPair();
		printResults(pair, (System.currentTimeMillis() - startTime));

		// fastClosest
		System.out.println("FastClosest");
		startTime = System.currentTimeMillis();
		pair = pcr.fastClosestPair();
		printResults(pair, (System.currentTimeMillis() - startTime));

		// slowDiameter
		System.out.println("SlowDiameter");
		startTime = System.currentTimeMillis();
		pair = pcr.slowDiameter();
		printResults(pair, (System.currentTimeMillis() - startTime));

		// fastDiameter
		System.out.println("FastDiameter");
		double epsilon = 0.7; // approximation factor
		startTime = System.currentTimeMillis();
		pair = pcr.fastDiameter(epsilon);
		printResults(pair, (System.currentTimeMillis() - startTime));
	}
}
