import java.util.*;
import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;

/**
 * A class for representing a node of an Octree
 */
public class OctreeNode {
	public int numberPoints; // number of points within the subcube associated with the OctreeNode
	public OctreeNode father;
	public int quadrant;
	public int level;
	public Point_3 p; // representative
	public Point_3 center;
	public Point_3 barycenter;
	public double L;
	public List<OctreeNode> children;
	public Vector_3 repForce; // used only in FastFR91

	/**
	 * Create the octree for storing an input point cloud
	 * 
	 * @param points the list of Point_3 to be added (defines the constructed
	 *               OctreeNode)
	 */
	public OctreeNode(List<Point_3> points) {
		this.L = findLength(points);
		this.center = findCenter(points);
		this.level = 0;
		this.quadrant = -1; // the original OctreeNode isn't inside any quadrant
		this.children = new LinkedList<OctreeNode>();
		this.father = null;
		this.p = null;
		this.barycenter = null;
		this.repForce = new Vector_3(0, 0, 0);
		this.numberPoints = 0;
		for (Point_3 p : points) {
			this.add(p);
		}
	}

	/**
	 * Build an empty OctreeNode
	 * 
	 * @param level    the level in the tree (root = 0)
	 * @param father   the father of this OctreeNode in the tree
	 * @param center   the Point_3 corresponding to the center of the cube
	 * @param quadrant inside the father cube, which quadrant this OctreeNode is in
	 */
	public OctreeNode(int level, OctreeNode father, double L, Point_3 center, int quadrant) {
		this.L = L;
		this.level = level;
		this.children = new LinkedList<OctreeNode>();
		this.quadrant = quadrant;
		this.father = father;
		this.center = center;
		this.barycenter = null;
		this.repForce = new Vector_3(0, 0, 0);
		this.numberPoints = 1;
	}

	/**
	 * Add a point into the OctreeNode
	 * 
	 * @param point the point to add
	 */
	public void add(Point_3 point) {
		int pointQuadrant = quadrant(point, this.center);

		if (this.p == null) {
			// There isn't any point yet, this is the first point added
			this.p = point;
			this.barycenter = new Point_3(point.x, point.y, point.z);
			this.numberPoints = 1;
			return;
		}

		if (this.children.isEmpty()) { // Lonely point without children
			int pQuadrant = quadrant(this.p, this.center);

			// We'll have to add a new child with our current point whatever happens
			OctreeNode pOcToAdd = new OctreeNode(this.level + 1, this, this.L / 2,
					newCenter(pQuadrant, this.center, this.L), pQuadrant);
			pOcToAdd.p = this.p;
			pOcToAdd.barycenter = new Point_3(this.p.x, this.p.y, this.p.z);
			pOcToAdd.quadrant = pQuadrant;
			this.children.add(pOcToAdd);

			if (pQuadrant != pointQuadrant) { // points are in different quadrants
				OctreeNode pointOcToAdd = new OctreeNode(this.level + 1, this, this.L / 2,
						newCenter(pointQuadrant, this.center, this.L), pointQuadrant);
				pointOcToAdd.p = point;
				pointOcToAdd.quadrant = pointQuadrant;
				pointOcToAdd.barycenter = new Point_3(point.x, point.y, point.z);
				this.children.add(pointOcToAdd);
			}

			else { // points in the same quadrant: we need to recurse in this quadrant
				pOcToAdd.add(point);
			}
		}

		else { // adding to an OctreeNode with children
			boolean quadrantAlreadyThere = false;
			for (OctreeNode child : this.children) {
				if (child.quadrant == pointQuadrant) {
					// Quadrant already occupied: add recursively
					child.add(point);
					quadrantAlreadyThere = true;
				}
			}
			if (quadrantAlreadyThere == false) {
				// The quadrant of our point is free
				OctreeNode pointOcToAdd = new OctreeNode(this.level + 1, this, this.L / 2,
						newCenter(pointQuadrant, this.center, this.L), pointQuadrant);
				pointOcToAdd.p = point;
				pointOcToAdd.barycenter = new Point_3(point.x, point.y, point.z);
				pointOcToAdd.quadrant = pointQuadrant;
				this.children.add(pointOcToAdd);
			}
		}
		this.numberPoints += 1;
		this.barycenter.x = 1. / this.numberPoints * (point.x + (this.numberPoints - 1.) * this.barycenter.x);
		this.barycenter.y = 1. / this.numberPoints * (point.y + (this.numberPoints - 1.) * this.barycenter.y);
		this.barycenter.z = 1. / this.numberPoints * (point.z + (this.numberPoints - 1.) * this.barycenter.z);
	}

	/**
	 * Center of the smaller cube, from its quadrant and the coordinates of the bigger cube
	 * 
	 * @param quadrant index of the smaller cube
	 * @param center   center of the current cube
	 * @param L        length of the current cube
	 * @return Point_3 center
	 */
	public static Point_3 newCenter(int quadrant, Point_3 center, double L) {
		Point_3 newCenter = new Point_3();
		newCenter.x = (quadrant % 8 < 4) ? center.x - L / 4 : center.x + L / 4;
		newCenter.y = (quadrant % 4 < 2) ? center.y - L / 4 : center.y + L / 4;
		newCenter.z = (quadrant % 2 < 1) ? center.z - L / 4 : center.z + L / 4;
		return newCenter;
	}

