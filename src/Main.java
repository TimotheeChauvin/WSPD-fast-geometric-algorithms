import Jcg.geometry.Point_3;
import java.util.*;
public class Main {
    
    


    public static void main(String[] args) {
        Point_3 p1 = new Point_3(1, 9, 0);
        Point_3 p2 = new Point_3(4, 9, 0);
        Point_3 p3 = new Point_3(4, 8, 0);
        Point_3 p4 = new Point_3(2, 3, 0);
        Point_3 p5 = new Point_3(7, 4, 0);
        Point_3 p6 = new Point_3(6, 9, 0);

        Point_3[] pointsArr = new Point_3[] {p1, p2, p3, p4, p5, p6};

        // FastClosestPair_3 fcpInstance = new FastClosestPair_3();
        // Point_3[] minPointsPair = fcpInstance.findClosestPair(pointsArr);
        // System.out.println(minPointsPair[0]);
        // System.out.println(minPointsPair[1]);

        // FastDiameter_3 fdInstance = new FastDiameter_3(0.9999);
        // Point_3[] diamPointPair = fdInstance.findFarthestPair(pointsArr);
        // System.out.println(diamPointPair[0]);
        // System.out.println(diamPointPair[1]);

        

    }
}