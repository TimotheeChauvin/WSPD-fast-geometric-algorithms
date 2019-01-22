import Jcg.geometry.Point_3;
import java.util.*;
public class Main {
    
    


    public static void main(String[] args) {
        Point_3 p1 = new Point_3(1, 2, 3);
        Point_3 p2 = new Point_3(4, 1, 7);
        Point_3 p3 = new Point_3(1, 8, 0);
        Point_3 p4 = new Point_3(9, 2, 2);
        List<Point_3> points = new ArrayList<Point_3>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        System.out.println(OctreeNode.findCenter(points));
        System.out.println(OctreeNode.findLength(points));
        OctreeNode oc = new OctreeNode(points);
        oc.printThis();

    
    }
}