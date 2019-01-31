import processing.core.*;

import java.awt.Color;
import java.io.File;

import Jcg.geometry.*;

/**
 * A simple 3d viewer for visualizing point clouds (based on Processing). It deals with methods for<p>
 * -) computing closest pairs <p>
 * -) computing the diameter of a point cloud <p>
 * 
 * @author Luca Castelli Aleardi (INF421,2018)
 *
 */
public class PointCloudViewer extends PApplet {

	PointSet points; // input point cloud
	int renderType=0; // choice of type of rendering
	int renderModes=3; // number of rendering modes
	
	DrawPointSet drawPointSet; // input point cloud
	Point_3[] selectedPoints=null; // selected points to draw in red 
	
	public static String filename;// input point clouds
	
	public void setup() {
		  size(1000,800,P3D);
		  ArcBall arcball = new ArcBall(this);

		  this.points=new PointSet(filename);
		  
		  this.drawPointSet=new DrawPointSet(this, this.points, null);
	}
		 
		public void draw() {
		  background(255);
		  //this.lights();
		  directionalLight(101, 204, 255, -1, 0, 0);
		  directionalLight(51, 102, 126, 0, -1, 0);
		  directionalLight(51, 102, 126, 0, 0, -1);
		  directionalLight(102, 50, 126, 1, 0, 0);
		  directionalLight(51, 50, 102, 0, 1, 0);
		  directionalLight(51, 50, 102, 0, 0, 1);
		 
		  translate(width/2.f,height/2.f,-1*height/2.f);
		  this.strokeWeight(1);
		  stroke(150,150,150);
		  
		  this.drawPointSet.draw(renderType);
		  this.drawOptions();
		  
		  if(this.selectedPoints!=null) {
			  for(Point_3 p: this.selectedPoints)
				  this.drawPointSet.drawPoint(p, Color.RED);
		  }
		  
		}
		
		public void keyPressed(){
			  switch(key) {
			    case('i'):case('I'): this.zoomIn(); break;
			    case('o'):case('O'): this.zoomOut(); break;
			    case('r'):this.renderType=(this.renderType+1)%this.renderModes; break;
			    case('c'):this.slowClosestPair(); break;
			    case('d'):this.slowDiameter(); break;
			    case('C'): this.fastClosestPair(); break;
			    case('D'): this.fastDiameter(); break;
			  }
		}
		
		public void drawOptions() {
			int hF=12;
			fill(0);
			this.textMode(this.SCREEN);
			this.text("press 'i' or 'o' for zooming", 10, hF);
			this.text("press 'c' for slow computation of closest pairs", 10, hF*2);
			this.text("press 'd' for slow computation of diameter", 10, hF*3);
			this.text("press 'C' for fast computation of closest pairs", 10, hF*4);
			this.text("press 'D' for fast computation of diameter", 10, hF*5);
		}

		public void slowClosestPair() {
			this.selectedPoints=null;
			Point_3[] inputPoints=this.points.toArray(); // input point cloud
			ClosestPair_3 slow=new SlowClosestPair_3();
			long startTime = System.currentTimeMillis();
			this.selectedPoints=slow.findClosestPair(inputPoints);
			System.out.println("slowClosest (ms):" + (System.currentTimeMillis() - startTime));
		}

		public void fastClosestPair() {
			this.selectedPoints=null;
			Point_3[] inputPoints=this.points.toArray(); // input point cloud
			ClosestPair_3 slow=new FastClosestPair_3();
			this.selectedPoints=slow.findClosestPair(inputPoints);
		}

		public void slowDiameter() {
			this.selectedPoints=null;
			Point_3[] inputPoints=this.points.toArray(); // input point cloud
			Diameter_3 slow=new SlowDiameter_3();
			long startTime = System.currentTimeMillis();
			this.selectedPoints=slow.findFarthestPair(inputPoints);
			System.out.println("slowDiameter (ms):" + (System.currentTimeMillis() - startTime));
		}

		public void fastDiameter() {
			double epsilon=0.7; // approximation factor
			this.selectedPoints=null;
			Point_3[] inputPoints=this.points.toArray(); // input point cloud
			Diameter_3 slow=new FastDiameter_3(epsilon);
			this.selectedPoints=slow.findFarthestPair(inputPoints);
		}

		public void zoomIn() {
			this.drawPointSet.zoom=this.drawPointSet.zoom*1.5;
		}

		public void zoomOut() {
			this.drawPointSet.zoom=this.drawPointSet.zoom*0.75;
		}

		public void scaleNormal(double scale) {
			this.drawPointSet.normalScale*=scale;
		}

		public void savePointCloud() {
			if(points!=null)
				points.toFile("output.off");
		}

		public static void setInputFile(String input) {
			if(input==null) { 
				System.out.println("Error: wrong input file "+input);
				System.exit(0);
			}
			File file = new File(input);
			if(file.exists()==false) { 
				System.out.println("Wrong input file: "+input+" not found");
				System.exit(0);
			}
			if(input.endsWith(".off")==false) { 
				System.out.println("Error: wrong input format "+input+" (.off required)");
				System.exit(0);
			}
			filename=input;
		}

		/**
		 * For running the PApplet as Java application
		 */
		public static void main(String args[]) {
			if(args.length!=1) {
				System.out.println("Wrong number of input parameters: required one file .off");
				System.exit(0);
			}
			PointCloudViewer.setInputFile(args[0]);
			
			PApplet.main(new String[] { "PointCloudViewer" });
		}

}
