/*
 * Created on February 23, 2015 by kostas-κγ  
 *
 * This is part of the InfoTweetsAlgorithms project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetsalgorithms.models.classification;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;

import java.math.BigDecimal;

import infotweetsalgorithms.input.document.Document;

/**
 * CrossValidation: Given a training dataset, the number of partitions,
 * and a supervized text classification learning model, 
 * trains the model and validates the accuracy of its predictions.
 *
 * @author kostas-κγ 
 */
public abstract class Classifier {
    
    /** The number of tweet correctly identified as non-informative. */
    protected int tp; 
    /** The number of tweet incorrectly identified as non-informative. */
    protected int fp;
    /** The number of tweet correctly identified as informative. */
    protected int tn;
    /** The number of tweet incorrectly identified as informative. */
    protected int fn;

    /** The number of partitions for this cross validation. */
    protected int numPartitions;

    protected Splitter splitter;

    /** The whole labeled training dataset that will be cross validated. */
    protected List<Document> documents;
    /** The partition of the training dataset that is used for training
	each time. This is the 80% of the whole labeled training dataset. */
    protected List<Document> trainingDocuments;
    /** The partition of the training dataset that is used for validation
	each time. This is the 20% of the whole labeled training dataset. */
    protected List<Document> testingDocuments;

    protected List<BigDecimal> documentIDs;

    /**
     * Constructs a new cross validation instance, 
     * given a training dataset,
     * the number of tweets, the number of paritions,
     * and a supervized text classification tc.
     *
     * @param trainingData a training dataset
     * @param numTweets
     * @param numPartitions
     * @param tc a supervized text classification tc 
     */
    public Classifier(List<Document> documents, int numPartitions) {	
	this.tp = 0; 
	this.fp = 0; 
	this.tn = 0; 
	this.fn = 0; 
	
	this.documents = documents;
	this.numPartitions = numPartitions;

	this.documentIDs = new ArrayList<BigDecimal>();
    } // Classifier()

    public int getTP() {return this.tp;}
    public int getFP() {return this.fp;}
    public int getTN() {return this.tn;}
    public int getFN() {return this.fn;}

    /**
     * Splits the training labeled dataset into two datasets:
     * training and testing, given a pointer start offset 
     * in training dataset, training dataset's size, 
     * each partition's size. 
     *
     * @param datasetStartOffset the pointer start offset in the training dataset.
     * @param trainingRange the size of the training dataset
     * @param partitionRange the size of each partition
     */
    protected void splitData(int datasetStartOffset, int trainingRange, 
			     int partitionRange) {
	//System.out.println(" ((c)) Splitting training dataset..."); 
	this.splitter = new Splitter(datasetStartOffset, trainingRange, 
				     partitionRange, this.documents);
	this.splitter.splitDataset();
	this.trainingDocuments = this.splitter.getTrainingDocuments();
	this.testingDocuments = this.splitter.getTestingDocuments();
	//System.out.println(" ((c)) Training dataset splitted."); 
    } // splitData()

    /**
     * Runs classification with cross validation.
     *
     */
    public void doClassification() {
	int numOfDocuments = this.documents.size();
	int partitionRange = numOfDocuments/this.numPartitions; 
	int trainingRange = numOfDocuments - partitionRange;
	int predictDataSize = partitionRange; 

	int setStartOffset = 0; 
	int currentPartition = 0;
	int datasetStartOffset = 0;
	while(currentPartition < this.numPartitions) {
	    // split the dataset into two partitions where:
	    // 80% is training, 20% is testing	    
	    splitData(datasetStartOffset,trainingRange,partitionRange);

	    reset(this.trainingDocuments);

	    //System.out.println("Start cv training.");
	    train(this.trainingDocuments);

	    //System.out.println("Start cv application.");
	    apply(this.testingDocuments);

	    currentPartition++;
	    datasetStartOffset = currentPartition*partitionRange;
	    evaluateResult();
	}
	printResults();
    } // run()

    protected abstract void reset(List<Document> trainingDocuments);
    protected abstract void train(List<Document> trainingDocuments);
    protected abstract void apply(List<Document> testingDocuments);

