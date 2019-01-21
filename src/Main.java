import Jcg.geometry.Point_3;
import java.util.*;
public class Main {
    
    


    public static void main(String[] args) {
        Point_3 p1 = new Point_3(1, 2, 3);
        Point_3 p2 = new Point_3(4, 1, 7);
        Point_3 p3 = new Point_3(1, 8, 0);
        Point_3 p4 = new Point_3(9, 2, 2);
        Point_3[] exampleArray = new Point_3[4];
        exampleArray[0] = p1;
        exampleArray[1] = p2;
        exampleArray[2] = p3;
        exampleArray[3] = p4;
        OctreeNode oct = new OctreeNode(new ArrayList<Point_3>());
        System.out.println(oct.findLength(exampleArray));
        System.out.println(oct.findCenter(exampleArray));
    }
}