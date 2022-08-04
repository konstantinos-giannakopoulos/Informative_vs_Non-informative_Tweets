/*
 * Created on February 27, 2015 by kostas-κγ 
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

import infotweetsalgorithms.input.document.Document;

/**
 * NaiveBayes: the implementation of the naive bayes classifier,
 * for the combination of the features.
 *
 * @author kostas
 */
public class BayesianNetwork extends Classifier {

    private int hashtagCeiling;
    private int wordsCeiling;
    private int recipientsCeiling;

    private double[] priors;
    public double[][] wordsLikelihood;
    public double[][] hashtagsLikelihood;
    public double[][] urlLikelihood;
    public double[][] recipientsLikelihood;
    //private double[][] retweetLikelihood;

    private double wordsWeight; // 0.15
    private double hashtagsWeight; // 0.3
    private double urlWeight; // 0.2
    private double recipientsWeight; // 0.1    
    //private double retweetWeight; // 0.1

    /**
     *
     */
    public BayesianNetwork(List<Document> documents, int numPartitions) {
	super(documents, numPartitions); 
	initCeilings();

	this.priors = new double[2];

	this.wordsLikelihood = new double[this.wordsCeiling][2];
	this.hashtagsLikelihood = new double[this.hashtagCeiling][2];
	this.urlLikelihood = new double[2][2];
	this.recipientsLikelihood = new double[this.recipientsCeiling][2];
	//this.retweetLikelihood = new double[2][2];

	this.wordsWeight = 1.0; //0.0;
	this.hashtagsWeight = 1.0; //0.3;
	this.urlWeight = 1.0; //0.7;
	this.recipientsWeight = 1.0; //0.0;
	//this.retweetWeight = 1.0; //0.0;

	initArraysForBinaryFeatures();
	initArraysForNumericalFeatures();
    } // BayesianNetwork()

    public void initCeilings() {
	this.wordsCeiling = 40;
	this.hashtagCeiling = 20;
	this.recipientsCeiling = 15;
    } // initCeilings()

    /**
     *
     */
    @Override public void reset(List<Document> trainingDocuments) {
	initCeilings();

	this.priors = new double[2];

	this.wordsLikelihood = new double[this.wordsCeiling][2];
	this.hashtagsLikelihood = new double[this.hashtagCeiling][2];
	this.recipientsLikelihood = new double[this.recipientsCeiling][2];
	this.urlLikelihood = new double[2][2];
	//this.retweetLikelihood = new double[2][2];

	initArraysForBinaryFeatures();
	initArraysForNumericalFeatures();
    } // reset()

    /**
     *
     */
    private void initArraysForBinaryFeatures() {
	// priorss
	for (int i=0; i < this.priors.length; i++)
	    this.priors[i] = 0.0;

	// binary features
	for (int i = 0; i < 2; i++) {
	    for (int j = 0; j < 2; j++) {
		this.urlLikelihood[i][j] = 0.0;
		//this.retweetLikelihood[i][j] = 0.0;
	    }
	}
    } // initArraysForBinaryFeatures()

    /**
     *
     */
    public void initArraysForNumericalFeatures() {
	// initiallize arrays with numerical features
	for (int i = 0; i < this.wordsCeiling; i++)
	    for (int j = 0; j < 2; j++)
		this.wordsLikelihood[i][j] = 0.0;

	for (int i = 0; i < this.hashtagCeiling; i++)
	    for (int j = 0; j < 2; j++) 
		this.hashtagsLikelihood[i][j] = 0.0;

	for (int i = 0; i < this.recipientsCeiling; i++)
	    for (int j = 0; j < 2; j++)
		this.recipientsLikelihood[i][j] = 0.0;	    	
    } // initArraysForNumericalFeatures

