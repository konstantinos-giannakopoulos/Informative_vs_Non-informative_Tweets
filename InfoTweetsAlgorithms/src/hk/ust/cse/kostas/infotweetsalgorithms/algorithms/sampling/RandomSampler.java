package infotweetsalgorithms.algorithms.sampling;

import java.util.List;
import java.util.ArrayList;

import infotweetsalgorithms.input.document.Document;  

public class RandomSampler {

    private List<Document> documents;
    private List<Document> samplingDocuments;

    public RandomSampler(List<Document> documents) {
	this.documents = documents;
	this.samplingDocuments = new ArrayList<Document>();
    } // RandomSampler()

    public List<Document> getSamplingDocuments() {
	return this.samplingDocuments;
    }

    public void sample() {
	for(Document document : documents) {
	    if(Math.random() < 0.1)
		this.samplingDocuments.add(document);
	}
    } // sample

} // RandomSampler