    /**
     * Validates the result of this cross validation by estimating
     * the number of true positives, false positives, true negatives,
     * false negatives.
     */
    protected void evaluateResult() {
	int currentPartitionTP = 0;
	int currentPartitionFP = 0;
	int currentPartitionTN = 0;
	int currentPartitionFN = 0;
	//if(this.classifier instanceof classifiers.features.bayesiannetwork.BayesianNetwork)
	//((classifiers.features.bayesiannetwork.BayesianNetwork)this.classifier).printValidationStatistics();

	// Also keep tp 
	//this.classifier.printValidationStatistics();
	for(Document doc : this.testingDocuments) {
	    boolean labelInformative = doc.labelInformative;
	    boolean assignInformative = doc.assignInformative; 

	    if(labelInformative == true && 
	       assignInformative == true) {
		this.tp++; //Correct Assignment
		currentPartitionTP++;
		if(!this.documentIDs.contains(doc.id))
		    this.documentIDs.add(doc.id);
	    } else if(labelInformative == false &&
		      assignInformative == true){
		this.fp++;
		currentPartitionFP++;
	    } else if(labelInformative == false && 
		      assignInformative == false) { 
		this.tn++; //Correct Assignment
		currentPartitionTN++;
	    } else if(labelInformative == true && 
		      assignInformative == false) { 
		this.fn++; //Wrong Assignment
		currentPartitionFN++;
	    }
	}
	double acc = 100.0*(currentPartitionTP+currentPartitionTN)/(currentPartitionTP+currentPartitionFP+currentPartitionTN+currentPartitionFN);
	System.out.println("partition accuracy: " + acc + " %");
	System.out.println
	    ("[TP] Correctly identified as informative: " 
	     + currentPartitionTP + "\ttotal so far: " + this.tp);
	System.out.println
	    ("[FP] Incorrectly identified as informative (extra): " 
	     + currentPartitionFP + "\ttotal so far: " + this.fp);
	System.out.println
	    ("[TN] Correctly identified as non-informative: "
	     + currentPartitionTN + "\ttotal so far: " + this.tn);
	System.out.println
	    ("[FN] Incorrectly identified as non-informative (missed): " 
	    + currentPartitionFN + "\ttotal so far: " + this.fn);
	System.out.println
	    ("Filtered out documents: " + this.documentIDs.size());
    } // evaluateResult()

    /**
     * Estimates accuracy for this cross validation.
     *
     * @return the accuracy
     */
    private double estimateAccuracy() {
	double accuracy = 100.0*(this.tp+this.tn)/(this.tp+this.fp+this.tn+this.fn); //correct / total
	return accuracy;
    } // getAccuracy()

    /**
     * Estimates recall for this cross validation.
     *
     * @return the recall
     */
    private double estimateRecall() {
	double recall = 100.0*(this.tp)/(this.tp+this.fn); //correct / total
	return recall;
    } // getAccuracy()

    /**
     * Estimates precision for this cross validation.
     *
     * @return the precision
     */
    private double estimatePrecision() {
	double precision = 100.0*(this.tp)/(this.tp+this.fp); //correct / total
	return precision;
    } // getAccuracy()

    /**
     * Estimates f2-score for this cross validation.
     *
     * @return the f2 score
     */
    private double estimateF2score(double precision, double recall) {
	double f2score = 5*(precision*recall)/(4*precision + recall); //correct / total
	return f2score;
    } // getAccuracy()

    /**
     *
     */
    protected void printResults() {
	System.out.println("\nFinal\n-------------------------------\n");
		    
	System.out.println
	    ("[TP] Correctly identified as informative: " + tp);
	System.out.println
	    ("[FP] Incorrectly identified as informative: " + fp);
	System.out.println
	    ("[TN] Correctly identified as non-informative: " + tn);
	System.out.println
	    ("[FN] Incorrectly identified as non-informative: " + fn);
	System.out.println("\n");
	System.out.println
	    ("Total filtered out documents: " + this.documentIDs.size());	    
	System.out.println
	    ("average accuracy: " + estimateAccuracy() + " %");
	double precision = estimatePrecision();
	double recall = estimateRecall();
	System.out.println
	    ("average recall: " + recall + " %");
	System.out.println
	    ("average F2-score: " + estimateF2score(precision,recall) + " %");

	try {
	    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("filteredDocIDs.txt")));// , true)));
	    for(BigDecimal docID : this.documentIDs) {
		out.println(docID);
		//out.print("\n");
	    }
	    out.flush();
	    out.close();
	} catch (java.io.IOException e) {

	}
	/*
	if(this instanceof WeightedMultinomialNB) {
	    try {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("weightsLog.txt", true)));
	out.print(((WeightedMultinomialNB)this).weightFreqHashtags);
		out.print("\t");
		out.print(((WeightedMultinomialNB)this).weightFreqWords);
		out.print("\t");
		out.print(((WeightedMultinomialNB)this).weightFreqUsers);
		out.print("\t");
	out.print(((WeightedMultinomialNB)this).weightSmoothHashtags);
		out.print("\t");
		out.print(((WeightedMultinomialNB)this).weightSmoothWords);
		out.print("\t");
		out.print(((WeightedMultinomialNB)this).weightSmoothUsers);
		out.print("\t");
		out.print(tp);
		out.print("\t");
		out.print(fp);
		out.print("\t");
		out.print(tn);
		out.print("\t");
		out.print(fn);
		out.print("\n");
		out.close();
	    } catch (java.io.IOException e) {
	    }
	    }*/
    } // printResults()

} // CrossValidation
