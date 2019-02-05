import jdg.graph.AdjacencyListGraph;
import jdg.io.GraphReader;
import jdg.io.GraphReader_MTX;
import jdg.layout.Layout;

public class GraphDrawingResults {
    public Layout layoutFR91, layoutFastFR91;
    GraphReader reader;
    AdjacencyListGraph g;
    AdjacencyListGraph gFast;
    public static int sizeX = 800;
    public static int sizeY = 800;

    public GraphDrawingResults(String filename) {
        this.reader = new GraphReader_MTX(); // open networks stores in Matrix Market format (.mtx)
        this.g = reader.read(filename); // read input network from file
        this.gFast = reader.read(filename); // read input network from file

        // Set initial locations at random. Since Layout uses a fixed seed, both graphs
        // will be initialized identically.
        Layout.setRandomPoints(this.g, 400, 400);
        Layout.setRandomPoints(this.gFast, 400, 400);
        this.layoutFR91 = new FR91Layout(this.g, sizeX, sizeY);
        this.layoutFastFR91 = new FastFR91Layout(this.gFast, sizeX, sizeY);
    }

    public static void main(String args[]) {
        String filename = args[0];
        GraphDrawingResults gdr = new GraphDrawingResults(filename);
        int N = 15; // number of iterations to perform
        for (int i = 0; i < N; i++) {
            gdr.layoutFR91.computeLayout();
            System.out.println();
            gdr.layoutFastFR91.computeLayout();
            System.out.println("\n");
        }
    }
}