    /**
     * 
     * class assignments: c = 0 => non-outlier, c = 1 => outlier
     * features: 0 => |ht|, 1 => url, 2 => |w|, 3 => rt, 4 => |rp|, 5 => text classification 
     *
     * @param trainingData
     */
    @Override public void train(List<Document> trainingDocuments) {
	// reset this Bayesian Network training model
	//reset(trainingDocuments);

	int informativesCounter = this.splitter.countInformatives(); 
	int noninformativesCounter = this.splitter.countNonInformatives();
	int totalNum = informativesCounter + noninformativesCounter;
	double[] N_c = new double[2];
	N_c[0] = 1.0*informativesCounter;
	N_c[1] = 1.0*noninformativesCounter;

	// estimate priorss	
	for(int c = 0; c < 2; c++){
	    this.priors[c] = 1.0*N_c[c]/totalNum; 
	}

	for(Document trainingDoc : trainingDocuments) { 
	    //normalized tuple -- feature values -- for each training tweet
	    int [] values = trainingDoc.values;
	    int info = (trainingDoc.labelInformative)? 0 : 1;	    
	    this.wordsLikelihood[values[0]][info]++;
	    this.hashtagsLikelihood[values[1]][info]++;
	    this.urlLikelihood[values[2]][info]++; 
	    this.recipientsLikelihood[values[3]][info]++; 
	} // next
	
	
	// normalize probabilities
	// Laplace smoothing
	// 0 -- informative, 1 -- non-informative
	for(int c = 0; c < 2; c++){
	    for(int i = 0; i < this.wordsCeiling; i++)
		this.wordsLikelihood[i][c] = 
		    (this.wordsLikelihood[i][c] + 1) / (N_c[c] + 2);
	    for(int i = 0; i < this.hashtagCeiling; i++)
		this.hashtagsLikelihood[i][c] = 
		    (this.hashtagsLikelihood[i][c] + 1) / (N_c[c] + 2);
	    for(int i=0; i< 2; i++)
		this.urlLikelihood[i][c] = 
		    (this.urlLikelihood[i][c] + 1) / (N_c[c] + 2);
	    for(int i = 0; i < this.recipientsCeiling; i++)
		this.recipientsLikelihood[i][c] = 
		    (this.recipientsLikelihood[i][c] + 1) / (N_c[c] + 2);
	}	
	//trainingData.estimateFeaturePMFs();	
	System.out.println("Training finished.");
    } // train()

    /**
     * Given an array of normalized features for a tweet, predicts 
     * wheather the tweet is outlier or not.
     *
     * @param normalizedFeatures the array of normalized features for a tweet
     * @return
     */
    @Override protected void apply(List<Document> testingDocuments) {
	for(Document doc: testingDocuments) {
	    int [] values = doc.values;
	    double[] probability = new double[2];
	    double denominator = 0.0;
	    for(int c = 0; c < 2; c++) { 
		probability[c] = 
		    this.wordsLikelihood[values[0]][c] *
		    this.hashtagsLikelihood[values[1]][c] * 
		    this.urlLikelihood[values[2]][c] *
		    this.recipientsLikelihood[values[3]][c] *
		    this.priors[c];
		denominator += probability[c];
	    }
	    
	    doc.assignInformative = 
		(probability[0] > probability[1] ? true : false);
	    doc.assignProbability = 
		(probability[0] > probability[1] ? 
		 probability[0]/denominator : probability[1]/denominator);
	}
    } // apply()

    /*
    private void String promptTrainingVariables() {
	StringBuilder builder = new StringBuilder();
	for(int c = 0; c < 2; c++)
	    builder.append("priors["+c+"] = " + this.priors[c] + "\n");
	for(int c = 0; c < 2; c++){
	    for(int i = 0; i < this.hashtagCeiling; i++)
		builder.append("hashtags["+i+"]["+c+"] = "
			       + this.hashtagsLikelihood[i][c] + "\n");
	    for(int i=0; i< 2; i++)
		builder.append("url["+i+"]["+c+"] = "
			       + this.urlLikelihood[i][c]+"\n");
	    for(int i = 0; i < this.wordsCeiling; i++)
		builder.append("words["+i+"]["+c+"] = "
			       + this.wordsLikelihood[i][c]+"\n");
	    for(int i = 0; i < 2; i++)
		builder.append("retweet["+i+"]["+c+"] = "
			       + this.retweetLikelihood[i][c]+"\n");
	    for(int i = 0; i < this.recipientsCeiling; i++)
		builder.append("recipients["+i+"]["+c+"] = "
			       + this.recipientsLikelihood[i][c] +"\n");
	}	
	return builder.toString();
    } // trainingParametersToString()
    */
} // BayesianNetwork