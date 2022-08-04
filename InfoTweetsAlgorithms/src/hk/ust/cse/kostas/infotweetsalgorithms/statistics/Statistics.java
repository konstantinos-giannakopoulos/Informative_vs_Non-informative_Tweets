package infotweetsalgorithms.statistics;

import java.util.List;
import java.util.ArrayList;

import infotweetsalgorithms.vocabulary.Vocabulary;
import infotweetsalgorithms.input.document.Document; 
import infotweetsalgorithms.algorithms.tfidf.TfIdf;

public class Statistics {

    public Statistics() {

    } // Statistics()

    public static void vocabularyStats(List<Document> documents) {
	Vocabulary vocabulary = new Vocabulary();
	vocabulary.build(documents);

	List<Integer> informativePMF = new ArrayList<Integer>(); 
	List<Integer> noninformativePMF = new ArrayList<Integer>(); 
	
	for(int i = 0; i < vocabulary.sizeOf(); i++) {
	    informativePMF.add(i,0);
	    noninformativePMF.add(i,0);
	}
	for(Document doc : documents) {
	    for(String term : doc.tokens) { 
		boolean informative = doc.labelInformative;
		int termIndexInVoc = vocabulary.indexOfTerm(term);
		if(termIndexInVoc >= 0) {
		    if(informative) {
			int cntr = informativePMF.get(termIndexInVoc);
			cntr = cntr + 1;
			informativePMF.set(termIndexInVoc,cntr);
		    } else {
			int cntr = noninformativePMF.get(termIndexInVoc);
			cntr = cntr + 1; 
			noninformativePMF.set(termIndexInVoc,cntr);
		    }
		}
	    }
	}

	// print
	System.out.println("term\tfreq\tinformative\tnoninformative");
	for(int i = 0; i < vocabulary.sizeOf(); i++) {
	    String term = vocabulary.getTermByIndex(i);
	    System.out.println(term
			       + "\t" + vocabulary.getTermFrequency(term)
			       + "\t" + informativePMF.get(i) 
			       + "\t" + noninformativePMF.get(i));
	}
    } // vocabularyStats()

    public static void tfidfStats(List<Document> documents) {
	Vocabulary vocabulary = new Vocabulary();
	vocabulary.build(documents);

	TfIdf tfidf = new TfIdf(documents,vocabulary);
	System.out.println(tfidf);
    } // tfidfStats()

} // Statistics