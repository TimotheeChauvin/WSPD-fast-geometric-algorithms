
import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import java.util.*;


/**
 * A class for representing an Octree
 * 
 * @author Luca Castelli Aleardi, Ecole Polytechnique
 * @version december 2018
 */
public class Octree {
	public OctreeNode root;
	
	public Octree(List<Point_3> points){
		this.root = new OctreeNode(points);
	}
	
	

}
