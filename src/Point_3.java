/**
 * Encode a point in 3D space
 */

class Point_3 {
    double x;
    double y;
    double z;
    public Point_3() {
    }
    public Point_3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distanceFrom(Point_3 p) {
        return Math.sqrt(
            Math.pow((this.x - p.x), 2)
            + Math.pow((this.y - p.y), 2)
            + Math.pow((this.z - p.z), 2));
    }

    public String toString() {
        return "(" + String.valueOf(this.x) + "," + String.valueOf(this.y) + "," + String.valueOf(this.z) + ")";
    }
}