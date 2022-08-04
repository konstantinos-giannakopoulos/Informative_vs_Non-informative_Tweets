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

import infotweetsalgorithms.input.datapoint.Datapoint;
import infotweetsalgorithms.util.Functions;

public class LOF extends Clustering {

    private static boolean ASC = true;
    private static boolean DESC = false;
    
    /* Number of nearest neighbors. */
    int k;

    /* Array with the lrd for each datapoint. */
    double [] lrd;

    /* Array with the LOF for each datapoint. */
    double [] lof;

    public LOF(List<Datapoint> datapoints) {
	super(datapoints);

	this.lrd = new double[this.numOfDatapoints];
	this.lof = new double[this.numOfDatapoints];
    } // LOF()

    public void setk(int k) {
	this.k = k;
    }

    public void estimate() {
	estimateLRDk();
	estimateLOFk();	
	assignFinalClasses();
    }

    /**
     * Finds all datapoints in distance k.
     */
    public void findPointsInDistance() {
	Map<Integer,Double> distancesForPoint 
	    = new HashMap<Integer,Double>();
	for(int i = 0; i < this.numOfDatapoints; i++) {
	    distancesForPoint.clear();
	    for(int j = 0; j < this.numOfDatapoints; j++) {
		if(i == j) continue;
		distancesForPoint.put
		    (j,this.datapointsPairwiseDistances[i][j]);
	    }

	    Map<Integer, Double> sortedDistancesForPointAscValue 
		= Functions.sortByComparator(distancesForPoint, ASC);

	    // retrieve kth distance for current point.
	    this.kthDistance[i] = retrievekDistance
		(this.k, sortedDistancesForPointAscValue);

	    // retrieve the points within the kth distance.
	    for (Map.Entry<Integer,Double> entry : 
		     sortedDistancesForPointAscValue.entrySet()) {
		if(entry.getValue() <= this.kthDistance[i])
		    this.withinDistancePoints[i].add(entry.getKey());
		else
		    break;
	    }
	}
    } // findkthDistances()

    private static double retrievekDistance
	(int k, Map<Integer, Double> source) {
	int count = 0;
	double kDistance = 0.0;
	HashMap<Integer,Double> target = new HashMap<Integer,Double>();
	for (Map.Entry<Integer,Double> entry:source.entrySet()) {
	    if (count < k) { //break;
		if(kDistance != entry.getValue()) {
		    kDistance = entry.getValue();
		    count++;
		}
	    } else {
		return kDistance;
	    }
	}
	return kDistance;
    } // retrievekDistance()

    private void estimateLRDk() {
	for(int i = 0; i < this.numOfDatapoints; i++) {
	    double sum_rdk = 0.0;	   
	    /* for each neighbor point estimate the sum */
	    for(int neighborPoint : this.withinDistancePoints[i]) {
		double rdk = Math.max
		    (this.kthDistance[neighborPoint], 
		     datapointsPairwiseDistances[i][neighborPoint]);
		sum_rdk += rdk;
	    }	    
	    double nk = withinDistancePoints[i].size();
	    double term = (1.0 * sum_rdk) / nk;	    
	    this.lrd[i] = 1.0 / term;
	}
    } // estimateLRDk()

    private void estimateLOFk() {
	for(int i = 0; i < this.numOfDatapoints; i++) {
	    double sum = 0.0;	   
	    /* for each neighbor point estimate the sum */
	    for(int neighborPoint : this.withinDistancePoints[i]) {
		double lrd = (1.0* this.lrd[neighborPoint]) / this.lrd[i];
		sum += lrd;
	    }	    
	    double nk = withinDistancePoints[i].size();	   
	    this.lof[i] = (1.0*sum) / nk;
	}	
    } // estimateLOFk()

    private void assignFinalClasses() {
	//Datapoint medianInlier = new Datapoint();
	//Datapoint medianOutlier = new Datapoint();
	int inlier = 0;
	int outlier = 0;
	int border = 0;
	int tp = 0; int fp = 0; int tn = 0; int fn = 0;
	double difference = 0.1;//1.0001;//1.00005;
	for(int i = 0; i < this.numOfDatapoints; i++) {
	    Datapoint currentPoint = this.datapoints.get(i);
	    if(this.lof[i] < (1.0 - difference)) { // inlier, informative
		inlier++;
		currentPoint.assignInformative = true; 
		//for(int j=0; j < currentPoint.elements.length; j++)  
		//  medianInlier.elements[j] += currentPoint.elements[j];
	    } else if(this.lof[i] < (1.0 + difference)) {
		border++;
		currentPoint.assignInformative = true; 
		//for(int j=0; j < currentPoint.elements.length; j++) 
		//  medianInlier.elements[j] += currentPoint.elements[j];
	    } else { // outlier, non-informative
		outlier++;
		currentPoint.assignInformative = false; 
		//for(int j=0; j < currentPoint.elements.length; j++)
		//  medianOutlier.elements[j] += currentPoint.elements[j];
	    }
	}

	//this.logfiles.appendLofLog("[dbg] LOF < 1: " + inlier
	//	   + "\tLOF ~ 1: " + border
	//	   + "\tLOF > 1: " + outlier);  	
	int in = inlier + border;
	int out = outlier > 0? outlier : 1;
	/*
	  for(int j = 0; j < medianInlier.elements.length; j++)
	  medianInlier.elements[j]=1.0*medianInlier.elements[j]/in;
	  
	  for(int j = 0; j < medianOutlier.elements.length; j++)
	  medianOutlier.elements[j]=1.0*medianOutlier.elements[j]/out;
	*/
	System.out.println("[dbg] LOF < 1: " + inlier
			   + "\tLOF ~ 1: " + border
			   + "\tLOF > 1: " + outlier);  	
	/*
	  System.out.println("inlier median:\n" + medianInlier);
	  System.out.println("outlier median:\n" + medianOutlier);
	*/

    } // assignFinalClasses()

} // LOF