	/**
	 * Return the integer for the quadrant containing the point, between 0 and 7. It
	 * is the decimal integer corresponding to the binary 3-digit number defined as
	 * follows: (x>center.x)(y>center.y)(z>center.z) (1 if true, 0 if false)
	 * 
	 * @param point  the point whose quadrant we are looking for
	 * @param center the center of the cube
	 * @param L      the length of the cube
	 * @return an int between 0 and 7 representing the quadrant
	 */
	public static int quadrant(Point_3 point, Point_3 center) {
		if (point.x <= center.x && point.y <= center.y && point.z <= center.z)
			return 0; // 000
		if (point.x <= center.x && point.y <= center.y && point.z > center.z)
			return 1; // 001
		if (point.x <= center.x && point.y > center.y && point.z <= center.z)
			return 2; // 010
		if (point.x <= center.x && point.y > center.y && point.z > center.z)
			return 3; // 011
		if (point.x > center.x && point.y <= center.y && point.z <= center.z)
			return 4; // 100
		if (point.x > center.x && point.y <= center.y && point.z > center.z)
			return 5; // 101
		if (point.x > center.x && point.y > center.y && point.z <= center.z)
			return 6; // 110
		if (point.x > center.x && point.y > center.y && point.z > center.z)
			return 7; // 111
		assert false; // we shouldn't reach this point
		return -1;
	}

	/**
	 * Given a list of 3D points, return the lowest and highest x, y, z
	 * 
	 * @param points a list of Point_3
	 * @return a double[2][3] array: {{xmin, ymin, zmin}, {xmax, ymax, zmax}}
	 */
	public static double[][] min_and_max_dim(List<Point_3> points) {
		double[][] min_max_dim = new double[2][3];
		min_max_dim[0][0] = points.get(0).x;
		min_max_dim[0][1] = points.get(0).y;
		min_max_dim[0][2] = points.get(0).z;

		min_max_dim[1][0] = points.get(0).x;
		min_max_dim[1][1] = points.get(0).y;
		min_max_dim[1][2] = points.get(0).z;

		for (Point_3 point : points) {
			if (point.x < min_max_dim[0][0])
				min_max_dim[0][0] = point.x;
			if (point.y < min_max_dim[0][1])
				min_max_dim[0][1] = point.y;
			if (point.z < min_max_dim[0][2])
				min_max_dim[0][2] = point.z;

			if (point.x > min_max_dim[1][0])
				min_max_dim[1][0] = point.x;
			if (point.y > min_max_dim[1][1])
				min_max_dim[1][1] = point.y;
			if (point.z > min_max_dim[1][2])
				min_max_dim[1][2] = point.z;
		}
		return min_max_dim;
	}

	/**
	 * Find the length of the cube containing all our points, defined as the highest
	 * max - min across all 3 dimensions.
	 * 
	 * @param points a list of Point_3
	 * @return (double) the length of our cube
	 */
	public static double findLength(List<Point_3> points) {
		double[] min_dim = min_and_max_dim(points)[0];
		double[] max_dim = min_and_max_dim(points)[1];
		double widthX = max_dim[0] - min_dim[0];
		double widthY = max_dim[1] - min_dim[1];
		double widthZ = max_dim[2] - min_dim[2];
		return (Math.max(Math.max(widthY, widthZ), widthX));
	}

	/**
	 * Return the center of a cube containing all our points, as a Point_3.
	 * Coordinates of this center are the averages for each dimension of the extreme
	 * coordinates.
	 * 
	 * @param points
	 * @return (Point_3) the center of our cube containing all points
	 */
	public static Point_3 findCenter(List<Point_3> points) {
		double[] min_dim = min_and_max_dim(points)[0];
		double[] max_dim = min_and_max_dim(points)[1];
		Point_3 center = new Point_3(
			(max_dim[0] + min_dim[0]) / 2,
			(max_dim[1] + min_dim[1]) / 2,
			(max_dim[2] + min_dim[2]) / 2);
		return center;
	}

	/**
	 * Print all points in this OctreeNode (leaves), in no particular order.
	 */
	public void printPoints() {
		if (this.children.isEmpty()) {
			System.out.print(this.p + ",");
		} else {
			for (OctreeNode child : this.children) {
				child.printPoints();
			}
		}
	}

	/**
	 * Print this OctreeNode recursively
	 */
	public void printThis() {
		if (this.children.isEmpty()) {
			System.out.print(this.p);
		} else { // recursion
			System.out.println("Node[L=" + this.L + "]" +
					"[center=" + this.center + "]" +
					"[representative=" + this.p + "]" +
					"[barycenter=" + this.barycenter + "]" +
					"[npoints=" + this.numberPoints + "]" +
					"(");
			for (OctreeNode child : this.children) {
				child.printThis();
				System.out.println(", ");
			}
			System.out.println(")");
		}
	}
}
