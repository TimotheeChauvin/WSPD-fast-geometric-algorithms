import Jcg.geometry.Point_3;
import java.util.*;
public class Main {
    
    


    public static void main(String[] args) {
        Point_3 p1 = new Point_3(1, 2, 3);
        Point_3 p2 = new Point_3(4, 1, 7);
        Point_3 p3 = new Point_3(1, 8, 0);
        Point_3 p4 = new Point_3(9, 2, 2);
        Point_3 p5 = new Point_3(9, 3, 2);
        Point_3 p6 = new Point_3(9, 3, 3);
        Point_3 p7 = new Point_3(1.5, 2, 3);
        List<Point_3> points = new ArrayList<Point_3>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        points.add(p4);
        points.add(p5);
        points.add(p6);
        points.add(p7);

        //System.out.println(OctreeNode.findCenter(points));
        //System.out.println(OctreeNode.findLength(points));
        OctreeNode oc = new OctreeNode(points);

        OctreeNode n1 = oc.children[0].children[1];
        OctreeNode n2 = oc.children[4].children[5];
        n1.printThis();
        n2.printThis();

        System.out.println();
        System.out.println(WSPD.distance(n1, n2));
        System.out.println();

        System.out.println(n1.L);
        System.out.println(n2.L);



        

    
    }
}