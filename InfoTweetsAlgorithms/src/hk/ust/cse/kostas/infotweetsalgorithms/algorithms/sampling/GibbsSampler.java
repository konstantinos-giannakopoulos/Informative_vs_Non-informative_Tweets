package infotweetsalgorithms.algorithms.sampling;

import infotweetsalgorithms.algorithms.sampling.Tuple;
import infotweetsalgorithms.input.document.Document;
import infotweetsalgorithms.vocabulary.Vocabulary;

import probabilitiesstatistics.distributions.Bernoulli;

import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;
import java.util.Collections;

public class GibbsSampler {

    private int numofIterations;
    private Tuple currentSample;
    private Vocabulary vocabulary;
    private List<Document> documents;
    private String[] vocIndexesTermsMapper;
    private List<Document> samplingDocuments;
    //private List<Tuple> documentsTuples;

    public GibbsSampler(Vocabulary vocabulary, List<Document> documents) {
	this.numofIterations = 300;
	this.vocabulary = vocabulary;
	this.documents = documents;
	this.samplingDocuments = new ArrayList<Document>();
	//this.documentsTuples = new ArrayList<Tuple>[];
    } // GibbsSampler

    public List<Document> getSamplingDocuments() {
	return this.samplingDocuments;
    } // getSamplingDocuments()

    private void presample() {
	System.out.println("[Presampling step]");
	int vocLength = this.vocabulary.sizeOf();
	// initiallize sample
	this.currentSample = new Tuple(vocLength);
	this.currentSample.initiallizeVariableValues();
	/*
	this.vocIndexesTermsMapper = new String[vocLength];
	for(int termIndex = 0; termIndex < vocLength; termIndex++)
	    this.vocIndexesTermsMapper[termIndex] = 
	    this.vocabulary.getTermByIndex(termIndex); */
	// trasform documents to tuples
	for(int d = 0; d < this.documents.size(); d++) {
	    Document doc = this.documents.get(d);
	    doc.docTermsToVocIndexesMapper = new ArrayList<Integer>();
	    for(String term : doc.tokens) {
		int vocIndex = this.vocabulary.indexOfTerm(term);
		doc.docTermsToVocIndexesMapper.add(vocIndex);
	    }	    
	    //System.out.println(doc.docTermsToVocIndexesMapper);
	    //doc.initTuple(vocLength);
	    //doc.tuple = new Tuple(vocLength);
	    //doc.tuple.initiallizeVariableValues();
	    //for(int i = 0; i < this.vocabulary.sizeOf(); i++) {
	    //if(docTermsToVocIndexesMapper.contains(i))
	    //    doc.tuple.updateVariableAtIndex(1.0,i);
	    //else
	    //    doc.tuple.updateVariableAtIndex(0.0,i);
	    //}
	    //System.out.println(this.documentsTuples[d]);
	} // next doc
    } // presample()

    public void sample() {
	int numVariables = this.vocabulary.sizeOf();
	System.out.println(numVariables);
	presample();	
	System.out.println("[Sampling step]");
	for (int i = 0; i < numofIterations; i++) {
	    System.out.println("----- Iteration: " + i + " -----");  
	    //System.out.println("Sample: " + this.currentSample);
	    // numVariables
	    for(int varIndex = 0; varIndex < numVariables; varIndex++) {
		List<Integer> posOfExistingVariables
		    = this.currentSample.getVariablePositions();
		//System.out.println
		//  ("Resampling variable: " + 
		//   this.vocabulary.getTermByIndex(varIndex));
		List<Document> evidenceExistsInDocuments  = 
		    findDocswExistingVariables
		    (posOfExistingVariables, varIndex);		
		//System.out.println
		//("cond docs: "+ evidenceExistsInDocuments);
		//List<Tuple> evidenceExistsInTuples = 
		//  findTupleswConditionedVariables(varIndex);
		//System.out.println
		//  ("denom: "+evidenceExistsInTuples.size());
	   
		double p0 = 0.0;
		double p1 = 0.0;
		if(evidenceExistsInDocuments.size() != 0) {
		    int exists = queryVariableExists
			(evidenceExistsInDocuments, varIndex);
		    //System.out.println("exists: " + exists);
		    int notExists =evidenceExistsInDocuments.size()-exists;
		    if(evidenceExistsInDocuments.size() != 0) {
			p0 = (1.0 * notExists) / 
			    evidenceExistsInDocuments.size();	     
			p1 = (1.0 * exists) / 
			    evidenceExistsInDocuments.size();
		    }
		} else {
		    p0 = 1.0; 
		    p1 = 0.0;
		}
		//System.out.println("p[0]: " + p0 + "\tp[1]: " + p1);
		int gibbsRandomNumber = new Bernoulli(p0).generateRandom();
		int bucket = 1;
		if(gibbsRandomNumber == 0) bucket = 0;
		this.currentSample.updateVariableAtIndex(bucket, varIndex);
		//System.out.println("New Sample: " + this.currentSample);
	    } 
	    //System.out.println("Ok");
	    if(i >= 1) {
		System.out.println("New Sample: ");//+this.currentSample);
		List<Integer> termsIndexes = 
		    this.currentSample.getVariablePositions();
		List<String> sampleTerms = new ArrayList<String>();
		for(int termIndex : termsIndexes) {
		    sampleTerms.add
			(this.vocabulary.getTermByIndex(termIndex));
		}
		//////////(this.vocabulary.getTermByIndex(termIndex));
		if(sampleTerms.size() != 0) { 
		    this.samplingDocuments.add(new Document(sampleTerms));
		    for(String sampleTerm : sampleTerms)
			System.out.println(sampleTerms);
		    //BigDecimal id = new BigDecimal(-1);
		    //for(Document doc : this.documents)
		    //    if(doc.equals(this.currentSample)) {
		    //	id = doc.id;
		    //	break;
		    //}
		    //System.out.println("Sample is Document (id): " + id);
		}
	    } // end-if
	} // next iteration
    } // sample()

