
import jdg.graph.AdjacencyListGraph;
import jdg.graph.Node;
import jdg.layout.Layout;
import Jcg.geometry.PointCloud_3;
import Jcg.geometry.Point_3;
import Jcg.geometry.Vector_3;

/**
 * A class implementing the force directed algorithm by Fruchterman and Reingold (1991)
 * 
 * @author Luca Castelli Aleardi, Ecole Polytechnique
 * @version december 2018
 */
public class FR91Layout extends Layout {
	// parameters of the algorithm by Fruchterman and Reingold
	public double k; // natural spring length
	public double area; // area of the drawing (width times height)
	public double C; // step
	public double temperature; // initial temperature
	public double minTemperature; // minimal temperature (strictly positive)
	public double coolingConstant; // constant term: the temperature decreases linearly at each iteration
	
	public int iterationCount=0; // count the number of performed iterations
	private int countRepulsive=0; // count the number of computed repulsive forces (to measure time performances)
	
	/**
	 * Initialize the parameters of the force-directed layout
	 * 
	 *  @param g  input graph to draw
	 *  @param w  width of the drawing area
	 *  @param h  height of the drawing area
	 *  @param C  step length
	 */
	public FR91Layout(AdjacencyListGraph g, double w, double h) {
		System.out.print("Initializing force-directed method: Fruchterman-Reingold 91...");
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
	 * Perform one iteration of the Force-Directed algorithm.
	 * Positions of vertices are updated according to their mutual attractive and repulsive forces.
	 */	
	public void computeLayout() {
		System.out.print("Performing iteration (FR91): "+this.iterationCount);
		long startTime=System.nanoTime(), endTime; // for evaluating time performances
		
		// first step: for each vertex compute the displacements due to attractive and repulsive forces
		
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
        this.cooling(); // update temperature
		
		// evaluate time performances
    	endTime=System.nanoTime();
        double duration=(double)(endTime-startTime)/1000000000.;
        System.out.println("iteration "+this.iterationCount+" done ("+duration+" seconds)");
		this.iterationCount++; // increase counter (to count the number of performed iterations)
	}
	
	/**
	 * Compute the displacement of vertex 'u', due to repulsive forces (of all nodes)
	 * 
	 * @param u  the vertex to which repulsive forces are applied
	 * @return 'displacement' a 3d vector storing the displacement of vertex 'u'
	 */	
	private Vector_3 computeRepulsiveForce(Node v) {
		Vector_3 delta;
		double norm;
		Vector_3 displacement = new Vector_3(0,0,0);
		for (Node u : g.vertices){
			delta= new Vector_3(u.p, v.p);  // compute delat=u-v
			norm=Math.sqrt((double) delta.squaredLength());  //compute norm of delta
			if (norm!=0){
				displacement=displacement.sum(delta.multiplyByScalar(repulsiveForce(norm)/norm)); //compute displacement of v due to u
			}
		}
		return displacement;
	}
	
	/**
	 * Compute, for each vertex, the displacement due to repulsive forces (between all nodes)
	 * 
	 * @return a vector v[]: v[i] stores the geometric displacement of the i-th node
	 */	
	private Vector_3[] computeAllRepulsiveForces() {
		Vector_3[] teta = new Vector_3[g.sizeVertices()];
		int i=0;
		for (Node v : g.vertices){
			teta[i]=computeRepulsiveForce(v);
			i++;
		}
		return teta;
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
			delta= new Vector_3(v.p, u.p);  // compute delat=u-v
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
	 * Cooling system: the temperature decreases linearly at each iteration
	 * 
	 * Remark: the temperature is assumed to remain strictly positive (>=minTemperature)
	 */	
	protected void cooling() {
		this.temperature=Math.max(this.temperature*coolingConstant, minTemperature);
		//this.temperature=Math.max(this.temperature-coolingConstant, minTemperature); // variant
	}
	
	public String toString() {
		String result="force-directed algorihm: Fruchterman Reingold\n";
		result=result+"\t area= "+w+" x "+h+"\n";
		result=result+"\t k= "+this.k+"\n";
		result=result+"\t C= "+this.C+"\n";
		result=result+"\t initial temperature= "+this.temperature+"\n";
		result=result+"\t minimal temperature= "+this.minTemperature+"\n";
		result=result+"\t cooling constant= "+this.coolingConstant+"\n";
		
		return result;
	}
	
}
