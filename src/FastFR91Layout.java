import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import jdg.graph.AdjacencyListGraph;
import jdg.graph.Node;
import jdg.layout.Layout;
import java.util.*;

/**
 * A class implementing the Fruchterman and Reingold method with fast approximation of repulsive forces
 * using a WSPD
 * 
 * @author Luca Castelli Aleardi, Ecole Polytechnique
 * @version december 2018
 */
public class FastFR91Layout extends Layout {
	// parameters of the algorithm by Fruchterman and Reingold
	public double k; // natural spring length
	public double area; // area of the drawing (width times height)
	public double C; // step
	public double temperature; // initial temperature
	public double minTemperature; // minimal temperature (strictly positive)
	public double coolingConstant; // constant term: the temperature decreases linearly at each iteration
	public double s; // well-separatedness parameter for our WSPD
	
	public int iterationCount=0; // count the number of performed iterations
	
	private double accumulated_time = 0;
	private HashMap<Point_3, Vector_3> hm; // our mapping between points and their associated repulsive forces
	
	/**
	 * Initialize the parameters of the force-directed layout
	 * 
	 *  @param g  input graph to draw
	 *  @param w  width of the drawing area
	 *  @param h  height of the drawing area
	 *  @param C  step length
	 */
	public FastFR91Layout(AdjacencyListGraph g, double w, double h) {
		System.out.print("Initializing force-directed method: fast Fruchterman-Reingold 91...");
		if(g==null) {
			System.out.println("Input graph not defined");
			System.exit(0);
		}
		this.g=g;
		int N=g.sizeVertices();
		
		// set the parameters of the algorithm FR91
		this.C=5.;
		this.w=w;
		this.h=h;
		this.area=w*h;
		this.k=C*Math.sqrt(area/N);
		this.temperature=w/2.; // the temperature is a fraction of the width of the drawing area
		this.minTemperature=0.1;
		this.coolingConstant=0.97;
		this.s = 1; // We tried 3 values of s here: 0.01, 0.1 and 1
		
		System.out.println("done ("+N+" nodes)");
		System.out.println(this.toString());
	}
	
	/**
	 * Compute the (intensity of the) attractive force between two nodes at a given distance
	 * 
	 * @param distance  distance between two nodes
	 */	
	public double attractiveForce(double distance) {
		return (distance*distance)/k;
	}
	
	/**
	 * Compute the (intensity of the) repulsive force between two nodes at a given distance
	 * 
	 * @param distance  distance between two nodes
	 */	
	public double repulsiveForce(double distance) {
		return (k*k)/distance;
	}

