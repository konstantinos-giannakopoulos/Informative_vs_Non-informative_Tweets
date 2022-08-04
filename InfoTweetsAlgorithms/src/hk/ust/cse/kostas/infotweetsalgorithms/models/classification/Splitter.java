package infotweetsalgorithms.models.classification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import infotweetsalgorithms.input.document.Document;

/**
 * Splitter: Given a training dataset for cross validation, 
 * splits the dataset into training and testing keeping the 
 * analogy: 80% training, 20% testing.
 *
 * @author kostas-κγ  
 */
public class Splitter {

    private List<Document> trainingDocuments;
    private List<Document> testingDocuments;
    private List<Document> documents;

    private int allDatasetSize;
    private int trainingDatasetSize;
    private int partitionSize;
    private int datasetStartOffset;

    /**
     * Construct a new Splitter instance, given a pointer start offset 
     * in training dataset, the size of training dataset, the size of the partition,
     * a training dataset.
     *
     * @param datasetStartOffset a pointer start offset in the training dataset
     * @param trainingSize the size of the training dataset
     * @param partitionSize the size of the partition
     * @param tweets the training dataset
     */
    public Splitter(int datasetStartOffset, int trainingSize, 
		    int partitionSize, List<Document> documents) {
	this.documents = documents;
	this.datasetStartOffset = datasetStartOffset;
	this.trainingDatasetSize = trainingSize;
	this.partitionSize = partitionSize;

	this.allDatasetSize = documents.size();

	this.trainingDocuments = new ArrayList<Document>();
	this.testingDocuments = new ArrayList<Document>();
    } // Splitter()

    public int getTrainingDatasetSize() {
	return this.trainingDatasetSize;
    }

    public List<Document> getTrainingDocuments() {
	return this.trainingDocuments;
    }

    public List<Document> getTestingDocuments() {
	return this.testingDocuments;
    }

    /**
     *
     */
    public void splitDataset() {
	int toOffset = this.datasetStartOffset + this.trainingDatasetSize;
	int trainIndex = -1;	
	for(int i = 0; i < this.datasetStartOffset; i++) {
	    this.trainingDocuments.add(this.documents.get(i));
	}
	int predictIndex = -1;
	int predictFromOffset = this.datasetStartOffset;
	int predictToOffset = predictFromOffset + this.partitionSize; 
	for(int i = predictFromOffset; i < predictToOffset; i++) {
	    this.testingDocuments.add(this.documents.get(i));
	}
	for(int i = predictToOffset; i < allDatasetSize; i++) {
	    this.trainingDocuments.add(this.documents.get(i));
	}
    } // createTrainingDataset()
    
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

    /**
     *
     *
    public int countOutliersLength() {
	int outliersLength = 0;
	for(TweetItem tweet : this.trainingTweets) {
	    Features features = tweet.getFeatures();
	    WordVector wordVector = features.getWordVector();
	    if(features == null) continue;
	    if(features.isLabeledAsOutlier()) outliersLength += wordVector.getLength();
	}
	return outliersLength;
    } // countOutliersLength

    /**
     *
     *
    public int countNonOutliersLength() {
	int nonOutliersLength = 0;
	for(TweetItem tweet : this.trainingTweets) {
	    Features features = tweet.getFeatures();
	    WordVector wordVector = features.getWordVector();
	    if(features == null) continue;
	    if(!features.isLabeledAsOutlier()) nonOutliersLength += wordVector.getLength();
	}
	return nonOutliersLength;
    } // countNonOutliersLength
*/
} // Splitter