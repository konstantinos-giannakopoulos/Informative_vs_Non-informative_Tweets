/*
 * Created on February 23, 2015 by kostas-κγ
 *
 * This is part of the InfoTweetAlgorithms project. 
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
import infotweetsalgorithms.distance.Distance;
import infotweetsalgorithms.distance.DistanceFactory;
import infotweetsalgorithms.util.Functions;

public abstract class Clustering { //extends ClusteringExecutionMode {
    
    protected List<Datapoint> datapoints;
    protected int numOfDatapoints;

    /* Array with pairwise distances of the datapoints. */ 
    protected double [][] datapointsPairwiseDistances;

    /* Array with kth distances for each datapoint. */ 
    double [] kthDistance;

    /* Array with the datapoints within kth distace for each datapoint. */
    List<Integer> [] withinDistancePoints;

    public Clustering(List<Datapoint> datapoints) {
	this.datapoints = new ArrayList<Datapoint>();
	this.datapoints.addAll(datapoints);
	this.numOfDatapoints = this.datapoints.size();
	this.datapointsPairwiseDistances 
	    = new double[this.numOfDatapoints][this.numOfDatapoints];
	this.kthDistance = new double[this.numOfDatapoints];

	this.withinDistancePoints = new ArrayList[this.numOfDatapoints];
	for(int i = 0; i < this.numOfDatapoints; i++) 
	    this.withinDistancePoints[i] = new ArrayList<Integer>();	
    }
    
    public final void doClustering() {
	estimatePairwiseDistances();  
	findPointsInDistance();
	estimate();
	//assignFinalClasses();
	evaluateResult();
    } // doClustering()


    protected void estimatePairwiseDistances() {
	String method = "euclidean";
	Distance distanceMetric = DistanceFactory.chooseDistance(method);
	int i = 0;
	for(Datapoint point1 : this.datapoints) {
	    int j = 0;
	    for(Datapoint point2 : this.datapoints) {
		if(i != j) {
		    this.datapointsPairwiseDistances[i][j] 
			= distanceMetric.estimate(point1,point2);
		} else {
		    
		}
		j++;
	    }
	    i++;
	}
    } // estimatePairwiseDistances()

    public void evaluateResult() {
	//Datapoint medianInlier = new Datapoint();
	//Datapoint medianOutlier = new Datapoint();
	int inlier = 0;
	int outlier = 0;
	int border = 0;
	int tp = 0; int fp = 0; int tn = 0; int fn = 0;
	double difference = 0.1;//1.0001;//1.00005;
	for(Datapoint currentPoint : this.datapoints) {
	    boolean labelInformative = currentPoint.labelInformative;
	    boolean assignInformative = currentPoint.assignInformative;
	    //int indexofDatapointInDataset = 
	    //this.datapoints.get(i).indexInDataset;
	    if(labelInformative == true && 
	       assignInformative == true) { // positive
		tp++;
		//this.logfiles.appendTPLog
		//  (this.datapoints.get(i).toString());
		//this.logfiles.appendLofLog
		//("TP: " + this.datapoints.get(i).toString() 
		//+ "\n\t\tLOF: " + this.lof[i]);
		//this.logfiles.appendLofLog
		//("\t\t" + this.tweets.searchTweetByIndex
		//(indexofDatapointInDataset).getMessages() 
		//+ "\t" + this.tweets.searchTweetByIndex
		//(indexofDatapointInDataset).getCleanTweet());
	    } else if(labelInformative == false && 
		      assignInformative == true){//positive
		fp++;
		//this.logfiles.appendFPLog
		//  (this.datapoints.get(i).toString());
		/*
		this.logfiles.appendLofLog
		    ("FP: " + this.datapoints.get(i).toString() 
		     + "\n\t\tLOF: " + this.lof[i]);
		this.logfiles.appendLofLog
		    ("\t\t" + this.tweets.getTweetItemByTweetId
		     (indexofDatapointInDataset).getMessages() 
		     + "\n\t\t" + this.tweets.getTweetItemByTweetId
		     (indexofDatapointInDataset).getCleanTweet());
		*/
	    } else if(labelInformative == false && 
		      assignInformative == false) { //negative 
		tn++;
		//this.logfiles.appendTNLog
		//  (this.datapoints.get(i).toString());
		/*
		this.logfiles.appendLofLog
		    ("TN: " + this.datapoints.get(i).toString() 
		     + "\n\t\tLOF: " + this.lof[i]);
		this.logfiles.appendLofLog
		    ("\t\t" + this.tweets.getTweetItemByTweetId
		     (indexofDatapointInDataset).getMessages() 
		     + "\n\t\t" + this.tweets.getTweetItemByTweetId
		     (indexofDatapointInDataset).getCleanTweet());
		*/
	    } else if(labelInformative == true && 
		      assignInformative == false) { //negative
		fn++;
		//this.logfiles.appendFNLog
		//  (this.datapoints.get(i).toString());
		//this.logfiles.appendLofLog
		//("FN: " + this.datapoints.get(i).toString() 
		//+ "\n\t\tLOF: " + this.lof[i]);
		//this.logfiles.appendLofLog
		//("\t\t" + this.tweets.searchTweetByIndex
		//(indexofDatapointInDataset).getMessages() 
		//+ "\t" + this.tweets.searchTweetByIndex
		//(indexofDatapointInDataset).getCleanTweet());
	    }
	}

	double acc = 100.0*(tp + tn)/( tp + fp + tn + fn);
	System.out.println("accuracy: " + acc + " %");
	System.out.println
	    ("[TP] Correctly identified as informative: " + tp);
	System.out.println
	    ("[FP] Incorrectly identified as informative: " + fp 
	     + " (extra informative)");
	System.out.println
	    ("[TN] Correctly identified as non-informative: " + tn);
	System.out.println
	    ("[FN] Incorrectly identified as non-informative: " + fn 
	     + " (missed informative)");
	double sensitivity = (1.0 * tp) / (tp + fn);
	System.out.println("sensitivity = " + sensitivity);
	double specificity = (1.0 * tn) / (tn + fp);
	System.out.println("specificity = " + specificity);
	/*
	this.logfiles.appendLofLog("accuracy: " + acc + " %");
	this.logfiles.appendLofLog
	    ("[TP] Correctly identified as outliers: " + tp);
	this.logfiles.appendLofLog
	    ("[FP] Incorrectly identified as outliers: " + fp);
	this.logfiles.appendLofLog
	    ("[TN] Correctly identified as non-outliers: " + tn);
	this.logfiles.appendLofLog
	    ("[FN] Incorrectly identified as non-outliers: " + fn 
	    + " (missed outliers)");*/
    } // evaluateResult()

    public abstract void findPointsInDistance();
    public abstract void estimate();
    //abstract public void assignFinalClasses(); 

} // ClusteringMethod