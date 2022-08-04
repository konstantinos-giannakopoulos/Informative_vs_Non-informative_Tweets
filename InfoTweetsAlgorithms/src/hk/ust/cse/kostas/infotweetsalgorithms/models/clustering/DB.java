/*
 * Created on February 22, 2015 by kostas-κγ 
 *
 * This is part of the OutlierDetector project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetsalgorithms.models.clustering;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import infotweetsalgorithms.input.datapoint.Datapoint; 

public class DB extends Clustering {

    public static boolean ASC = true;
    public static boolean DESC = false;

    /** Least fraction of the objects in the dataset. */
    public double p;

    /** Distance. */
    public double d;

    /* Array with the DB for each datapoint. */
    double [] db;

    public DB(List<Datapoint> datapoints) {
	super(datapoints);

	this.p = 0.05;
	this.d = 0.09;
	this.db = new double[this.numOfDatapoints]; 
	Arrays.fill(this.db,0.0);
    } // DB()

    /**
     * Finds all the datapoints within the distance.
     */
    public void findPointsInDistance() {
	Map<Integer,Double> distancesForPoint = 
	    new HashMap<Integer,Double>();
	//List<Integer> withinDistancePoints = new ArrayList<Integer>();
	for(int i = 0; i < this.numOfDatapoints; i++) {
	    //System.out.println("[dbg] point " + i + " : ");
	    distancesForPoint.clear();
	    //withinDistancePoints.clear();
	    for(int j = 0; j < this.numOfDatapoints; j++) {
		// estimatekNearestPoints(i);
		// collect the pairwise distances for each point.
		//System.out.println("[dbg]\twith point: " + j);
		distancesForPoint.put
		    (j,this.datapointsPairwiseDistances[i][j]);
		//System.out.println("[dbg]\t\tdistance: " 
		//+ this.datapointsPairwiseDistances[i][j]); 
	    }

	    for (Map.Entry<Integer,Double> entry : 
		     distancesForPoint.entrySet()) {
		if(entry.getValue() <= this.d)
		    this.withinDistancePoints[i].add(entry.getKey());
	    }
	}
    } // findPointsWithinhDistances()

    public void estimate() {
	double fractionForPoint = 0.0;
	for(int i = 0; i < this.numOfDatapoints; i++) {
	    Datapoint currentPoint = this.datapoints.get(i); 
	    fractionForPoint = 1.0 * 
		this.withinDistancePoints[i].size() / this.numOfDatapoints;
	    if(fractionForPoint >= this.p) // inlier, informative
		//this.db[i] = 0.0;
		currentPoint.assignInformative = true;
	    else // outlier
		//this.db[i] = 1.0;
		currentPoint.assignInformative = false;
	}
    } // estimate()

} // DB