import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import java.util.*;

/**
 * A class for representing an Octree
 */
public class Octree {
	public OctreeNode root;

	public Octree(List<Point_3> points) {
		this.root = new OctreeNode(points);
	}
}