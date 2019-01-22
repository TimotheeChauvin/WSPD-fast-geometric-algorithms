import java.util.List;

import Jcg.geometry.Point_3;

/**
 * A class for representing a node of an Octree
 * 
 * @author Luca Castelli Aleardi, Ecole Polytechnique
 * @version december 2018
 */

public class OctreeNode {
	public int level;
	public OctreeNode[] children = null;
	public OctreeNode father;
	public Point_3 p; // point stored in a leaf

	/**
	 * Create the octree for storing an input point cloud
	 * @param points the list of Point_3 to be added (defines the constructed OctreeNode)
	 */

	public OctreeNode(List<Point_3> points) {
		double L = findLength(points);
		Point_3 center = findCenter(points);
		this.level = 0;
		this.children = null;
		this.father = null;
		this.p = null;
		for (Point_3 p : points) {
			this.add(p, L, center);
		}
	}

	public OctreeNode(int level, OctreeNode father) {
		this.level = level;
		this.children = null;
		this.father = father;
	}

	/**
	 * Add a point into the OctreeNode
	 * 
	 * @param point the point to add
	 * @param L the length of the current considered cube
	 * @param center the center of the current considered cube
	 */

	public void add(Point_3 point, double L, Point_3 center) {
		if (this.children == null) { // only one level
			if (this.p == null) { // no point yet
				this.p = point;
			}
			else { // only one level, already one point
				this.children = new OctreeNode[8];
				for (int i = 0; i < 8; i++) {
					this.children[i] = OctreeNode(this.level+1, this);
				}
				if (quadrant(this.p) != quadrant(point)) { // points are in different quadrants
					this.children[quadrant(this.p)].p = p;
					this.children[quadrant(point)].p = point;
					this.p = null;
				}
				else { // points in the same quadrant: we need to recurse
					this.children[quadrant(this.p)].p = p;
					this.p = null;
					// Now we consider the new smaller cube defined by quadrant(this.p)
					double newL = L/2;
					Point_3 newCenter;
					if (quadrant)

				}
			}
		}
	}

	/**
	 * Return the center of the new smaller cube containing the considered point
	 * 
	 * @param point the considered point
	 * @param center current center of the big cube
	 * @param L current length of the big cube
	 * @return (Point_3) the new center of the smaller cube
	 */

	public static Point_3 newCenter(Point_3 point, Point_3 center, double L) {
		Point_3 newCenter;
		newCenter.x = (point.x <= center.x) ? center.x-L/4 : center.x+L/4;
		newCenter.y = (point.y <= center.y) ? center.y-L/4 : center.y+L/4;
		newCenter.z = (point.z <= center.z) ? center.z-L/4 : center.z+L/4;
		return newCenter;
	}


	/**
	 * Return the integer for the quadrant containing the point, between 0 and 7.
	 * It is the decimal integer corresponding to the binary 3-digits number defined as
	 * follows: (x>center.x)(y>center.y)(z>center.z) (1 if true, 0 if false)
	 * 
	 * @param point the point whose quadrant we are looking for
	 * @param center the center of the cube
	 * @param L the length of the cube
	 * @return an int between 0 and 7 representing the quadrant
	 */

	public static int quadrant(Point_3 point, Point_3 center, double L) {
		if (point.x <= center.x && point.y <= center.y && point.z <= center.z) {
			return 0; // 000
		}
		if (point.x <= center.x && point.y <= center.y && point.z > center.z) {
			return 1; // 001
		}
		if (point.x <= center.x && point.y > center.y && point.z <= center.z) {
			return 2; // 010
		}
		if (point.x <= center.x && point.y > center.y && point.z > center.z) {
			return 3; // 011
		}
		if (point.x > center.x && point.y <= center.y && point.z <= center.z) {
			return 4; // 100
		}
		if (point.x > center.x && point.y <= center.y && point.z > center.z) {
			return 5; // 101
		}
		if (point.x > center.x && point.y > center.y && point.z <= center.z) {
			return 6; // 110
		}
		if (point.x > center.x && point.y > center.y && point.z > center.z) {
			return 7; // 111
		}
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
			if (point.x < min_max_dim[0][0]) {
				min_max_dim[0][0] = point.x;
			}
			if (point.y < min_max_dim[0][1]) {
				min_max_dim[0][1] = point.y;
			}
			if (point.z < min_max_dim[0][2]) {
				min_max_dim[0][2] = point.z;
			}

			if (point.x > min_max_dim[1][0]) {
				min_max_dim[1][0] = point.x;
			}
			if (point.y > min_max_dim[1][1]) {
				min_max_dim[1][1] = point.y;
			}
			if (point.z > min_max_dim[1][2]) {
				min_max_dim[1][2] = point.z;
			}
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
		Point_3 center = new Point_3((max_dim[0] + min_dim[0]) / 2, (max_dim[1] + min_dim[1]) / 2,
				(max_dim[2] + min_dim[2]) / 2);
		return center;
	}

}
