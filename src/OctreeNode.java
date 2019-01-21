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
	public OctreeNode[] children=null;
	public OctreeNode father;
	public Point_3 p; //point stored in a leaf

	/**
	 * Create the octree for storing an input point cloud
	 */
	public OctreeNode(List<Point_3> points) {
		double L = findLength(points);
		Point_3 center = findCenter(points);
		this.level = 0;
		this.children = null;
		this.father = null;
		this.p = null;
		for (Point_3 p : points){
			this.add(p, L, center);
		}
	}
	
	/**
	 * Add a node into the OctreeNode
	 */
	public void add(Point_3 point, double L, Point_3 center) {
		if (this.children == null){
			if (this.p == null){
				this.p = point;
			}
			else{
				this.children = new OctreeNode[8];
				for (int i = 0; i<8; i++){
					this.children[i].level = this.level +1;
				}
			}
		}
	}

	public static double[][] min_and_max_dim(List<Point_3> points){
		double[][] min_max_dim = new double[2][3];
		min_max_dim[0][0] = points.get(0).x;
		min_max_dim[0][1] = points.get(0).y;
		min_max_dim[0][2] = points.get(0).z;
		
		min_max_dim[1][0] = points.get(0).x;
		min_max_dim[1][1] = points.get(0).y;
		min_max_dim[1][2] = points.get(0).z;
		for (Point_3 point : points){
			if (point.x < min_max_dim[0][0]){
				min_max_dim[1][0] = point.x;
			}
			if (point.y < min_max_dim[0][1]){
				min_max_dim[1][1] = point.y;
			}
			if (point.z < min_max_dim[0][2]){
				min_max_dim[1][2] = point.z;
			}

			if (point.x > min_max_dim[1][0]){
				min_max_dim[1][0] = point.x;
			}
			if (point.y > min_max_dim[1][1]){
				min_max_dim[1][1] = point.y;
			}
			if (point.z > min_max_dim[1][2]){
				min_max_dim[1][2] = point.z;
			}
		}
        return min_max_dim;
    }

    public static double findLength(List<Point_3> points){
		double[] min_dim  = min_and_max_dim(points)[0];
		double[] max_dim  = min_and_max_dim(points)[1];
        double widthX = max_dim[0] - min_dim[0];
		double widthY = max_dim[1] - min_dim[1];
		double widthZ = max_dim[2] - min_dim[2];
        return(Math.max(Math.max(widthY, widthZ), widthX));
    }
    
    public static Point_3 findCenter(List<Point_3> points){
		double[] min_dim  = min_and_max_dim(points)[0];
		double[] max_dim  = min_and_max_dim(points)[1];
        Point_3 center = new Point_3((max_dim[0] + min_dim[0])/2, (max_dim[1] + min_dim[1])/2, (max_dim[2] + min_dim[2])/2);
        return center;
    }
		
	
}
