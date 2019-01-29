import Jcg.geometry.Point_3;
import java.util.*;
public class Main {
    
    


    public static void main(String[] args) {
        /*Point_3 p1 = new Point_3(1, 2, 3);
        Point_3 p2 = new Point_3(4, 1, 7);
        Point_3 p3 = new Point_3(1, 8, 0);
        Point_3 p4 = new Point_3(9, 2, 2);
        Point_3 p5 = new Point_3(9, 3, 2);
        Point_3 p6 = new Point_3(9, 3, 3);
        Point_3 p7 = new Point_3(1.5, 2, 3);

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
        System.out.println(n2.L);*/

        /*Point_3 p1 = new Point_3(9, 9, 0);
        Point_3 p2 = new Point_3(9, 10, 0);
        Point_3 p3 = new Point_3(10, 9, 0);
        Point_3 p4 = new Point_3(10, 10, 0);
        Point_3 p5 = new Point_3(-9, -9, 0);
        Point_3 p6 = new Point_3(-9, -10, 0);
        Point_3 p7 = new Point_3(-10, -9, 0);
        Point_3 p8 = new Point_3(-10, -10, 0);*/


        Point_3 p1 = new Point_3(1, 9, 0);
        Point_3 p2 = new Point_3(4, 9, 0);
        Point_3 p3 = new Point_3(4, 8, 0);
        Point_3 p4 = new Point_3(2, 3, 0);
        Point_3 p5 = new Point_3(7, 4, 0);
        Point_3 p6 = new Point_3(6, 9, 0);





        List<Point_3> points1 = new ArrayList<Point_3>();
        points1.add(p1);
        points1.add(p2);
        points1.add(p3);
        points1.add(p4);
        /*List<Point_3> points2 = new ArrayList<Point_3>();
        points2.add(p5);
        points2.add(p6);
        points2.add(p7);
        points2.add(p8);
        OctreeNode n1 = new OctreeNode(points1);
        OctreeNode n2 = new OctreeNode(points2);
        Set<OctreeNode[]> wspdPartial = WSPD.WSPDrec(n1, n2, 1.);
        WSPD.printWSPD(wspdPartial);*/


        points1.add(p5);
        points1.add(p6);
        //points1.add(p7);
        //points1.add(p8);
        Octree oc = new Octree(points1);
        oc.root.printThis();
        Set<OctreeNode[]> wspd = WSPD.buildWSPD(oc, 0.5);
        WSPD.printWSPD(wspd);

    }
}