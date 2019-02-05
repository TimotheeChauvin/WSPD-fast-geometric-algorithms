import processing.core.PApplet;
import jdg.graph.AdjacencyListGraph;
import jdg.io.GraphReader;
import jdg.io.GraphReader_MTX;
import jdg.layout.Layout;

/**
 * A program for computing network layouts with the "spring embedder" paradigm
 * 
 * This program requires one parameter: the input network, stored in Matrix Market format (.mtx)
 *
 * @author Luca Castelli Aleardi (Ecole Polytechnique, INF421, 2018)
 */
public class NetworkLayout extends DrawGraph {
	
	/**
	 * For running the PApplet as Java application
	 */
	public static void main(String args[]) {
		System.out.println("PI INF421 (2018)");
		if(args.length==0 || args.length>1) {
			System.out.println("Error: wrong arguments, one parameter required");
			System.out.println("Usage example:  java -jar NetworkLayout networks/network.mtx");

			System.exit(0);
		}
		if(args[0].endsWith(".mtx")==false) {
			System.out.println("Error: wrong input format (MTX format supported)");
			System.exit(0);
		}
		
		String filename=args[0];
		GraphReader reader=new GraphReader_MTX(); // open networks stores in Matrix Market format (.mtx)
		AdjacencyListGraph g=reader.read(filename); // read input network from file
		Layout.setRandomPoints(g, 400, 400); // set initial locations at random
		
		DrawGraph.inputGraph=g; // set the input network

		PApplet.main(new String[] { "DrawGraph" }); // start the Processing viewer
	}

}