	/**
	 * Compute an approximation of the repulsive forces exerted on each point, using a WSPD
	 * @return an array of the repulsive forces exerted on each point
	 */
	private Vector_3[] computeAllRepulsiveForces() {
		this.hm = new HashMap<Point_3, Vector_3>(g.vertices.size(), (float) 1.);
		ArrayList<Point_3> pointsList = new ArrayList<Point_3>();
		for (Node n: this.g.vertices) {
			// Add the point corresponding to this node
			pointsList.add(n.p);
		}
		long startTime = System.currentTimeMillis();
		Octree oc = new Octree(pointsList);
		System.out.println("Octree build time (ms): " + (System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		List<OctreeNode[]> wspd = WSPD.buildWSPD(oc, this.s);
		System.out.println("WSPD build time (ms): " + (System.currentTimeMillis() - startTime));
		System.out.println("Number of pairs in WSPD: " + wspd.size());

		startTime = System.currentTimeMillis();
		for (OctreeNode[] pair: wspd) {
			OctreeNode n1 = pair[0];
			OctreeNode n2 = pair[1];
			double distance = Math.sqrt(
				Math.pow(n1.barycenter.x - n2.barycenter.x, 2) + 
				Math.pow(n1.barycenter.y - n2.barycenter.y, 2) + 
				Math.pow(n1.barycenter.z - n2.barycenter.z, 2));
			// direction: normalized Vector_3 from n1 to n2
			Vector_3 direction = new Vector_3(n1.barycenter, n2.barycenter).multiplyByScalar(1/distance);
			n1.repForce = n1.repForce.sum(direction.multiplyByScalar(- n2.numberPoints * this.repulsiveForce(distance)));
			n2.repForce = n2.repForce.sum(direction.multiplyByScalar(n1.numberPoints * this.repulsiveForce(distance)));
		}
		System.out.println("Loop over WSPD pairs time (ms): " + (System.currentTimeMillis() - startTime));
		this.recTraversal(oc.root);
		
		Vector_3[] repForces = new Vector_3[g.sizeVertices()];
		int count = 0;
		for (Node vertex : g.vertices) {
			repForces[count] = this.hm.get(vertex.p); // get the repulsive force for this point
			count += 1;
		}
		return repForces;
	}

	/**
	 * Recursively traverse our WSPD top-down and for each point, add the repulsive force
	 * exerted on the representative point of its father OctreeNode.
	 * Store the resulting forces (for each leaf) in this.hm.
	 */
	public void recTraversal(OctreeNode n) {
		if (!n.children.isEmpty()) {
			for (OctreeNode child : n.children) {
				child.repForce = child.repForce.sum(n.repForce); // add force to the child
				recTraversal(child);
			}
		}
		else { // leaf
			this.hm.put(n.p, n.repForce);
		}
	}


	/**
	 * Perform one iteration of the Force-Directed algorithm.
	 * Positions of vertices are updated according to their mutual attractive and repulsive forces.
	 * 
	 * Repulsive forces are approximated using the WSPD decomposition
	 */	
	public void computeLayout() {
		System.out.println("Performing iteration (fast FR91): "+this.iterationCount);
		long startTime=System.nanoTime(), endTime; // for evaluating time performances
		
		long partialStartTime = System.currentTimeMillis();
		Vector_3[] tetaRepulsive = computeAllRepulsiveForces();  // compute the displacements due to repulsive forces (for each vertex)
		System.out.println("RepulsiveForces time (ms): " + (System.currentTimeMillis() - partialStartTime));

		partialStartTime = System.currentTimeMillis();
		Vector_3[] tetaAttractive = computeAllAttractiveForces(); // compute the displacements due to attractive forces (for each vertex)
		System.out.println("AttractiveForces time (ms): " + (System.currentTimeMillis() - partialStartTime));

		Vector_3 teta;  
		double norm;
		int i =0;
		
		// second step: compute the total displacements and move all nodes to their new locations
		for (Node u:g.vertices){
			teta=tetaRepulsive[i].sum(tetaAttractive[i]); // compute total displacement
			norm=Math.sqrt((double) teta.squaredLength());  // norm of teta
			if (norm!=0){
				u.setPoint(u.p.sum(teta.multiplyByScalar(Math.min(temperature,norm)/norm))); //modify coordinates of u in accordance with computed forces
			}
			i++;
		}
		
		// evaluate time performances
    	endTime=System.nanoTime();
        double duration=(double)(endTime-startTime)/1000000000.;
        accumulated_time += duration;
        System.out.println("iteration "+this.iterationCount+" done ("+duration+" seconds, accumulated time=" + accumulated_time + ")");

		this.cooling(); // update temperature
		
		this.iterationCount++; // increase counter (to count the number of performed iterations)
	}

	/**
	 * Compute the displacement of vertex 'u', due to the attractive forces of its neighbors
	 * 
	 * @param u  the vertex to which attractive forces are applied
	 * @return 'disp' a 3d vector storing the displacement of vertex 'u'
	 */	
	private Vector_3 computeAttractiveForce(Node v) {
		Vector_3 delta;
		double norm;
		Vector_3 disp = new Vector_3(0,0,0);
		for (Node u : g.getNeighbors(v)){
			delta= new Vector_3(v.p, u.p);  // compute delta=u-v
			norm=Math.sqrt((double) delta.squaredLength());  //compute norm of delta
			if (norm!=0){
				disp=disp.sum(delta.multiplyByScalar(attractiveForce(norm)/norm));  //compute displacement of v due to u
			}
		}
		return disp;
	}

	/**
	 * Compute, for each vertex, the displacement due to attractive forces (between neighboring nodes)
	 * 
	 * @return a vector v[]: v[i] stores the geometric displacement of the i-th node
	 */	
	private Vector_3[] computeAllAttractiveForces() {
		Vector_3[] teta = new Vector_3[g.sizeVertices()];
		int i=0;
		for (Node v : g.vertices){
			teta[i]=computeAttractiveForce(v);
			i++;
		}
		return teta;
	}

	
	/**
	 * Cooling system: the temperature decreases exponentially at each iteration
	 * 
	 * Remark: the temperature is assumed to remain strictly positive (>=minTemperature)
	 */	
	protected void cooling() {
		this.temperature=Math.max(this.temperature*coolingConstant, minTemperature);
		//this.temperature=Math.max(this.temperature-coolingConstant, minTemperature); // variant
	}
	
	public String toString() {
		String result="fast implementation of the force-directed algorihm: Fruchterman Reingold\n";
		result=result+"\t area= "+w+" x "+h+"\n";
		result=result+"\t k= "+this.k+"\n";
		result=result+"\t C= "+this.C+"\n";
		result=result+"\t initial temperature= "+this.temperature+"\n";
		result=result+"\t minimal temperature= "+this.minTemperature+"\n";
		result=result+"\t cooling constant= "+this.coolingConstant+"\n";
		
		return result;
	}

}
