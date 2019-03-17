import java.io.File;
import java.util.Scanner;

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
		System.out.println(filename);
		File file = new File(filename);
		Scanner sc = new Scanner(file);
		String line;
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
		SlowClosestPair_3 slow = new SlowClosestPair_3();
		return slow.findClosestPair(this.points);
	}

	public Point_3[] fastClosestPair() {
		FastClosestPair_3 fast = new FastClosestPair_3();
		return fast.findClosestPair(this.points);
	}

	public Point_3[] slowDiameter() {
		SlowDiameter_3 slow = new SlowDiameter_3();
		return slow.findFarthestPair(this.points);
	}

	public Point_3[] fastDiameter(double epsilon) {
		FastDiameter_3 fast = new FastDiameter_3(epsilon);
		return fast.findFarthestPair(this.points);
	}

	public static void printResults(String methodName, Point_3[] pair, double time) {
		System.out.println(methodName);
		System.out.println("time (ms): " + time);
		System.out.println("pair: " + pair[0] + " ; " + pair[1]);
		System.out.println("distance: " + WSPD.distance(pair[0], pair[1]));
		System.out.println();
	}

	public static void main(String args[]) throws Exception {
		
		PointCloudResults pcr = new PointCloudResults(args[0]); // args[0] is the filename
		
		long startTime;
		Point_3[] pair;  // the found closest or farthest pair

		// slowClosest
		startTime = System.currentTimeMillis();
		pair = pcr.slowClosestPair();
		printResults("SlowClosest", pair, (System.currentTimeMillis() - startTime));

		// fastClosest
		startTime = System.currentTimeMillis();
		pair = pcr.fastClosestPair();
		printResults("FastClosest", pair, (System.currentTimeMillis() - startTime));

		// slowDiameter
		startTime = System.currentTimeMillis();
		pair = pcr.slowDiameter();
		printResults("SlowDiameter", pair, (System.currentTimeMillis() - startTime));

		// fastDiameter
		double epsilon = 0.7; // approximation factor
		startTime = System.currentTimeMillis();
		pair = pcr.fastDiameter(epsilon);
		printResults("FastDiameter", pair, (System.currentTimeMillis() - startTime));
	}
}
