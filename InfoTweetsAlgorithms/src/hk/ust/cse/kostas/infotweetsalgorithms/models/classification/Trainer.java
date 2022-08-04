package infotweetsalgorithms.models.classification;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileWriter;


import infotweetsalgorithms.input.document.Document;
import infotweetsalgorithms.vocabulary.Vocabulary;

/**
 * MultinomialNBTrainer:
 *
 *
 * @author kostas-κγ 
 */
public class Trainer {

    private List<Document> trainingDocuments;
    private double[] priors;
    private double[][] conditionalProbabilities;
    private Vocabulary vocabulary;

    /**
     * Takes dataset and training data as input.
     * Applies Multivariate Bernouli Naive Bayes.
     */
    public Trainer(List<Document> trainingDocuments) {
	this.trainingDocuments = trainingDocuments;
	this.priors = new double[2];
	this.vocabulary = new Vocabulary();
	this.vocabulary.build(trainingDocuments);
	int vocSize = this.vocabulary.sizeOf();
	this.conditionalProbabilities = new double[vocSize][2];
    }

    public Vocabulary getVocabulary() {
	return this.vocabulary;
    } // getVocabulary()

    /**
     *
     *
     */
    public void train() {
	//List<TweetItem> trainingTweets = trainingData.getData();
	System.out.println("Voc size: " + this.vocabulary.sizeOf());	
	int informativesCounter = countInformatives();
	int noninformativesCounter = countNonInformatives();
	int totalNum = informativesCounter + noninformativesCounter;
	double[] N_c = new double[2];
	N_c[0] = 1.0*informativesCounter;
	N_c[1] = 1.0*noninformativesCounter;
	int T = 0;
	int[][] T_c = new int[2][this.vocabulary.sizeOf()];

	for(int c = 0; c < 2; c++){
	    T = 0;
	    this.priors[c] = 1.0*N_c[c]/totalNum; 
	        List<Integer> textIndexes_c = 
		    concatenateTextOfAllDocumentsInClass(c);
		for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		    T_c[c][v] = countTokensOfTerm(textIndexes_c, v);
		    T += T_c[c][v];
		}
		for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		    this.conditionalProbabilities[v][c] = 
			1.0*(T_c[c][v] + 1)/(T + this.vocabulary.sizeOf());
		}    
	}
	//promptTrainingVariables();
    } // train()

    private void promptTrainingVariables() {
	System.out.println("priors");
	for(int c = 0; c < 2; c++){ 
	    System.out.println(this.priors[c]);
	}
	System.out.println("conditionals");
	for(int v = 0; v < this.vocabulary.sizeOf(); v++){ 
	    System.out.println(v +"\t"+this.vocabulary.getTermByIndex(v));
	    for(int c = 0; c < 2; c++){   
		System.out.println(this.conditionalProbabilities[v][c]);
	    }
	}
    } // promptTrainingVariables();

    /**
     *
     */
    private List<Integer> concatenateTextOfAllDocumentsInClass(int c) {
	List<Integer> textIndexes = new ArrayList<Integer>();
	for(Document trainingDoc : this.trainingDocuments) {
	    List<String> terms = trainingDoc.tokens;
	    if(c == 0) {
		if(trainingDoc.labelInformative) 
		    for(String term : terms)
			textIndexes.add(this.vocabulary.indexOfTerm(term));
	    } else if(c == 1) {
		if(!trainingDoc.labelInformative) 
		    for(String term : terms)
			textIndexes.add(this.vocabulary.indexOfTerm(term));
	    }
	}
	return textIndexes;
    } // concatenateTextOfAllDocumentsInClass()

    /**
     *
     */
    private int countTokensOfTerm(List<Integer> textIndexes_c, 
				  int vocabularyTermIndex) {
	int occurrences = 0;
	return occurrences = Collections.frequency
	    (textIndexes_c,vocabularyTermIndex);
    } // countTokensOfTerm()

    /**
     *
     *
     * @return
     */
    public int countInformatives() {
	int informativeCounter = 0;
	for(Document doc : this.trainingDocuments) {
	    if(doc.labelInformative) informativeCounter++;
	}
	return informativeCounter;
    } // countInformatives()

    /**
     *
     */
    public int countNonInformatives() {
	int nonInformativesCounter = 0;
	for(Document doc : this.trainingDocuments) {
	    if(!doc.labelInformative) nonInformativesCounter++;
	}
	return nonInformativesCounter;
    } // countNonInformatives()

} // Trainer