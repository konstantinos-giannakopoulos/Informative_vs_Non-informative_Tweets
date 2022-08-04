package infotweetsalgorithms.input.document;

import java.util.List;
import java.math.BigDecimal;

//import infotweetsalgorithms.algorithms.sampling.Tuple;

public class Document {

    public BigDecimal id;
    public List<String> tokens;
    public int[] values;
    public boolean labelInformative;
    public boolean assignInformative;
    public double assignProbability;
    public List<Integer> docTermsToVocIndexesMapper;
  //public Tuple tuple;

    /**
     * Constructor used for TextClassifier only.
     */
    public Document(BigDecimal id, 
		    List<String> tokens, boolean labelInformative) {
	this.id = id;
	this.tokens = tokens;
	this.labelInformative = labelInformative;
    } // Document()

    /**
     * Constructor used for Bayesian Network only.
     */
    public Document(BigDecimal id,
		    int [] values, boolean labelInformative) {
	this.id = id;
	this.values = values;
	this.labelInformative = labelInformative;
    } // Document()

    /**
     * Constructor used for Hybrid only.
     */
    public Document(BigDecimal id, List<String> tokens,
		    int [] values, boolean labelInformative) {
	this.id = id;
	this.tokens = tokens;
	this.values = values;
	this.labelInformative = labelInformative;
    } // Document()

    public Document(List<String> tokens) {
	this.tokens = tokens;
    } // Document()
    /*
    public void initTuple(int length) {
	this.tuple = new Tuple(length);
	this.tuple.initiallizeVariableValues();
	}*/

    @Override public boolean equals(Object o) {
	if(o == this) 
	    return true;
	if(!(o instanceof Document)) 
	    return false;
	Document doc = (Document)o;
	if(this.tokens.size() != doc.tokens.size())
	    return false;
	return this.tokens.equals(doc);
    } // equals();

    @Override public String toString() {
	StringBuilder str = new StringBuilder(); 
	str.append("\n");     
	str.append("\n[id] " + this.id); 
	str.append("\n[label] " + this.labelInformative); 
	str.append("\n");  
	for(String token : this.tokens)
	    str.append(token + "\t");  
	return str.toString();	    
    }

} // Documnent