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

import infotweetsalgorithms.input.document.Document;
import infotweetsalgorithms.vocabulary.Vocabulary;

/**
 * TextClassifier:
 *
 *
 * @author kostas-κγ      
 */
public abstract class TextClassifier extends Classifier {
 
    protected double[] priors;
    protected double[][] conditionalProbabilities;
    protected Vocabulary vocabulary;

    public TextClassifier(List<Document> documents, int numPartitions) {
	super(documents, numPartitions);
	this.priors = new double[2];
	this.vocabulary = new Vocabulary();
    }

    protected void reset(List<Document> trainingDocuments) {
	this.priors = new double[2];
	this.vocabulary.build(trainingDocuments);
	int vocSize = this.vocabulary.sizeOf();
	this.conditionalProbabilities = new double[vocSize][2];
    }

    protected abstract void train(List<Document> trainingDocuments);

    protected abstract void apply(List<Document> testingDocuments);

} // TextClassifier
