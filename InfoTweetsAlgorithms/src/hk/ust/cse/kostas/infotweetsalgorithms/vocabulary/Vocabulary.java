package infotweetsalgorithms.vocabulary;

import java.util.List;
import java.util.ArrayList;

import infotweetsalgorithms.input.document.Document;

public class Vocabulary {

    private List<String> terms;
    private List<Integer> frequencies;
    private List<Integer> documentFrequencies;
    private double[][] priorFrequencies; // from the samples
    private double[] priorFreqs; 

    public Vocabulary() {
	this.terms = new ArrayList<String>();
	this.frequencies = new ArrayList<Integer>();
	this.documentFrequencies = new ArrayList<Integer>();
    } // Vocabulary()

    public void addTerm(String term) {
	if(!this.terms.contains(term)) {
	    this.terms.add(term);
	    int freq = 1;
	    this.frequencies.add(freq);
	    this.documentFrequencies.add(0);
	} else {
	    int freq = getTermFrequency(term);
	    int index = indexOfTerm(term);
	    freq++;
	    this.frequencies.set(index,freq);
	}
    } // addTerm()

    public void removeTerm(String term) {
	int termIndex = indexOfTerm(term);
	this.frequencies.remove(termIndex);
	this.documentFrequencies.remove(termIndex);
    } // removeTerm()


    /**
     * Returns the number of terms in the vocabulary
     *
     * @returns: 
     */
    public int sizeOf() {
	return this.terms.size();
    }

    /**
     * Given a term, returns its index position in the vocabulary. 
     *
     * @param:
     * @returns: 
     */
    public int indexOfTerm(String term) {
	return this.terms.indexOf(term);
    }

    public boolean termIsHashTagByIndex(int index) {
	String token = this.terms.get(index);
	if(token.startsWith("#"))
	    return true;
	return false;
    } // termIsHashTagByIndex()

    public boolean termIsUserByIndex(int index) {
	String token = this.terms.get(index);
	if(token.startsWith("@"))
	    return true;
	return false;
    } // termIsUserByIndex()

    public int termCategoryByIndex(int index) {
	String token = this.terms.get(index);
	if(token.startsWith("#"))
	    return 1;
	if(token.startsWith("@"))
	    return 2;
	return 0;
    } // termIsHashTagByIndex()


    /**
     * Given an index position, returns the term that is stored there.
     *
     * @param:
     * @returns:
     */
    public String getTermByIndex(int index) {
	return this.terms.get(index);
    }

    public int getTermFrequency(String term) {
	int index = this.terms.indexOf(term);
	if (index >= 0) 
	    return this.frequencies.get(index);
	else 
	    return -1;
    }

    public int getDocumentFrequencyForTerm(String term) {
	int index = this.terms.indexOf(term);
	return this.documentFrequencies.get(index);
    }

    public void build(List<Document> documents) {
	clear();
	for(Document doc : documents) {
	    for(String term : doc.tokens) { 
		if(term.startsWith("http")) continue;
		//if(term.contains("@")||term.startsWith("http")) continue;
		//if(term.startsWith("#") || term.contains("@")  
		// || term.startsWith("http")) continue; 
		addTerm(term);  
	    }
	}
    } // build()

    public void estimateDocumentFrequencies(List<Document> documents) {
	for(String term : this.terms) {	    
	    int termIndex = indexOfTerm(term);
	    //if(termIndex >= 0) {
	    int documentFrequency = 0;
	    for(Document doc : documents) {
		if(doc.tokens.contains(term))
		    documentFrequency++;
	    }
	    this.documentFrequencies.set(termIndex,documentFrequency);
	}//}
    } // estimateDocumentFrequencies()

    /**
     * vocabulary has to be built first, because its size 
     * is needed.
     */
    public void fillinPriors(List<Document> documents) {
	build(documents);
	int length = this.terms.size();
	System.out.println("size of the sample: " + documents.size());
	this.priorFrequencies = new double[length][2];
	double[] total = new double[2];
	this.priorFreqs = new double[length];
	double tot = 0.0;
	total[0] = 0.0; total[1] = 0.0;
	for(int i = 0; i < length; i++) {
	    for(int j = 0; j < 2; j++) {
		this.priorFrequencies[i][j] = 1.0;
		total[j] += 1.0;
	    }
	    this.priorFreqs[i] = 1.0;
	    tot += 1.0;
	}
	for(Document doc : documents) {
	    int c = 1;
	    if(doc.labelInformative) c = 0;
	    for(String term : doc.tokens) {
		int index = indexOfTerm(term);
		if(index != -1) {
		    this.priorFrequencies[index][c]++;
		    total[c] += 1.0;
		    tot += 1.0; 
		}
	    }	    
	}
	for(int i = 0; i < length; i++)  
	    for(int j = 0; j < 2; j++)
		this.priorFrequencies[i][j] /= total[j];

	for(int i = 0; i < length; i++)  
	    this.priorFreqs[i] /= tot;

	//for(int i = 0; i < length; i++)  
	//  for(int j = 0; j < 2; j++)
	//System.out.println(getTermByIndex(i) + 
	//		   "\t" + this.priorFrequencies[i][j]);
    } // fillinPriors()

    public double[] getPriorFreqs() {
	return this.priorFreqs;
    } // getPriorFreqs()

    public double[][] getPriorFrequencies() {
	return this.priorFrequencies;
    } // getPriorFrequencies()

    public double getPriorFrequencyAtIndex(int index, int c) {
	return this.priorFrequencies[index][c];
    } // getPriorFrequencyAtIndex()

    public void clear() {
	this.terms.clear();
	this.frequencies.clear();
    }

} // Vocabulary