    /*
    private List<Tuple> findTupleswConditionedVariables(int varQueryIndex){
	List<Tuple> existsInTuples = new ArrayList<Tuple>();
	int vocLength = this.vocabulary.sizeOf();
	for(Document doc : this.documents) {
	    Tuple tuple = doc.tuple;
	    boolean found = true;
	    for(int i = 0; i < vocLength; i++) {
		if(i != varQueryIndex) {
		    if(this.currentSample.getVariableValueAtIndex(i) != 
		       tuple.getVariableValueAtIndex(i)) {
			found = false;
			break;
		    }
		}
	    }
	    if(found)
		existsInTuples.add(tuple);
	} // next tuple
	return existsInTuples;
    } // findTupleswConditionedVariables()
    */
    
    private List<Document> findDocswExistingVariables
	(List<Integer> posOfExistingVariables, int varQueryIndex) {
	List<Document> existInDocuments = new ArrayList<Document>();
	//System.out.println("tuple: " + posOfExistingVariables);
	//System.out.println("var: " + varQueryIndex);
	/*
	List<Integer> posOfExistingVars = new ArrayList<Integer>();
	posOfExistingVars.addAll(posOfExistingVariables);
	if(posOfExistingVariables.contains(varQueryIndex))
	    posOfExistingVars.remove(new Integer(varQueryIndex));
	    Collections.sort(posOfExistingVars);*/
	for(Document doc : this.documents) {
	    //System.out.println("document: " + doc.tokens);	    
	    List<Integer> docTermsIndexes = doc.docTermsToVocIndexesMapper;
	    /*
	    if(docTermsIndexes.contains(varQueryIndex))
		docTermsIndexes.remove(new Integer(varQueryIndex));
	    */
	    boolean keep = true;
	    for(int pos : posOfExistingVariables) {
		if(pos != varQueryIndex) {
		    //System.out.println
		    //("pos: " + pos + " var: " 
		    //+ this.vocabulary.getTermByIndex(pos));
		    if(!docTermsIndexes.contains(pos)){
			////////(this.vocabulary.getTermByIndex(pos))) {
			//System.out.println
			//(doc.tokens + " not contains "
			// + this.vocabulary.getTermByIndex(pos));
			keep = false;
			break;
		    }
		    //System.out.println
		    //(doc.tokens + " contains " + 
		    //this.vocabulary.getTermByIndex(pos));
		} // end-if
	    } // end for-loop
	    if(keep) {
		for(int pos : docTermsIndexes) {
		    if(pos != varQueryIndex) { 
			if(!posOfExistingVariables.contains(pos)) {
			    //System.out.println("tuple contains more!");
			    keep = false;
			    break;
			}
		    } // end-if
		}
	    }

	    /*
	    if(posOfExistingVars.size() != docTermsIndexes.size()) {
		keep = false;
		continue;
	    } else {
		Collections.sort(docTermsIndexes);
		if(posOfExistingVars.equals(docTermsIndexes)) {
		    keep = true;
		}
		}*/
	    if(keep)
		existInDocuments.add(doc);
	} // for-loop : next doc
	return existInDocuments;
    } // findDocswExistingVariables()
    
    private int queryVariableExists
	(List<Document> docs, int varQueryIndex) {
	int counter = 0;
	for(Document doc : docs) {
	    if(doc.docTermsToVocIndexesMapper.contains(varQueryIndex))
		counter++;
	}
	return counter;
    } // queryVariableExists()


    /**
     * Debug main.
     */
    public static void main(String[] args) {
	List<Document> documents = new ArrayList<Document>();
	List<String> terms = new ArrayList<String>();
	terms.add("a");	terms.add("c");	terms.add("f");
	documents.add(new Document(new BigDecimal(1), terms, true));
	terms = new ArrayList<String>();
	terms.add("a"); terms.add("d"); terms.add("e");
	documents.add(new Document(new BigDecimal(2), terms, true));
	terms = new ArrayList<String>();
	terms.add("a"); terms.add("d"); terms.add("f");
	documents.add(new Document(new BigDecimal(3), terms, true));
	terms = new ArrayList<String>();
	terms.add("b"); terms.add("c"); terms.add("g");
	documents.add(new Document(new BigDecimal(4), terms, false));
	terms = new ArrayList<String>();
	terms.add("b"); terms.add("d");terms.add("e"); terms.add("f");
	documents.add(new Document(new BigDecimal(5), terms, false));
	terms = new ArrayList<String>();
	terms.add("c"); terms.add("h");
	documents.add(new Document(new BigDecimal(6), terms, false));
	terms = new ArrayList<String>();
	terms.add("b"); terms.add("c");
	documents.add(new Document(new BigDecimal(7), terms, false));
	terms = new ArrayList<String>();
	terms.add("d"); terms.add("f");
	documents.add(new Document(new BigDecimal(8), terms, false));
	terms = new ArrayList<String>();
	terms.add("d");terms.add("e"); terms.add("f");
	documents.add(new Document(new BigDecimal(9), terms, false));
	terms = new ArrayList<String>();
	terms.add("e"); terms.add("f");
	documents.add(new Document(new BigDecimal(10), terms, false));
	terms = new ArrayList<String>();
	terms.add("c");terms.add("d"); terms.add("f");
	documents.add(new Document(new BigDecimal(11), terms, true));

	System.out.println(documents);

	Vocabulary vocabulary = new Vocabulary();
	vocabulary.build(documents);
	GibbsSampler gs = new GibbsSampler(vocabulary, documents);
	gs.sample();
    } // main()

} // GibbsSampler