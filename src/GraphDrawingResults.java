import jdg.graph.AdjacencyListGraph;
import jdg.io.GraphReader;
import jdg.io.GraphReader_MTX;
import jdg.layout.Layout;

public class GraphDrawingResults {
	public static Layout layoutFR91, layoutFastFR91;
    public static void main(String args[]) {
        String filename=args[0];
		GraphReader reader=new GraphReader_MTX(); // open networks stores in Matrix Market format (.mtx)
        AdjacencyListGraph g=reader.read(filename); // read input network from file
        AdjacencyListGraph gFast=reader.read(filename); // read input network from file

        // set initial locations at random. Since Layout uses a fixed seed, both graphs will be
        // initialized identically.
        Layout.setRandomPoints(g, 400, 400);
        Layout.setRandomPoints(gFast, 400, 400);

        layoutFR91=new FR91Layout(g, 0, 0); // the sizes don't matter here
	    layoutFastFR91=new FastFR91Layout(gFast, 0, 0);


        layoutFR91.computeLayout();
        layoutFastFR91.computeLayout();
    }


}