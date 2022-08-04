package infotweetsalgorithms.client;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import infotweetsalgorithms.models.clustering.Clustering;
import infotweetsalgorithms.models.clustering.LOF;
import infotweetsalgorithms.models.clustering.DB;

import infotweetsalgorithms.models.classification.Classifier;
import infotweetsalgorithms.models.classification.TextClassifier;
import infotweetsalgorithms.models.classification.MultinomialNB;
import infotweetsalgorithms.models.classification.Trainer;
import infotweetsalgorithms.models.classification.MultivariateBernoulliNB;
import infotweetsalgorithms.models.classification.BayesianNetwork;
import infotweetsalgorithms.models.classification.Hybrid;
import infotweetsalgorithms.models.classification.WeightedMultinomialNB;
import infotweetsalgorithms.models.classification.WeightedMultinomialNBPrior;
import infotweetsalgorithms.models.classification.ComplementMultinomialNB;
import infotweetsalgorithms.models.classification.NormalizedWeightVectorsMNB;

import infotweetsalgorithms.input.datapoint.Datapoint;
import infotweetsalgorithms.input.document.Document;

import infotweetsalgorithms.vocabulary.Vocabulary;
import infotweetsalgorithms.statistics.Statistics;

import infotweetsalgorithms.algorithms.sampling.RandomSampler;
import infotweetsalgorithms.algorithms.sampling.GibbsSampler;

public class InfoTweetsAlgorithms {

    List<Datapoint> datapoints;
    List<Document> documents;

    public InfoTweetsAlgorithms(List<Datapoint> datapoints,
				List<Document> documents) {
	this.datapoints = datapoints;
	this.documents = documents;
    } // InfoTweetsAlgorithms()

    public void run(String mode, String clusteringChoice, int k,
		    String classificationChoice, 
		    String textClassifierChoice, int cvPartitions,
		    double weightFreqHashtags, double weightFreqWords,
		    double weightFreqUsers, double weightSmoothHashtags,
		    double weightSmoothWords, double weightSmoothUsers) {
	if(mode.equals("clustering"))
	    runClusteringAlgorithms(clusteringChoice, k);
	else if(mode.equals("classification")) 
	    runClassificationAlgorithms
		(classificationChoice, textClassifierChoice,cvPartitions,
		 weightFreqHashtags, weightFreqWords, weightFreqUsers, 
		 weightSmoothHashtags,weightSmoothWords,weightSmoothUsers);
	else if(mode.equals("statistics"))
	    runStatisticsAlgorithms();
	else if(mode.equals("sampling"))
	    runSampling(cvPartitions,weightFreqHashtags, 
			weightFreqWords,weightFreqUsers);
    } // run()
	
    private void runClusteringAlgorithms(String clusteringChoice, int k) {
	// Clustering algorithm
	Clustering method = null;
	if(clusteringChoice.equals("lof")) {
	    method = new LOF(this.datapoints);
	    System.out.println("[dbg] k : " + k);
	    System.out.println
		("How many datapoints: " + this.datapoints.size());
	    ((LOF)method).setk(k);
	} else if(clusteringChoice.equals("db")) {
	        method = new DB(this.datapoints);
	} else {
	    throw new IllegalArgumentException
		("Clustering choice not found.");
	}
	method.doClustering();    
    } // runClusteringAlgorithms()

