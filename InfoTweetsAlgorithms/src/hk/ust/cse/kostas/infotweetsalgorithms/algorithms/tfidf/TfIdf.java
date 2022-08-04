package infotweetsalgorithms.algorithms.tfidf;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import infotweetsalgorithms.input.document.Document;
import infotweetsalgorithms.vocabulary.Vocabulary;

public class TfIdf {

    Vocabulary vocabulary;
    List<Document> documents;
    int [] tf;
    double [] idf;
    double [] tfidf;
    
    public TfIdf(List<Document> documents, Vocabulary vocabulary) {
	this.documents = documents;
	this.vocabulary = vocabulary;

	this.tf = new int[this.vocabulary.sizeOf()];
	this.idf = new double[this.vocabulary.sizeOf()];
	this.tfidf = new double[this.vocabulary.sizeOf()];
	Arrays.fill(this.tf,0);
	Arrays.fill(this.idf,0.0);
	Arrays.fill(this.tfidf,0.0);
    } // TfIdf()

    private void estimateTf() {
	int size = this.vocabulary.sizeOf();
	for(Document doc : this.documents) {  
	    for(String term : doc.tokens) {  
		int index = this.vocabulary.indexOfTerm(term);
		if(index >= 0)
		    this.tf[index] = this.vocabulary.getTermFrequency(term);
	    }	    
	}
	//return tf;
    } // estimateTf()

    private void estimateIdf() {
	this.vocabulary.estimateDocumentFrequencies(this.documents);
	int totalNumDocuments = this.documents.size();
	for(int i = 0; i < this.vocabulary.sizeOf(); i++) {
	    int documentFrequency = 0;
	    String term = this.vocabulary.getTermByIndex(i);
	    documentFrequency = 
		this.vocabulary.getDocumentFrequencyForTerm(term);
	    double l = 1.0 * totalNumDocuments / documentFrequency;
	    double idfv = Math.log10(l); 
	    this.idf[i] = idfv;
	} // 
	//return idf;
    } // estimateTfIdf()

    public void estimateTfIdf() {
	estimateTf();
	estimateIdf();
	for(int i = 0; i < this.tfidf.length; i++) {
	    this.tfidf[i] = 1.0 * this.tf[i] * this.idf[i];
	}
    } // estimateTfIdf()

    public double getScoreByVocabularyIndex(int index) {
	return this.tfidf[index];
    } // getScore()

    @Override public String toString() {
	//estimateTfIdf();
	StringBuilder sb = new StringBuilder();
	sb.append("term\ttfidf\t\ttf\tidf");
	for(int i = 0; i < this.tfidf.length; i++) {
	    sb.append("\n");
	    String term = this.vocabulary.getTermByIndex(i);
	    sb.append(term + "\t" + this.tfidf[i] 
		      + "\t\t" + this.tf[i] + "\t " + this.idf[i]);
	}
	return sb.toString();
    } // toString()

} // TfIdf