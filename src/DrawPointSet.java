import java.awt.Color;

import Jcg.geometry.*;
import processing.core.PApplet;

/**
 * Class for rendering a surface triangle mesh (using Processing)
 * 
 * @author Luca Castelli Aleardi
 *
 */
public class DrawPointSet {
	
	PApplet view; // Processing 3d frame (where meshes are rendered)
	public PointSet points1, points2; // input point clouds
	public double[][] normals=null; // normals are not defined at the beginning (they must be computed)
	public boolean[] outliers=null; // outliers have to be computed
	
	// rendering parameters
	double scaleFactor=60; // scaling factor: useful for 3d rendering
	public boolean showNormals=false;
	public boolean showOutliers=false;
	public double zoom=1.; // for scaling the drawing
	public double normalScale=1.; // for scaling the normal vectors
	public float pointSize=40;
	
	/**
	 * Create a surface mesh from an OFF file
	 */	
	public DrawPointSet(PApplet view, PointSet points1, PointSet points2) {
		this.view=view;
   	    this.points1=points1;
   	    this.points2=points2;
    	this.scaleFactor=this.computeScaleFactor();
	}
	
	/**
	 * Draw a point (as a small sphere)
	 */	
	public void drawPoint(Point_3 p) {
		float s=(float)this.scaleFactor*(float)this.zoom;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		float z1=(float)p.getZ().doubleValue()*s;
		
		view.translate(x1, y1, z1);
		view.sphere(s/this.pointSize);
		view.translate(-x1, -y1, -z1);
	}

	/**
	 * Draw a point (as a small sphere)
	 */	
	public void drawPoint(Point_3 p, Color c) {
		float s=(float)this.scaleFactor*(float)this.zoom;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		float z1=(float)p.getZ().doubleValue()*s;
		
		this.view.stroke(c.getRed(), c.getGreen(), c.getBlue());
		view.translate(x1, y1, z1);
		view.sphere(2*s/this.pointSize);
		view.translate(-x1, -y1, -z1);
	}

	/**
	 * Draw the vertex normal
	 */	
	public void drawNormal(Point_3 p, double[] n) {
		double norm=Math.sqrt(n[0]*n[0]+n[1]*n[1]+n[2]*n[2]);
		double factor=normalScale*(this.scaleFactor/2000f)/norm;
		
		//System.out.println("vertex normal "+p);
		float x1=(float)p.getX().doubleValue();
		float y1=(float)p.getY().doubleValue();
		float z1=(float)p.getZ().doubleValue();
		float x2=(float)(n[0]*factor);
		float y2=(float)(n[1]*factor);
		float z2=(float)(n[2]*factor);
		
		this.drawSegment(p, new Point_3(x1+x2, y1+y2, z1+z2), new float[]{0f, 255f, 0f, 255f});
		this.drawSegment(p, new Point_3(x1-x2, y1-y2, z1-z2), new float[]{0f, 255f, 0f, 255f});
	}

	/**
	 * Draw a (colored) segment between two points
	 */	
	public void drawSegment(Point_3 p, Point_3 q, float[] color) {
		float s=(float)this.scaleFactor*(float)this.zoom;
		float x1=(float)p.getX().doubleValue()*s;
		float y1=(float)p.getY().doubleValue()*s;
		float z1=(float)p.getZ().doubleValue()*s;
		float x2=(float)q.getX().doubleValue()*s;
		float y2=(float)q.getY().doubleValue()*s;
		float z2=(float)q.getZ().doubleValue()*s;
		
		this.view.stroke(color[0], color[1], color[2], color[3]);
		this.view.line(	x1, y1, z1, x2, y2, z2 );		
	}

	/**
	 * Draw the entire point cloud
	 */
	public void draw(int type) {
		//this.drawAxis();
		
		view.noStroke();
		
		if(points1!=null) {
			int i=0;
			for(Point_3 p: this.points1.listOfPoints()) {
				if(showOutliers==true && outliers!=null && outliers[i]==true) {
					view.fill(100f, 250f, 100f); // outliers in the first point cloud are drawn in green
				}
				else {
					view.fill(0f, 0f, 250f); // points in the first point cloud are drawn in blue by default
				}
				this.drawPoint(p);
				i++;
			}
			
			if(showNormals==true && normals!=null){
				i=0;
				view.fill(0f, 250f, 0f); // draw vertex normals as green segments
				for(Point_3 p: this.points1.listOfPoints()) {
					this.drawNormal(p, normals[i]);
					i++;
				}
			}

		}
		
		if(points2!=null) {
			view.fill(250f, 0f, 0f); // points in the first point cloud are drawn in blue by default
			for(Point_3 p: this.points2.listOfPoints()) {
				this.drawPoint(p);
			}
		}
		view.strokeWeight(1);
	}
	
	/**
	 * Compute the scale factor (depending on the maximal distance from the origin)
	 */
	public double computeScaleFactor() {
		if(this.points1==null || this.points1.size()<1)
			return 1;
		double maxDistance=0.;
		Point_3 origin=new Point_3(0., 0., 0.);
		for(Point_3 p: this.points1.listOfPoints()) {
			double distance=Math.sqrt(p.squareDistance(origin).doubleValue());
			maxDistance=Math.max(maxDistance, distance);
		}
		return Math.sqrt(3)/maxDistance*150;
	}
	
	/**
	 * Update the scale factor
	 */
	public void updateScaleFactor() {
		this.scaleFactor=this.computeScaleFactor();
	}
	
}