    private void runClassificationAlgorithms
	(String classificationChoice, String textClassifierChoice,
	 int cvPartitions,
	 double weightFreqHashtags, double weightFreqWords,
	 double weightFreqUsers, double weightSmoothHashtags,
	 double weightSmoothWords, double weightSmoothUsers) {
	Classifier classifier = null;
	if(classificationChoice.equals("tc")) {
	    if(textClassifierChoice.equals("multinomial")) {  
		classifier = new MultinomialNB
		    (this.documents,cvPartitions); 
	    } else if (textClassifierChoice.equals("bernoulli")) { 
		classifier = new MultivariateBernoulliNB
		    (this.documents,cvPartitions); 
	    }  else if (textClassifierChoice.equals("weightedMNB")) { 
		classifier = new WeightedMultinomialNB
		    (this.documents,cvPartitions); 
		((WeightedMultinomialNB)classifier).setWeights
		    (weightFreqHashtags, weightFreqWords,
		     weightFreqUsers, weightSmoothHashtags,
		     weightSmoothWords, weightSmoothUsers);
	    }  else if (textClassifierChoice.equals("complementMNB")) { 
		classifier = new ComplementMultinomialNB
		    (this.documents,cvPartitions); 
	    }  else if (textClassifierChoice.equals
			("normalizedWeightVectorsMNB")) { 
		classifier = new NormalizedWeightVectorsMNB
		    (this.documents,cvPartitions); 
	    } else {
		throw new IllegalArgumentException
		    ("Classification choice not found.");
	    }
	} else if(classificationChoice.equals("bn")) {
	    classifier = new BayesianNetwork(this.documents,cvPartitions); 
	} else if(classificationChoice.equals("hybrid")) {
	    classifier = new Hybrid(this.documents,cvPartitions); 
	} else {
	    throw new IllegalArgumentException
		("Classification choice not found.");
	}
	classifier.doClassification();       
	//for(Document doc : this.documents) {
	//    if(doc.labelInformative && !doc.assignInformative)
	//	System.out.println(doc);
	//}
    } // runClassificationAlgorithms()

    private void runStatisticsAlgorithms() {
	//Statistics.vocabularyStats(this.documents);
	Statistics.tfidfStats(this.documents);
    } // runStatisticsAlgorithms()

    private void runSampling(int cvPartitions, double weightFreqHashtags, 
			     double weightFreqWords, 
			     double weightFreqUsers) {	

	//Trainer trainer = new Trainer(this.documents);
	//trainer.train();
	// sample
	///RandomSampler randomSampler = new RandomSampler(this.documents);
	///randomSampler.sample();
	///List<Document> samples = randomSampler.getSamplingDocuments();
	// collect priors
	//voc.fillinPriors(samples);
	//double[][] samplePriors = voc.getPriorFrequencies();
	///Vocabulary voc = new Vocabulary();
	///voc.fillinPriors(samples);
	///double[][] samplePriors = voc.getPriorFrequencies(); 
	// call weightedMNB model

	System.out.println("Sampling");
	
	Classifier classifier = new WeightedMultinomialNBPrior
	    (this.documents, cvPartitions); 
	((WeightedMultinomialNBPrior)classifier).setWeights
	    (weightFreqHashtags, weightFreqWords,
	     weightFreqUsers);
	Vocabulary voc = new Vocabulary();
	voc.build(this.documents);
	//GibbsSampler gibbsSampler = new GibbsSampler(voc,this.documents);
	//gibbsSampler.sample();
	classifier.doClassification();	
    } // runSampling()

    public static void main(String [] args) {
	List<Datapoint> datapoints = new ArrayList<Datapoint>();
	List<Document> documents = new ArrayList<Document>();
	InfoTweetsAlgorithms itAlgorithms = 
	    new InfoTweetsAlgorithms(datapoints,documents);	
	String mode = args[1];
	String clusteringChoice = args[2];
	int k = Integer.parseInt(args[3]);
	String classificationChoice = args[4];
	String textClassifierChoice = args[5];
	int cvPartitions = Integer.parseInt(args[6]);
	double weightFreqHashtags = Double.parseDouble(args[7]);
	double weightFreqWords = Double.parseDouble(args[8]);
	double weightFreqUsers = Double.parseDouble(args[9]);
	double weightSmoothHashtags = Double.parseDouble(args[10]);
	double weightSmoothWords = Double.parseDouble(args[11]);
	double weightSmoothUsers = Double.parseDouble(args[12]);
	
	try {
	    itAlgorithms.run
		(mode,clusteringChoice,k, classificationChoice,
		 textClassifierChoice, cvPartitions,
		 weightFreqHashtags, weightFreqWords, weightFreqUsers,
		 weightSmoothHashtags,weightSmoothWords,weightSmoothUsers);
	} catch(IllegalArgumentException illargex) {
	    illargex.printStackTrace();
	}
    } // main()

} // InfoTweetsAlgorithms