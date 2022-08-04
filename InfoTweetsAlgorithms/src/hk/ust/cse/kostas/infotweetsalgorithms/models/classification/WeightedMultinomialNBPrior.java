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
import java.util.ArrayList;
import java.util.Collections;

import infotweetsalgorithms.input.document.Document;
import infotweetsalgorithms.algorithms.sampling.RandomSampler; 
import infotweetsalgorithms.algorithms.sampling.GibbsSampler; 

/**
 * WeightedMultinomialNB:
 *
 *
 * @author kostas-κγ 
 */
public class WeightedMultinomialNBPrior extends TextClassifier {

    public double weightFreqHashtags;
    public double weightFreqWords;
    public double weightFreqUsers;

    /**
     * Takes dataset and training data as input.
     * Applies Multivariate Bernouli Naive Bayes.
     */
    public WeightedMultinomialNBPrior
	(List<Document> documents, int numPartitions) {
	super(documents, numPartitions);
    }

    public void setWeights
	(double weightFreqHashtags, double weightFreqWords,
	 double weightFreqUsers) {	
	this.weightFreqHashtags = weightFreqHashtags;
	this.weightFreqWords = weightFreqWords;
	this.weightFreqUsers = weightFreqUsers;
    } // setWeights()

    /**
     *
     *
     * @param trainingDocuments
     */
    @Override protected void train(List<Document> trainingDocuments) {	
	//System.out.println("Voc size: " + this.vocabulary.sizeOf());
	RandomSampler randomSampler=new RandomSampler(trainingDocuments);
	randomSampler.sample(); 
	List<Document> samples = randomSampler.getSamplingDocuments();
	//GibbsSampler gibbsSampler = 
	//  new GibbsSampler(this.vocabulary, trainingDocuments);
	//gibbsSampler.sample();
	//List<Document> samples = gibbsSampler.getSamplingDocuments();
	this.vocabulary.fillinPriors(samples);
	double[][] samplePriors = this.vocabulary.getPriorFrequencies(); 
	double[] samplePriorFreqs = this.vocabulary.getPriorFreqs();

	int informativesCounter = this.splitter.countInformatives();
	int noninformativesCounter = this.splitter.countNonInformatives();
	int totalNum = informativesCounter + noninformativesCounter;
	double[] N_c = new double[2];
	N_c[0] = 1.0*informativesCounter;
	N_c[1] = 1.0*noninformativesCounter;
	int T = 0;
	int[][] T_c = new int[2][this.vocabulary.sizeOf()];

	for(int c = 0; c < 2; c++){
	    T = 0;
	    this.priors[c] = 1.0 * N_c[c]/totalNum; 
	    List<Integer> textIndexes_c = 
		concatenateTextOfAllDocumentsInClass(c, trainingDocuments);
	    double wf = 0.0;
	    for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		int cat = this.vocabulary.termCategoryByIndex(v);
		if(cat == 0)
		    wf = this.weightFreqWords;
	        else if(cat == 1)
		    wf = this.weightFreqHashtags;
		else if(cat == 2)
		    wf = weightFreqUsers;
		T_c[c][v] = (int)wf * countTokensOfTerm(textIndexes_c, v);
		T += T_c[c][v];
	    }
	    double sW = 0.0;
	    double ws = 0.0; 
	    for(int v = 0; v < this.vocabulary.sizeOf(); v++){
		//ws = samplePriors[v][c];
		ws = samplePriorFreqs[v];
		this.conditionalProbabilities[v][c] = 1.0*(T_c[c][v] + ws);
		sW += ws;
	    }
	    for(int v = 0; v < this.vocabulary.sizeOf(); v++) { 
		this.conditionalProbabilities[v][c] /= (T + sW);
		    //(T + this.vocabulary.sizeOf()); 
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
    } // promptTrainingVariables()

    /**
     *
     */
    private List<Integer> concatenateTextOfAllDocumentsInClass
	(int c, List<Document> trainingDocuments) {
	List<Integer> textIndexes = new ArrayList<Integer>();
	for(Document trainingDoc : trainingDocuments) {
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
		score[c] = 1.0 * this.priors[c];
		for(int t : docTokensIndexesInVocabulary) {
		    logScore[c] += Math.log
			(this.conditionalProbabilities[t][c]);
		    score[c] *= 1.0 * this.conditionalProbabilities[t][c];
		}
	    }
	    /* argmax score[c] */
	    doc.assignInformative = (score[0] > score[1]) ? true : false;
	    doc.assignProbability = 
		(score[1] > score[0]) ? score[1] : score[0];
	}    
    } // apply()

} // WeightedMultinomialNBPrior