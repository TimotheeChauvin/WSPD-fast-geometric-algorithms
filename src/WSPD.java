import Jcg.geometry.Point_3;
import java.util.*;

public class WSPD {
    WSPD wspd;

    public WSPD(Octree oc, double s) {
        this.wspd = computeWSPD(oc.root, oc.root, s);
    }

    public static Set<OctreeNode[]> computeWSPD(OctreeNode n1, OctreeNode n2, double s) {
        if (n1.level > n2.level) {
            return computeWSPD(n2, n1, s);
        }
        if (n1.p == null || n2.p == null || 
            (n1.children == null && n2.children == null && n1.p == n2.p)) {
            return null;
        }
        if (areWellSeparated(n1, n2, s)) {
            Set<OctreeNode[]> set = new Set<OctreeNode[]>();
            set.add(new OctreeNode[] {n1, n2});
            return set;
        }

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
            assert (!n1.p.equals(n2.p));
            // 2 distinct points: necessary well separated
            return true;
        } else {
            return (Math.max(n1.L, n2.L) <= s * distance(n1, n2));
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

}