import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Some tests of our programs (Octree, WSPD, FastClosestPair, FastDiameter),
        // with an approximation of the point cloud presented by Sariel Har-Peled
        // page 41 in
        // http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.110.9927&rep=rep1&type=pdf

        Point_3 p1 = new Point_3(1, 9, 0);
        Point_3 p2 = new Point_3(4, 9, 0);
        Point_3 p3 = new Point_3(4, 7, 0);
        Point_3 p4 = new Point_3(2, 3, 0);
        Point_3 p5 = new Point_3(7, 4, 0);
        Point_3 p6 = new Point_3(6, 9, 0);

        Point_3[] pointsArr = new Point_3[] { p1, p2, p3, p4, p5, p6 };
        Octree oc = new Octree(Arrays.asList(pointsArr));
        oc.root.printThis();
        List<OctreeNode[]> wspd = WSPD.buildWSPD(oc, 0.5);
        System.out.println();
        WSPD.printWSPD(wspd);
        FastClosestPair fcpInstance = new FastClosestPair();
        Point_3[] minPointsPair = fcpInstance.findClosestPair(pointsArr);
        System.out.println(minPointsPair[0]);
        System.out.println(minPointsPair[1]);

        FastDiameter fdInstance = new FastDiameter(0.9999);
        Point_3[] diamPointPair = fdInstance.findFarthestPair(pointsArr);
        System.out.println(diamPointPair[0]);
        System.out.println(diamPointPair[1]);
    }
}