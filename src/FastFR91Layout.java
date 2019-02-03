

import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;
import jdg.graph.AdjacencyListGraph;
import jdg.graph.Node;
import jdg.layout.Layout;

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
	public boolean useCooling; // say whether performing simulated annealing
	public double s; // well-separatedness parameter for our WSPD
	
	public int iterationCount=0; // count the number of performed iterations
	private int countRepulsive=0; // count the number of computed repulsive forces (to measure time performances)
	
	private double accumulated_time = 0;
	
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
		this.C=1.;
		this.w=w;
		this.h=h;
		this.area=w*h/100;
		this.k=C*Math.sqrt(area/N);
		this.temperature=w/5.; // the temperature is a fraction of the width of the drawing area
		this.minTemperature=0.05;
		this.coolingConstant=0.98;
		this.s = 1; // TODO decide which value to use
		
		System.out.println("done ("+N+" nodes)");
		//System.out.println("k="+k+" - temperature="+temperature);
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
		countRepulsive++;
		return (k*k)/distance;
	}
	
	/**
	 * Compute the displacement of vertex 'u', due to the attractive forces of its neighbors
	 * 
	 * @param u  the vertex to which attractive forces are applied
	 * @return 'disp' a 3d vector storing the displacement of vertex 'u'
	 */	
	private Vector_3 computeAttractiveForce(Node u) {
		Vector_3 attractive_force = new Vector_3(0,0,0);
		
		for(Node temp : u.neighbors)
		{
			double distance = u.p.distanceFrom(temp.p).doubleValue();
			double aF = attractiveForce(distance);
			attractive_force.x += (temp.p.x - u.p.x) / distance * aF;
			attractive_force.y += (temp.p.y - u.p.y) / distance * aF; 
			attractive_force.z += (temp.p.z - u.p.z) / distance * aF;
		}
		return attractive_force;		
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



	private Vector_3[] computeAllRepulsiveForces() {
		// TODO
		ArrayList<Point_3> pointsList;
		for (Node n: this.g.vertices) {
			// Node contains a "p" (point) field
			pointsList.add(n.p);
		}

		Octree oc = new Octree(pointsList);
		List<OctreeNode[]> wspd = WSPD.buildWSPD(oc, this.s);
		for (OctreeNode[] pair: wspd) {
			OctreeNode n1 = pair[0];
			OctreeNode n2 = pair[1];
			
		}
	}


	/**
	 * Perform one iteration of the Force-Directed algorithm.
	 * Positions of vertices are updated according to their mutual attractive and repulsive forces.
	 * 
	 * Repulsive forces are approximated using the WSPD decomposition
	 */	
	public void computeLayout() {
		System.out.print("Performing iteration (fast FR91): "+this.iterationCount);
		long startTime=System.nanoTime(), endTime; // for evaluating time performances
		
		Vector_3[] tetaRepulsive = computeAllRepulsiveForces();  // compute the displacements due to repulsive forces (for each vertex)
		Vector_3[] tetaAttractive = computeAllAttractiveForces(); // compute the displacements due to attractive forces (for each vertex)
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
	 * Cooling system: the temperature decreases linearly at each iteration
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
