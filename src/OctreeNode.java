import java.util.*;
import Jcg.geometry.Point_3;

/**
 * A class for representing a node of an Octree
 * 
 * @author Luca Castelli Aleardi, Ecole Polytechnique
 * @version december 2018
 */

public class OctreeNode {
	public double L;
	public int level;
	public int quadrant;
	public List<OctreeNode> children;
	public OctreeNode father;
	public Point_3 p; // point stored in a leaf
	public Point_3 center;

	/**
	 * Create the octree for storing an input point cloud
	 * @param points the list of Point_3 to be added (defines the constructed OctreeNode)
	 */

	public OctreeNode(List<Point_3> points) {
		this.L = findLength(points);
		this.center = findCenter(points);
		this.level = 0;
		this.quadrant = -1; // original OctreeNode isn't inside any quadrant
		this.children = new LinkedList<OctreeNode>();
		this.father = null;
		this.p = null;
		for (Point_3 p : points) {
			this.add(p);
		}
	}

	/**
	 * Build an empty OctreeNode
	 * @param level
	 * @param father
	 */

	public OctreeNode(int level, OctreeNode father, double L, Point_3 center, int quadrant) {
		this.L = L;
		this.level = level;
		this.children = new LinkedList<OctreeNode>();
		this.quadrant = quadrant;
		this.father = father;
		this.center = center;
	}

	/**
	 * Add a point into the OctreeNode
	 * 
	 * @param point the point to add
	 */

	public void add(Point_3 point) {
		if (this.children.isEmpty()) { // only one level
			if (this.p == null) { // no point yet
				this.p = point;
			}
			else { // only one level, already one point
				int pQuadrant = quadrant(this.p, this.center, this.L);
				int pointQuadrant = quadrant(point, this.center, this.L);
				if (pQuadrant != pointQuadrant) { // points are in different quadrants
					OctreeNode pOcToAdd = new OctreeNode(this.level+1, this, this.L/2,newCenter(pQuadrant, this.center, this.L) ,pQuadrant);
					pOcToAdd.p = this.p;
					pOcToAdd.quadrant = pQuadrant;
					this.children.add(pOcToAdd);

					OctreeNode pointOcToAdd = new OctreeNode(this.level+1, this, this.L/2,newCenter(pointQuadrant, this.center, this.L) ,pointQuadrant) ;
					pointOcToAdd.p = point;
					pointOcToAdd.quadrant = pointQuadrant;
					this.children.add(pointOcToAdd);
				}
				else { // points in the same quadrant: we need to recurse in this quadrant
					OctreeNode rec = new OctreeNode(this.level+1, this, this.L/2,newCenter(pQuadrant, this.center, this.L) ,pQuadrant);
					rec.p = this.p;
					rec.quadrant = pQuadrant;
					this.children.add(rec);
					rec.add(point);
				}
			}
		}
		else { // adding to an OctreeNode with children: add recursively
			int pointQuadrant = quadrant(point, this.center, this.L);
			for (OctreeNode child:this.children) {
				if (child.quadrant == pointQuadrant) {
					child.add(point);
				}
			}
		}
	}
	

	/** 
	 * center of the smaller cube 
	 * @param quadrant index of the smaller cube
	 * @param center center of the current cube
	 * @param L length of the current cube
	 * @return Point_3 center
	 */

	public static Point_3 newCenter(int quadrant, Point_3 center, double L) {
		Point_3 newCenter = new Point_3();
		newCenter.x = (quadrant % 8 < 4) ? center.x-L/4 : center.x+L/4;
		newCenter.y = (quadrant % 4 < 2) ? center.y-L/4 : center.y+L/4;
		newCenter.z = (quadrant % 2 < 1) ? center.z-L/4 : center.z+L/4;
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

	public void printPoints() {
		if (this.children.isEmpty()) {
			System.out.print(this.p + ",");
		}
		else {
			for (OctreeNode child:this.children) {
					child.printPoints();
			}
		}
	}
	

	/**
	 * prints this OctreeNode recursively
	 */
	public void printThis() { 
		if (this.children.isEmpty()) {
			System.out.print(this.p);
		}
		else { // recursion
			System.out.println("Node[L=" + this.L + "][center=" + this.center + "](");
			for (OctreeNode child:this.children) {
				child.printThis();
				System.out.println(", ");
			}
			System.out.println(")");
		}
	}
}
