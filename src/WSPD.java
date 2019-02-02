import Jcg.geometry.Point_3;
import java.util.*;

public class WSPD {
    /**
     * build a complete WSPD with parameter s from an Octree
     * @param oc octree
     * @param s WSPD parameter
     * @return set of arrays of 2 OctreeNodes corresponding to well-separated point sets
     */

    public static Set<Set<OctreeNode>> buildWSPD(Octree oc, double s) {
        return WSPDrec(oc.root, oc.root, s);
    }

    /**
     * build a partial WSPD with parameter s from two OctreeNodes
     * @param n1 first OctreeNode
     * @param n2 second OctreeNode
     * @param s WSPD parameter
     * @return set of arrays of 2 OctreeNodes corresponding to well-separated point sets
     */

    public static Set<Set<OctreeNode>> WSPDrec(OctreeNode n1, OctreeNode n2, double s) {
        System.out.println();
        double L1 = n1.children == null ? 0 : n1.L;
        double L2 = n2.children == null ? 0 : n2.L;
        System.out.println("n1 representative = " + n1.p);
        System.out.println("n1 length = " + L1);
        System.out.println("n2 representative = " + n2.p);
        System.out.println("n2 length = " + L2);
        
        if (n1.p == null || n2.p == null) {
            System.out.println("one is empty");
            // One of the OctreeNodes is empty
            return null;
        }

        if (areWellSeparated(n1, n2, s)) {
            System.out.println("well separated pair");
            Set<Set<OctreeNode>> set = new HashSet<Set<OctreeNode>>();
            Set<OctreeNode> pairSet = new HashSet<OctreeNode>();
            pairSet.add(n1);
            pairSet.add(n2);
            set.add(pairSet);
            for (OctreeNode oc : pairSet) {
                System.out.println(oc.p);
            }
            return set;
        }

        if (L1 < L2) {
            System.out.println("swap");
            // Make sure n1 is always the bigger cube
            return WSPDrec(n2, n1, s);
        }
        
        if (n1.children == null && n2.children == null && (n1.p).equals(n2.p)) {
            System.out.println("same leaf");
            // if n1 and n2 are the same leaf
            return null;
        }
        
        Set<Set<OctreeNode>> set = new HashSet<Set<OctreeNode>>();
        System.out.println("decompose");
        for (OctreeNode c:n1.children){
            Set<Set<OctreeNode>> toAdd = WSPDrec(c, n2, s);
            if (toAdd != null) {
                set.addAll(toAdd);
            }
        }
        return set;
    }

    /**
     * Return whether the 2 OctreeNodes are well-separated with parameter s. If each
     * OctreeNode stands for 1 point, both points being distinct, then they are
     * well-separated.
     * 
     * @param n1
     * @param n2
     * @param s  // well-separatedness parameter
     * @return // boolean
     */


    public static boolean areWellSeparated(OctreeNode n1, OctreeNode n2, double s) {
        if (n1.children == null && n2.children == null) {
            assert (!n1.p.equals(n2.p)); // n1 and n2 are the same leaf: handled in WSPDrec
            // 2 distinct points: necessary well separated
            return true;
        }
        if (n1.children == null && n2.children != null){
            double dist = distance(n1.p, n2);
            return (s * n2.L<= dist); 
        }
        if (n2.children == null && n1.children != null){
            double dist = distance(n2.p, n1);
            return (s * n1.L <= dist); 
        }
        
        else {
            return (s * Math.max(n1.L, n2.L) <= distance(n1, n2));
        }
    }

    /**
     * Return the distance between cubes defined by OctreeNodes n1 and n2
     * 
     * @param n1
     * @param n2
     * @return the smallest distance between points of the cubes
     */

    public static double distance(OctreeNode n1, OctreeNode n2) {
        double x1m = n1.center.x - n1.L / 2; // x min
        double x1M = n1.center.x + n1.L / 2; // x max

        double y1m = n1.center.y - n1.L / 2;
        double y1M = n1.center.y + n1.L / 2;

        double z1m = n1.center.z - n1.L / 2;
        double z1M = n1.center.z + n1.L / 2;

        double x2m = n2.center.x - n2.L / 2;
        double x2M = n2.center.x + n2.L / 2;

        double y2m = n2.center.y - n2.L / 2;
        double y2M = n2.center.y + n2.L / 2;

        double z2m = n2.center.z - n2.L / 2;
        double z2M = n2.center.z + n2.L / 2;

        double diffx;
        double diffy;
        double diffz;

        if (x1M < x2m) { // n1 left of n2
            diffx = x2m - x1M;
        } else if (x1m > x2M) { // n1 right of n2
            diffx = x2M - x1m;
        } else { // there is a value of x interpolating both cubes
            diffx = 0;
        }

        if (y1M < y2m) { // n1 in front of n2
            diffy = y2m - y1M;
        } else if (y1m > y2M) { // n1 behind of n2
            diffy = y2M - y1m;
        } else { // there is a value of y interpolating both cubes
            diffy = 0;
        }

        if (z1M < z2m) { // n1 under of n2
            diffz = z2m - z1M;
        } else if (z1m > z2M) { // n1 above of n2
            diffz = z2M - z1m;
        } else { // there is a value of z interpolating both cubes
            diffz = 0;
        }

        return Math.sqrt(diffx * diffx + diffy * diffy + diffz * diffz);
    }
    /**
     * Returns the distance between a point and the square defined by an OctreeNode
     * @param point in class Point_3
     * @param oc OctreeNode
     * @return the distance as a double
     */

    public static double distance(Point_3 point, OctreeNode oc) {
        double x1m = oc.center.x - oc.L / 2; // x min
        double x1M = oc.center.x + oc.L / 2; // x max

        double y1m = oc.center.y - oc.L / 2;
        double y1M = oc.center.y + oc.L / 2;

        double z1m = oc.center.z - oc.L / 2;
        double z1M = oc.center.z + oc.L / 2;

        double diffx;
        double diffy;
        double diffz;

        if (x1M < point.x) { // oc left of point
            diffx = point.x - x1M;
        } else if (x1m > point.x) { // oc right of point
            diffx = point.x - x1m;
        } else { // there is a value of x interpolating both cubes
            diffx = 0;
        }

        if (y1M < point.y) { // oc in front of point
            diffy = point.y - y1M;
        } else if (y1m > point.y) { // oc behind of point
            diffy = point.y - y1m;
        } else { // there is a value of y interpolating both cubes
            diffy = 0;
        }

        if (z1M < point.z) { // oc under of point
            diffz = point.z - z1M;
        } else if (z1m > point.z) { // oc above of point
            diffz = point.z - z1m;
        } else { // there is a value of z interpolating both cubes
            diffz = 0;
        }

        return Math.sqrt(diffx * diffx + diffy * diffy + diffz * diffz);
    }
    

    public static void printWSPD(Set<Set<OctreeNode>> wspd){
        for (Set<OctreeNode> pair: wspd){
            System.out.println("Pair:");
            for (OctreeNode oc : pair) {
                oc.printPoints();
                System.out.println();
            }
        }
    }
}