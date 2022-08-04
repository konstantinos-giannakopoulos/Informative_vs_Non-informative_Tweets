/*
 * Created on February 23, 2014 by kostas-κγ
 *
 * This is part of the InfoTweetAlgorithms project. 
 * Any subsequent modification
 * of the file should retain this disclaimer. 
 *
 * The Hong Kong University of Science and Technology,
 * School of Computer Science and Engineering
 */
package infotweetsalgorithms.models.classification;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

import infotweetsalgorithms.input.document.Document;

/**
 * MultivariateBernoulliNB:
 *
 * @author kostas-κγ 
 */
public class MultivariateBernoulliNB extends TextClassifier {

    /**
     * Takes dataset and training data as input.
     * Applies Multivariate Bernouli Naive Bayes.
     */
    public MultivariateBernoulliNB
	(List<Document> documents, int numPartitions) {
	super(documents, numPartitions);
    } // MultivariateBernoulliNB()

    /**
     * NaiveBayes abstract interface -- specifies how to train 
     * this multivariate Bernoulli model given a training 
     * dataset.
     * 
     * @param trainingDocuments the trainig dataset.
     */
    @Override protected void train(List<Document> trainingDocuments) {
	//reset();
	//List<TweetItem> trainingTweets = trainingData.getData();
	System.out.println("Voc size: " + this.vocabulary.sizeOf()); 
	int informativesCounter = this.splitter.countInformatives();
	int noninformativesCounter = this.splitter.countNonInformatives();
	int totalNum = informativesCounter + noninformativesCounter;  
	double[] N_c = new double[2];
	N_c[0] = 1.0*informativesCounter;
	N_c[1] = 1.0*noninformativesCounter;

	for(int c = 0; c < 2; c++){
	    this.priors[c] = 1.0*N_c[c]/totalNum; 
	    for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		int N_ct = countTweetsInClassContainingTerm
		    (c, v, trainingDocuments);
		this.conditionalProbabilities[v][c] = 
		    1.0*(N_ct + 1)/(N_c[c] + 2);
	    }
	}
    } // train()

    /**
     * Given a class, a vocabulary term index and a collection of training
     * tweets, count how many tweets of the training dataset that contain 
     * the vocabulary term belong to the class.
     *
     * @param c the class
     * @param vocabularyTermIndex the index of a vocabulary term.
     * @param trainingTweets the collection of training tweets.
     * @return the integer number of tweets of the training dataset that contain
     * the vocabulary term belong to the class.
     */
    private int countTweetsInClassContainingTerm
	(int c, int v, List<Document> trainingDocuments) {
	int counter = 0;
	for(Document trainingDoc : trainingDocuments) { 
	    List<String> terms = trainingDoc.tokens; 
	    if(c == 0) { 
		if(trainingDoc.labelInformative && 
		   terms.contains(this.vocabulary.getTermByIndex(v)))
		    counter++;
	    } else if(c == 1) { 
		if(!trainingDoc.labelInformative && 
		   terms.contains(this.vocabulary.getTermByIndex(v)))
		    counter++;
	    }
	}
	return counter;
    } // countTweetsInClassContainingTerm()

    /**
     *
     *
     * @param 
     * @return <code>true</code> if the feature vector is classified 
     * as non-informative, <code>false</code> otherwise.
     */
    @Override protected void apply(List<Document> testingDocuments) {
	for(Document doc: testingDocuments) {  
	    Set<Integer> docTokensIndexesInVocabulary = 
		new HashSet<Integer>(); 
	    for(String token : doc.tokens) { 
		int vocIndex = this.vocabulary.indexOfTerm(token);
		if(vocIndex != -1)  
		    docTokensIndexesInVocabulary.add(vocIndex); 
	    }

	    double[] score = new double[2];
	    double[] logScore = new double[2];

	    for(int c = 0; c < 2; c++) {
		logScore[c] = Math.log(this.priors[c]);
		score[c] = this.priors[c];
		
		for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		    if(docTokensIndexesInVocabulary.contains(v)) {
			logScore[c] += Math.log
			    (this.conditionalProbabilities[v][c]);
			score[c] *= this.conditionalProbabilities[v][c];
		    } else {
			logScore[c] += Math.log
			    (1 - this.conditionalProbabilities[v][c]);
			score[c] *=(1-this.conditionalProbabilities[v][c]);
		    }
		}
	    }
	    /* argmax score[c] */
	    doc.assignInformative = (score[0] > score[1]) ? true : false;
	    doc.assignProbability = 
		(score[1] > score[0]) ? score[1] : score[0];
	}
    } // apply()

} // MultivariateBernoulliNB